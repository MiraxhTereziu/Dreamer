package com.miraxh.dreamer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miraxh.dreamer.models.Day
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recycleView: RecyclerView
    private lateinit var adapeter: RecycleAdapeter
    private lateinit var myParent: ConstraintLayout

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
        setSupportActionBar(findViewById(R.id.toolbar))

        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        initToolbar()
    }

    fun initToolbar(){
        var days = listOf<Day>(
            Day(1, "Tu", true),
            Day(2, "We", false),
            Day(3, "Th", true),
            Day(4, "Fr", false),
            Day(5, "Sa", true),
            Day(6, "Su", false),
            Day(7, "Mo", true),
            Day(8, "Tu", true),
            Day(9, "We", false),
            Day(10, "Th", true),
            Day(11, "Fr", false),
            Day(12, "Sa", true),
            Day(13, "Su", false),
            Day(14, "Mo", true)
        )

        /*myParent = findViewById(R.id.main)

        var layoutInflater = LayoutInflater.from(this)

        var view:View = layoutInflater.inflate(R.layout.tool_bar,null,false)

        myParent.addView(view)*/

        recycleView = findViewById(R.id.rec_view)
        adapeter = RecycleAdapeter(this, days)
        recycleView.adapter = adapeter
    }
}
