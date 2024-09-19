package com.example.weatherapp

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resolutionForResult: ActivityResultLauncher<IntentSenderRequest>
    private var cityNameState by mutableStateOf(" ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    checkLocationSettingsAndGetLocation()

                } else {
                    Log.d("MainActivity", "Location permission not granted")
                    cityNameState = "Permission Denied"
                }
            }
        resolutionForResult =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    getCurrentLocation()
                } else {
                    Log.d("MainActivity", "Location settings resolution denied")
                    cityNameState = "Location Settings Denied"
                }
            }

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {

                MainScreen(weatherViewModel,cityNameState)
                LaunchedEffect(Unit) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                LaunchedEffect(cityNameState) {
                    if (cityNameState.isNotEmpty()) {
                        // Fetch city data using the viewModel
                        Log.d("CityName", cityNameState)
                       weatherViewModel.getData(cityNameState)
                    }
                }


            }
        }
    }

     // writing the functions to get the location of user.

    private fun checkLocationSettingsAndGetLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 2000
            numUpdates =5
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            getCurrentLocation()
        }

        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(e.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(
                        "MainActivity",
                        "Error getting location settings resolution: ${sendEx.message}"
                    )
                }
            } else {
                Log.e("MainActivity", "Error checking location settings: ${e.message}")
                cityNameState = "Location Settings Error"
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        cityNameState = "fetching location.."

        lifecycleScope.launch {
            try {
                val location = fusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    null
                ).await()

                if (location != null) {
                    Log.d(
                        "MainActivity",
                        "Location received: ${location.latitude}, ${location.longitude}"
                    )
                    processLocation(location)
                } else {
                    Log.e("MainActivity", "getCurrentLocation returned null")
                    cityNameState = "Location not available"
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error getting current location: ${e.message}")
                cityNameState = "Error: ${e.message}"
            }
        }
    }

    private fun processLocation(location: android.location.Location) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses?.firstOrNull()?.locality
                launch(Dispatchers.Main) {
                    cityNameState = cityName ?: "City not found"
                }
                Log.d("MainActivity", "Location: ${location.latitude}, ${location.longitude}")
                Log.d("MainActivity", "City: $cityName")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error getting location: ${e.message}")
                launch(Dispatchers.Main) {
                    cityNameState = "Error: ${e.message}"
                }
            }
        }
    }
}




