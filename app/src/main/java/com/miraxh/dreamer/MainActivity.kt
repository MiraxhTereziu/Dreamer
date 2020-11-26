package com.miraxh.dreamer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.miraxh.dreamer.util.MyLifeCycleObserver
import kotlinx.android.synthetic.main.activity_main_nav.*


class MainActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var handler: Handler

    private val navigationListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            drawer.closeDrawer(GravityCompat.START)
            handler.postDelayed(Runnable {
                when (item.itemId) {
                    R.id.diary_item -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.home_dest)
                    }
                    R.id.add_item -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.add_dest)
                    }
                    /*R.id.stats_item -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.stats_dest)
                    }
                    R.id.music_item -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.music_dest)
                    }
                    R.id.settings_item -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.settings_dest)
                    }*/
                }
            },0)
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inizializzazione main layout
        setContentView(R.layout.activity_main_nav)
        //inizializzazione hadler drawer
        handler = Handler()
        //inizializzazione drawer
        drawer = findViewById(R.id.drawer_layout)
        //inizializzazione della nav bar che gestisce la navigazione (non più implementata)
        nav_view.setNavigationItemSelectedListener(navigationListener)
        //inizializzazione classe di lifecycle per gestire gli stati dell'applicazione
        lifecycle.addObserver(MyLifeCycleObserver())

    }

    fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }


}


