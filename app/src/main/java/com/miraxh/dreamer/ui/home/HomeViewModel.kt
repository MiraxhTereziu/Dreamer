package com.miraxh.dreamer.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.data.dream.DreamDatabase
import com.miraxh.dreamer.data.dream.DreamRepository
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val dreamRepository = DreamRepository(app)
    var dreamData = dreamRepository.dreamData

    private val toolbarHelper = ToolbarHelper()
    val daysData = toolbarHelper.days

    init {
        updateToolbar()
    }

    fun insertAllDrams(firebaseDreamList: MutableList<Dream>) {
        firebaseDreamList.forEach {
            dreamRepository.insertNewDream(it)
            dreamRepository.refreshData()
        }
    }

    fun updateToolbar() {
        toolbarHelper.getDaysData(dreamRepository.dreamData)
    }

    fun deleteAllDreams(){
        dreamRepository.deleteAll()
    }

    fun deleteDream(dreamID:String){
        dreamRepository.deleteDream(dreamID)
    }
}
