package com.example.niniprojekt1

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.niniprojekt1.databinding.ActivityMainBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var auth: FirebaseAuth
    private lateinit var geoClient: GeofencingClient
    private lateinit var shops: HashMap<String, Shop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sp = getSharedPreferences("mainSP", Context.MODE_PRIVATE)
        editor = sp.edit()

        var productViewModel = ProductViewModel(application)
        var productadapter = ProductAdapter(productViewModel)


        auth = FirebaseAuth.getInstance()

        val productListActivity = Intent(applicationContext, ProductListActivity::class.java)
        val optionsActivity = Intent(applicationContext, OptionsActivity::class.java)
        val mainActivity = Intent(applicationContext, MainActivity::class.java)


        binding.buttonList.setOnClickListener {
            val mauth = FirebaseAuth.getInstance().currentUser
            if (mauth != null) {
                Toast.makeText(this,mauth.toString(),Toast.LENGTH_LONG).show()
                productListActivity.putExtra("Auth",FirebaseAuth.getInstance().currentUser?.uid)
                startActivity(productListActivity)
            } else{
                    Toast.makeText(this,"najpeirw sie zaloguj",Toast.LENGTH_LONG).show()
                }
        }

        binding.singout.setOnClickListener {
            auth.signOut()
            startActivity(mainActivity)
        }


        binding.buttonOptions.setOnClickListener {
            startActivity(optionsActivity)
        }



        if (sp.getBoolean("nightMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        delegate.applyDayNight()

        binding.buttonreg.setOnClickListener {
            Toast.makeText(this, binding.email.text.toString().toString(), Toast.LENGTH_LONG).show()
            auth.createUserWithEmailAndPassword(
                binding.email.text.toString(),
                binding.pass.text.toString()

            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Rejestracja powiodła się", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Rejestracja nie powiodła się", Toast.LENGTH_LONG).show()
                    Log.e("rejestracja", it.exception?.message.toString())
                }
            }

        }

        binding.buttonLogin.setOnClickListener {

            auth.signInWithEmailAndPassword(
                binding.email.text.toString(),
                binding.pass.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val upIntent = Intent(this,ProductListActivity::class.java)
                    Toast.makeText(this, "Logowanie powiodło się", Toast.LENGTH_LONG).show()
                    startActivity(upIntent)
                } else {
                    Toast.makeText(this, "Logowanie nie powiodło się", Toast.LENGTH_LONG).show()
                    Log.e("logowanie", it.exception?.message.toString())
                }
            }
        }


//        val newList: List<Shop> = emptyList()
//
//        geoClient = LocationServices.getGeofencingClient(this)
//
//        binding.buttonList.setOnClickListener {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                    ), 1
//                )
//            }
//
//        }
//        productViewModel.allShops.observe(this, Observer {
//            shops = it
//
//
//            LocationServices.getFusedLocationProviderClient(this).lastLocation
//                .addOnSuccessListener {
//                    if(it==null){
//                        Log.e("geofenceApp", "Location is null.")
//                    }else{
//                        Log.i("geofenceApp", "Location: ${it.latitude}, ${it.longitude}")
//                    }
//                    for (i in shops.values){
//                        addGeofence(i.name, i.longitude, i.latitude, i.radius)
//                    }
//
//                }
//                .addOnFailureListener {
//                    Log.e("geofenceApp", "Location error: ${it.message.toString()}")
//                }
//        })
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(name: String,longitude: Double,latitude: Double, radius: Long){

        val geofence = Geofence.Builder()
            .setRequestId("geo ${name}")
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




    override fun onStart() {
        super.onStart()

        if (sp.getBoolean("backgroundColor",false)){
            binding.buttonList.setBackgroundColor(getColor(R.color.green))
            binding.buttonOptions.setBackgroundColor(getColor(R.color.green))
        }else{
            binding.buttonList.setBackgroundColor(getColor(R.color.purple_200))
            binding.buttonOptions.setBackgroundColor(getColor(R.color.purple_200))
        }
    }
}