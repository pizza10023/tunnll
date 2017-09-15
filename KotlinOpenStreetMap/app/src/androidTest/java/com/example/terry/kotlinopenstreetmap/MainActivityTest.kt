package com.example.terry.kotlinopenstreetmap

import android.content.Context
import android.os.Environment
import com.example.terry.kotlinopenstreetmap.MainActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import java.io.FileOutputStream
import java.io.InputStream
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.view.menu.ActionMenuItemView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import java.io.File


/**
 * Created by Terry on 9/14/2017.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    private val MAP_DEFAULT_LATITUDE = 40.1106
    private val MAP_DEFAULT_LONGITUDE = -88.2073
    @get:Rule
    public var rule :ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun onCreate() {
        val activity = rule.activity
        val map:MapView=activity.map_l
        assertNotNull(map)
        val toolbar:View=activity.toolbar
        assertNotNull(toolbar)
        var diff=0.04
        assert(map.canZoomIn())
        assert(map.canZoomOut())
        assertEquals(TileSourceFactory.MAPNIK,map.tileProvider.tileSource)
        assert(map.overlayManager.tilesOverlay.useDataConnection())
    }

    @Test
    fun onResume() {
        val activity = rule.activity
        val map:MapView=activity.map_l
        assertNotNull(map)

    }

    @Test
    fun onPause() {

    }

    @Test
    fun onCreateOptionsMenu() {
        assertNotNull(R.menu.menu_main)
        assertNotNull(R.id.action_exit)
        val activity = rule.activity
        val action:ActionMenuItemView = activity.findViewById(R.id.action_exit) as ActionMenuItemView
        assert(action.hasText())
    }

    @Test
    fun onOptionsItemSelected() {

    }

    @Test
    fun transferFiles() {
        val sdCard: File = Environment.getExternalStorageDirectory();
        var dir: File = File(sdCard.getAbsolutePath() + "/osmdroid/")
        assert(dir.isDirectory)
        assert(dir.exists())



    }

}