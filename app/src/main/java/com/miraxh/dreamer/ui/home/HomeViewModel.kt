package com.miraxh.dreamer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.DreamDatabase
import com.miraxh.dreamer.data.dream.DreamRepository
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val dreamDAO = DreamDatabase.getDatabase(app)
        .dreamDAO()

    val dreamRepository = DreamRepository(app)
    val dreamData = dreamRepository.dreamData


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
