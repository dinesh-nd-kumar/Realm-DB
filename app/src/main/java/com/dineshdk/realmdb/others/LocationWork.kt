package com.dineshdk.realmdb.others

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dineshdk.realmdb.Model.UserLocation
import com.dineshdk.realmdb.repo.CurrentUser
import com.google.android.gms.location.LocationServices
import io.realm.Realm

class LocationWork(var context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val TAG = "LocationWork"
    private lateinit var realm: Realm
    private lateinit var cUser : CurrentUser

    override fun doWork(): Result {



        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show()
            }

            return Result.retry()
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    realm = Realm.getDefaultInstance()
                    cUser = CurrentUser(context)
                    realm.executeTransaction {

                        val l = UserLocation(latitude,longitude,cUser.getId(),System.currentTimeMillis())
                        it.insert(l)
                        Log.d(TAG, "doWork: onSuccess latitude $latitude longitude $longitude")


                        }


                }
            }
            .addOnFailureListener {
                //
            }





        Log.d(TAG, "doWork:  location isDone ")
        return Result.success()


    }
}