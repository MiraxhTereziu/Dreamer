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

    private val dreamRepository = DreamRepository(app)
    val dreamData = dreamRepository.dreamData

    private val toolbarHelper = ToolbarHelper()
    val daysData = toolbarHelper.days

    companion object{
        var firstStart = true
    }

    init {
        if(firstStart){
            updateToolbar()
            firstStart = false
        }
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

    fun updateToolbar(){
        toolbarHelper.getDaysData(dreamRepository.dreamData)
    }
}
