package com.miraxh.dreamer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miraxh.dreamer.util.MyLifeCycleObserver
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity(){

    private val navigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_item -> {
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
        setContentView(R.layout.activity_main)
        //inizializzazione della nav bar che gestisce la navigazione
        nav_view.setOnNavigationItemSelectedListener(navigationListener)
        //inizializzazione classe di lifecycle per gestire gli stati dell'applicazione
        lifecycle.addObserver(MyLifeCycleObserver())
    }
}
