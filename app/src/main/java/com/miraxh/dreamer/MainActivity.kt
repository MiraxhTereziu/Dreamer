package com.miraxh.dreamer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.miraxh.dreamer.util.MyLifeCycleObserver
import kotlinx.android.synthetic.main.activity_main_nav.*


class MainActivity() : AppCompatActivity() {


    //vecchia bottom nav bar
    private val navigationListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.diary_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.home_dest)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.stats_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.stats_dest)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.add_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.add_dest)
                    //val menu: Menu = nav_view.menu
                    //menu.findItem(R.id.add_item).setIcon(R.drawable.ic_add_enable)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.music_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.music_dest)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settings_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.settings_dest)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inizializzazione main layout

        setContentView(R.layout.activity_main_nav)
        //inizializzazione della nav bar che gestisce la navigazione (non più implementata)
        nav_view.setNavigationItemSelectedListener(navigationListener)
        //inizializzazione classe di lifecycle per gestire gli stati dell'applicazione
        lifecycle.addObserver(MyLifeCycleObserver())

        //how to make status bar full trasperent
        /*val w: Window = window // in Activity's onCreate() for instance
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )*/
    }

    @SuppressLint("WrongConstant")
    fun openDrawer() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer?.openDrawer(Gravity.START)
    }
}


