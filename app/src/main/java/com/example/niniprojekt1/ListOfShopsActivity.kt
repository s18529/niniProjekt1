package com.example.niniprojekt1

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niniprojekt1.databinding.ActivityListOfShopsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


class ListOfShopsActivity : AppCompatActivity() {


    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityListOfShopsBinding
    private lateinit var geoClient: GeofencingClient
    private lateinit var shops: HashMap<String, Shop>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfShopsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productViewModel = ProductViewModel(application)
        var shopAdapter = ShopAdapter(productViewModel)

        val productListActivity = Intent(applicationContext, MapOfShopsActivity::class.java)


        productViewModel.allShops.observe(this, Observer { shopsList ->
            shopsList.let {
                shopAdapter.setShops(shopsList.values.toList())
            }
        })

        binding.mapButton.setOnClickListener {
            startActivity(productListActivity)
        }

//        binding.buttonDodaj.setOnClickListener {
//            addAnnotationToMapFromList(binding.eTName.text.toString())
//        }



        binding.rv2.layoutManager = LinearLayoutManager(this)
        //DividerItemDecorator (optional)
        binding.rv2.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        //Adapter do Listy
        binding.rv2.adapter = shopAdapter

        val newList: List<Shop> = emptyList()

        geoClient = LocationServices.getGeofencingClient(this)

        binding.buttonDodaj.setOnClickListener {
            addAnnotationToMapFromList(binding.eTName.text.toString())
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), 1
                )
            }
            binding.eTName.text.clear()
            binding.etOpis.text.clear()
            binding.EtPromien.text.clear()


        }
        productViewModel.allShops.observe(this, Observer {
            shops = it


            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener {
                    if(it==null){
                        Log.e("geofenceApp", "Location is null.")
                    }else{
                        Log.i("geofenceApp", "Location: ${it.latitude}, ${it.longitude}")
                    }
                    for (i in shops.values){
                        addGeofence(i.name, i.longitude, i.latitude, i.radius)
                    }

                }
                .addOnFailureListener {
                    Log.e("geofenceApp", "Location error: ${it.message.toString()}")
                }
        })

    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(name: String,longitude: Double,latitude: Double, radius: Long){

        val geofence = Geofence.Builder()
            .setRequestId("${name}")
            .setCircularRegion(latitude, longitude, radius.toFloat())
            .setExpirationDuration(30*60*1000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geoRequest = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .build()

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            Intent(this, GeoReceiver::class.java),
            PendingIntent.FLAG_MUTABLE
        )

        geoClient.addGeofences(geoRequest, pendingIntent)
            .addOnSuccessListener {
                Log.i("geofenceApp", "Geofence: ${geofence.requestId}  is added!")

            }
            .addOnFailureListener {
                Log.e("geofenceApp", it.message.toString()) //ERROR 1004 = missing ACCESS_BACKGROUND_PERMISSION
            }
    }


    private fun addAnnotationToMapFromList(title: String){


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),1)
            return
        }
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


        if (location != null) {
            //Toast.makeText(this, location.longitude.toString(), Toast.LENGTH_SHORT).show()
        }

            var productViewModel = ProductViewModel(application)
            val shop = Shop(
                id = "1",
                name = title,
                longitude = location?.longitude!!,
                latitude = location.latitude,
                description = binding.etOpis.text.toString(),
                radius = (binding.EtPromien.text.toString()).toLong(),
                best = false)
            productViewModel.insertShop(shop)
    }


}