package com.example.niniprojekt1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.niniprojekt1.databinding.ActivityMapOfShopsBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

class MapOfShopsActivity : AppCompatActivity() {


    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMapOfShopsBinding

    private lateinit var shops: HashMap<String, Shop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapOfShopsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productViewModel = ProductViewModel(application)

        binding.mapView.also {
            it.getMapboxMap().loadStyleUri(
                Style.MAPBOX_STREETS) {
                productViewModel.allShops.observe(this, Observer {

                    shops = it
                    for (i in shops.values){
                        addAnnotationToMap(i.name, i.longitude, i.latitude)
                    }
                })
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun addAnnotationToMap(title: String,longitude: Double,latitude: Double){
        val pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        val marker = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val scaledMarker = Bitmap.createScaledBitmap(marker, (marker.width*0.3).toInt(), (marker.height*0.3).toInt(), true)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
                1)
            return
            }

            val paOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(scaledMarker)
                .withTextAnchor(TextAnchor.TOP)
                .withTextField(title)

            pointAnnotationManager.create(paOptions)

    }
}