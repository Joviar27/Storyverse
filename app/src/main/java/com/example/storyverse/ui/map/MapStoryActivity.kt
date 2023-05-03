package com.example.storyverse.ui.map

import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.storyverse.R
import com.example.storyverse.databinding.ActivityMapStoryBinding
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.ui.liststory.ListStoryViewModel
import com.example.storyverse.ui.liststory.ListStoryViewModelFactory
import com.example.storyverse.utils.InfoWindowAdapter
import com.example.storyverse.utils.ResultState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapStoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapStoryBinding

    private var _viewModel: ListStoryViewModel? = null
    private val viewModel get() = _viewModel

    private var _listStory : List<StoryEntity>? = null
    private val listStory get() = _listStory

    private val boundsBuilder = LatLngBounds.Builder()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        obtainViewModel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        getMyLocation()
        setMapStyle()
        getStoryList()
    }

    private fun getStoryList(){
        viewModel?.getStoryList(1)?.observe(this){ result ->
            when(result){
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error ->{
                    showLoading(false)
                    Toast.makeText(
                        this,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ResultState.Success ->{
                    showLoading(false)
                    _listStory = result.data
                    addManyMarker(listStory as List<StoryEntity>)
                }
            }
        }
    }

    private fun obtainViewModel() {
        val factory : ListStoryViewModelFactory = ListStoryViewModelFactory.getInstance(this)
        val viewModel : ListStoryViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    private fun getMyLocation() {
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            mMap.isMyLocationEnabled = true
        }
        else{
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private var added = 0.0

    private fun addManyMarker(listStory: List<StoryEntity>){
        val markerStoryMap = HashMap<Marker, String>()

        listStory.forEach{ location ->
            val latLng = LatLng(location.lat+added, location.lon+added)

            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(location.name)
                    .snippet(location.description)
            )
            boundsBuilder.include(latLng)

            markerStoryMap[marker as Marker] = location.photoUri
            added += 0.00001
        }

        mMap.setInfoWindowAdapter(InfoWindowAdapter(this@MapStoryActivity, markerStoryMap))

        val bounds : LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
        added = 0.0
    }

    private fun setMapStyle(){
        try{
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if(!success){
                Log.e(ContentValues.TAG, "Style parsing error")
            }
        } catch (e : Resources.NotFoundException){
            Log.e(ContentValues.TAG, "Can't find style, error : ${e.message.toString()}")
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}