package com.miraxh.dreamer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val dataList = ToolbarHelper()
    val daysData = dataList.days

    init {
        dataList.getDaysData()
    }
}
