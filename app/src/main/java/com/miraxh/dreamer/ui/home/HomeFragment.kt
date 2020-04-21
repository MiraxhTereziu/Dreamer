package com.miraxh.dreamer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.models.Day
import com.miraxh.dreamer.ui.toolbar.ToolbarRecycleAdapeter
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment(), ToolbarRecycleAdapeter.DayListener  {

    private lateinit var recycleView: RecyclerView
    private lateinit var adapeterToolbar: ToolbarRecycleAdapeter


    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        var days =  mutableListOf<Day>()
        days = ToolbarHelper.getPastTime(days)
        recycleView = view.findViewById(R.id.recyclerview)
        adapeterToolbar = ToolbarRecycleAdapeter(requireContext(),days)
        recycleView.adapter = adapeterToolbar

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onDayItemListener(day: Day) {

    }
}
