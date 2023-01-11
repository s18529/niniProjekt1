package com.example.niniprojekt1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.lang.Math.random
import kotlin.math.log
import kotlin.random.Random

class GeoReceiver : BroadcastReceiver() {

    private val channelID = "shopChannel"

    override fun onReceive(context: Context, intent: Intent) {


        var pom = Random.nextInt(0, 1000)

        var pomString = ""

        val geofenceEvent = GeofencingEvent.fromIntent(intent)
        for (geofence in geofenceEvent.triggeringGeofences){
            if (geofence!=null)
                Log.i("geofence", "Geofence with id: ${geofence.requestId} TRIGGERED")

            if (geofenceEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.i("geofence1", "We entered to ${geofence.requestId}.")
                pomString = "We entered to ${geofence.requestId}."

            }else if (geofenceEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                Log.i("geofence1", "We left from ${geofence.requestId}.")
                pomString = "We left from ${geofence.requestId}."

            }else{
                Log.i("geofence1", geofenceEvent.geofenceTransition.toString())
                Log.i("geofence1", geofenceEvent.errorCode.toString())
            }
        }
        createNotificationChannel(context)
        val actIntent = Intent(context, ProductListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            actIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, channelID)
            .setContentTitle(pomString)
            .setContentText("sadsdsaadsadsadsdas")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setGroup("noti Shop Group")
            .build()


       with( NotificationManagerCompat.from(context)){
           notify(pom, notification)
       }
    }

    private fun createNotificationChannel(context: Context){
        val notificationChannel = NotificationChannel(
            channelID,
            "Student Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context)
            .createNotificationChannel(notificationChannel)

    }


}