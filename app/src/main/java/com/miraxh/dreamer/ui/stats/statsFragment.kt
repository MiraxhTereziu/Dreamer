package com.miraxh.dreamer.ui.stats

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.miraxh.dreamer.R

class statsFragment : Fragment() {

    companion object {
        fun newInstance() = statsFragment()
    }

    private lateinit var viewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stats_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
