package com.admaja.storyappsubmission.view.maps

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.activity.viewModels
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.databinding.ActivityMapsBinding
import com.admaja.storyappsubmission.data.Result
import com.admaja.storyappsubmission.view.main.MainActivity
import com.admaja.storyappsubmission.view.main.MainViewModel
import com.admaja.storyappsubmission.view.main.MainViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//         Add a marker in Sydney and move the camera
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        addStoryMaker()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun addStoryMaker() {
        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val mapsViewModel: MainViewModel by viewModels {
            factory
        }
//        mapsViewModel.getStories().observe(this) {
//            if (it != null) {
//                when(it) {
//                    is Result.Loading -> {
//
//                    }
//                    is Result.Success -> {
//                        it.data.forEach { story ->
//                            val latLng = LatLng(story.lat, story.lon)
//                            mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
//                            boundBuilder.include(latLng)
//                        }
//                        val bounds: LatLngBounds = boundBuilder.build()
//                        mMap.animateCamera(
//                            CameraUpdateFactory.newLatLngBounds(
//                                bounds,
//                                resources.displayMetrics.widthPixels,
//                                resources.displayMetrics.heightPixels,
//                                300
//                            )
//                        )
//                    }
//                    is Result.Error -> {
//
//                    }
//                }
//            }
//        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        const val EXTRA_TOKEN = "token"
    }
}