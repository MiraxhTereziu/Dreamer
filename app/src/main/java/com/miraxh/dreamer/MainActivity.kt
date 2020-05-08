package com.miraxh.dreamer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.miraxh.dreamer.util.MyLifeCycleObserver
import kotlinx.android.synthetic.main.activity_main_nav.*


class MainActivity() : AppCompatActivity() {


    lateinit var drawer : DrawerLayout

    private val navigationListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            drawer.closeDrawer(GravityCompat.START)
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
        //inizializzazione drawer
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        //inizializzazione della nav bar che gestisce la navigazione (non più implementata)
        nav_view.setNavigationItemSelectedListener(navigationListener)
        //inizializzazione classe di lifecycle per gestire gli stati dell'applicazione
        lifecycle.addObserver(MyLifeCycleObserver())
    }

    fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    fun closeKeyboard(){
        val view = this.currentFocus
        if(view != null){
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken,0)
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}


