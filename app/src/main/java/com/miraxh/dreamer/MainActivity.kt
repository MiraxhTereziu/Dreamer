package com.miraxh.dreamer

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miraxh.dreamer.data.DreamRepository
import com.miraxh.dreamer.ui.home.HomeViewModel
import com.miraxh.dreamer.ui.toolbar.ToolbarRecycleAdapter
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
        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(navigationListener)
    }
}
