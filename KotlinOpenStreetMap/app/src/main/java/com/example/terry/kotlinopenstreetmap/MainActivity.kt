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
import android.support.v4.content.ContextCompat
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay








class MainActivity : AppCompatActivity() {

    private val map:MapView by lazy {
        map_l.setBuiltInZoomControls(true);
        map_l.setMultiTouchControls(true);
        map_l

    }

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
        map.setTileSource(TileSourceFactory.MAPNIK)
        var mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), map)
        mLocationOverlay.enableMyLocation()
        map.getOverlays().add(mLocationOverlay)
        val mapController = map.controller
        mapController.setZoom(9)
        val startPoint = GeoPoint(40.1106, -88.2073)
        mapController.setCenter(startPoint)
        fab.setOnClickListener { view ->
            moveTaskToBack(true)
        }
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
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


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
