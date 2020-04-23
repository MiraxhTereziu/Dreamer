package com.miraxh.dreamer.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.ui.toolbar.ToolbarRecycleAdapter
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), ToolbarRecycleAdapter.DayListener {

    private lateinit var daysRecycleView: RecyclerView
    private lateinit var dreamRecyclerView: RecyclerView
    private lateinit var adapterDay: ToolbarRecycleAdapter
    private lateinit var adapterDream: DreamListAdapter
    private lateinit var daysState: Parcelable
    private lateinit var viewModel: HomeViewModel

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        dreamListLoader(view)
        initToolbar(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun dreamListLoader(view: View){

        dreamRecyclerView = view.findViewById(R.id.dreamRecyclerview)
        viewModel.dreamData.observe(viewLifecycleOwner, Observer {
            adapterDream = DreamListAdapter(requireContext(),it)
            dreamRecyclerView.adapter = adapterDream
        })
    }

    private fun initToolbar(view: View){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        daysRecycleView = view.findViewById(R.id.daysRecyclerview)

        daysState = daysRecycleView.layoutManager?.onSaveInstanceState()!!

        viewModel.daysData.observe(viewLifecycleOwner, Observer {
            adapterDay = ToolbarRecycleAdapter(requireContext(), it, this)
            daysRecycleView.adapter = adapterDay
            restoreStateRV(daysState, daysRecycleView)
        })
    }

    override fun onDayItemListener(day: Day, position: Int) {
        daysState = saveStateRV(daysRecycleView)
        viewModel.changeState(day)
    }

    private fun saveStateRV(recyclerView: RecyclerView): Parcelable {
        return recyclerView.layoutManager?.onSaveInstanceState()!!
    }

    private fun restoreStateRV(state: Parcelable, recyclerView: RecyclerView) {
        recyclerView.layoutManager!!.onRestoreInstanceState(state)
    }
}
