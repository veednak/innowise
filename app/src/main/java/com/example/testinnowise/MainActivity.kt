package com.example.testinnowise

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.testinnowise.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


var lat: Double = 55.5
var lon: Double = 27.0
private const val REQUEST_PERMISSION_LOCATION = 255

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        navView.setupWithNavController(navController)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSION_LOCATION
        )
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            print("Разрешение  не дано")
        } else {
            print("Разрешение дано")
        }

    }

    override fun onResume() {
        super.onResume()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            lat =
                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!.latitude
            lon =
                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!.longitude
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0F,
                locationListener
            )
        } catch (ex: SecurityException) {
            Log.d("myTag", ex.toString())
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lat = location.latitude
            lon = location.longitude
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We now have permission to use the location
            }
        }
    }
}