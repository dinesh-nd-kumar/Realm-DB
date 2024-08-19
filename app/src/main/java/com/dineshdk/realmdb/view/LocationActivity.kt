package com.dineshdk.realmdb.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

open class LocationActivity : AppCompatActivity() {

    private val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissionResults ->
            permissionResults.forEach{
                if (it.key == Manifest.permission.ACCESS_FINE_LOCATION){
                    if (it.value ){
                        checkSettingsAndStartLocationUpdate()
                    }else{
                        askLocationPermission()
                    }
                }
            }
        }
    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            checkSettingsAndStartLocationUpdate()
        } else
            askLocationPermission()
    }
    private fun checkSettingsAndStartLocationUpdate(){
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isLocationEnabled) {

            AlertDialog.Builder(this)
                .setTitle("Enable Location Services")
                .setMessage("Location services are required for this app. Please enable them in the settings.")
                .setCancelable(false)
                .setPositiveButton("Settings") { dialog, _ ->

                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    dialog.dismiss()

                }
                .setNegativeButton("Cancel"){ dialog, _ ->
                    checkSettingsAndStartLocationUpdate()
                    dialog.dismiss()

                }
                .show()
        }

    }
    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show()
            }
            requestPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))



        }

    }

}