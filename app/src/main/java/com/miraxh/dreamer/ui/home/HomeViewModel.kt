package com.miraxh.dreamer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.models.Day
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val dataList = ToolbarHelper()
    val daysData = dataList.days

    init {
        dataList.getDaysData()
    }

    fun changeState(day: Day){
        var tmpList = daysData.value
        tmpList?.forEach {
            if(day.day == it.day){
                it.active = it.active.not()
            }
        }
        daysData.value = tmpList
    }
}
