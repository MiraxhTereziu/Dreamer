package com.miraxh.dreamer.ui.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.data.dream.DreamDatabase
import com.miraxh.dreamer.data.dream.DreamRepository
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper

class AddViewModel(val app: Application) : AndroidViewModel(app){

    private val dreamRepository = DreamRepository(app)

    fun newDream(dream: Dream){
        dreamRepository.insertNewDream(dream)
        dreamRepository.refreshData()
    }

}
