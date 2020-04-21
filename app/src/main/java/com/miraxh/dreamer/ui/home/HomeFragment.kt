package com.miraxh.dreamer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.models.Day
import com.miraxh.dreamer.ui.toolbar.ToolbarRecycleAdapeter
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

        recycleView = view.findViewById(R.id.recyclerview)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.daysData.observe(viewLifecycleOwner, Observer {
            val adapeter = ToolbarRecycleAdapeter(requireContext(),it)
            recycleView.adapter = adapeter
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDayItemListener(day: Day) {

    }
}
