package com.example.terry.kotlinopenstreetmap

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.activity_main.*
import android.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.api.IMapController
import android.Manifest.permission
import android.Manifest.permission.WRITE_CALENDAR
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay

class MainActivity : AppCompatActivity() {

    private val MAP_DEFAULT_LATITUDE = 44.445883
    private val MAP_DEFAULT_LONGITUDE = 26.040963

    private val map:MapView by lazy {
        map_l.setBuiltInZoomControls(true)
        map_l.setMultiTouchControls(true)
        map_l.setTileSource(TileSourceFactory.MAPNIK)
        map_l.setClickable(true)
        map_l

    }


    private var mItemizedOverlay: ItemizedOverlay<OverlayItem>?=null
    private var lastPosition: OverlayItem? = null
    private var mLocationOverlay:MyLocationNewOverlay ?=null
    private var compassOverlay:CompassOverlay?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))


        setContentView(R.layout.activity_main)
        val writeCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val fineLocationCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        setSupportActionBar(toolbar)
        val mapController = map.controller
        mapController.setZoom(14)
        val startPoint = GeoPoint(40.1106, -88.2073)
        mapController.setCenter(startPoint)
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), map)
        mLocationOverlay?.enableMyLocation()
        mLocationOverlay?.enableFollowLocation()
        mLocationOverlay?.runOnFirstFix(Runnable { mapController.animateTo(mLocationOverlay?.getMyLocation()) })
        map.getOverlays().add(mLocationOverlay)
        compassOverlay = CompassOverlay(this, map)
        compassOverlay?.enableCompass()
        map.getOverlays().add(compassOverlay)



        val locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var location:Location?=null


    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mLocationOverlay?.enableMyLocation()
        mLocationOverlay?.enableFollowLocation()

    }

    override fun onPause() {
        super.onPause()
        mLocationOverlay?.disableMyLocation()
        mLocationOverlay?.disableFollowLocation()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_exit) {
            moveTaskToBack(true)
            true
        } else super.onOptionsItemSelected(item)

    }

}
