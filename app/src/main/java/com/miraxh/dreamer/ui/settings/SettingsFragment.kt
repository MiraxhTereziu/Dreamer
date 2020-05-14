package com.miraxh.dreamer.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.miraxh.dreamer.R

class SettingsFragment : Fragment() {

    companion object {
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var deleteAllBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.settings_fragment, container, false)
        deleteAllBtn = view.findViewById(R.id.delete_all)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        deleteAllBtn.setOnClickListener {
            viewModel.delete()
        }
    }

}
