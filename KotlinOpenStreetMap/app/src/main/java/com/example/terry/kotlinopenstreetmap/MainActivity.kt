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

import android.Manifest.permission

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import org.osmdroid.tileprovider.MapTileProviderBasic

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    //CURRENT LOCATION: URBANA,IL
    private val MAP_DEFAULT_LATITUDE = 40.1106
    private val MAP_DEFAULT_LONGITUDE = -88.2073

    private val map:MapView by lazy {
        map_l.setBuiltInZoomControls(true)
        map_l.setMultiTouchControls(true)
        map_l.setTileSource(TileSourceFactory.MAPNIK)
        map_l.setClickable(true)
        map_l

    }


    private var mLocationOverlay:MyLocationNewOverlay ?=null
    private var compassOverlay:CompassOverlay?=null
    private var mTilesOverlay:TilesOverlay?=null
    private var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=PackageManager.PERMISSION_DENIED
    private var MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=PackageManager.PERMISSION_DENIED
    private var MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=PackageManager.PERMISSION_DENIED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        //FOR API23 and HIGHER
        if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                     Array<String>(1){Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        else
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=PackageManager.PERMISSION_GRANTED
        if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    Array<String>(1){Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        else
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=PackageManager.PERMISSION_GRANTED
        if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    Array<String>(1){Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
        else
            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=PackageManager.PERMISSION_GRANTED
        if(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE==PackageManager.PERMISSION_GRANTED) {
            transferFiles(ctx, "map.mbtiles", "map.mbtiles")
        }

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Intialize MapView and set zoom and inital starting location
        val mapController = map.controller
        mapController.setZoom(14)
        val startPoint = GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE)
        mapController.setCenter(startPoint)
        //Set tiles to be provided by either files if exist or cache or server, from MAPNIK
        var mTiles= MapTileProviderBasic(ctx)
        mTiles.setTileSource(TileSourceFactory.MAPNIK)
        mTilesOverlay=TilesOverlay(mTiles,baseContext)
        mTilesOverlay?.setLoadingBackgroundColor(Color.TRANSPARENT)
        map.overlays.add(mTilesOverlay)
        //Show user location and go to location on first find
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), map)
        mLocationOverlay?.enableMyLocation()
        mLocationOverlay?.enableFollowLocation()
        mLocationOverlay?.runOnFirstFix(Runnable { mapController.animateTo(mLocationOverlay?.getMyLocation()) })
        map.overlays.add(mLocationOverlay)
        //Add a compass overlay
        compassOverlay = CompassOverlay(this, map)
        compassOverlay?.enableCompass()
        map.overlays.add(compassOverlay)



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
    //retrieves files from assest and puts them in the external storage
    companion object {


    fun transferFiles(ctx:Context, input:String,output:String) {
        val sdCard: File = Environment.getExternalStorageDirectory();
        var dir: File = File(sdCard.getAbsolutePath() + "/osmdroid/")
        dir.mkdirs()
        Configuration.getInstance().setOsmdroidBasePath(dir)
        var file: File = File("" + dir + "/" + output)
        if (!file.exists())
            file.createNewFile()
        var mapOutput: FileOutputStream = FileOutputStream(file)
        var mapInput = ctx.getAssets().open(input);
        val buffer = ByteArray(mapInput.available())
        mapInput.read(buffer)
        mapOutput.write(buffer)
    }
    }


}
