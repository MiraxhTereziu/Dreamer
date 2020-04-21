package com.miraxh.dreamer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

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
