package com.miraxh.dreamer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.DreamDatabase
import com.miraxh.dreamer.data.dream.DreamRepository
import com.miraxh.dreamer.ui.toolbar.ToolbarHelper

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val dreamRepository = DreamRepository(app)
    val dreamData = dreamRepository.dreamData

    private val toolbarHelper = ToolbarHelper()
    val daysData = toolbarHelper.days

    init {
        updateToolbar()
    }

    fun updateToolbar() {
        toolbarHelper.getDaysData(dreamRepository.dreamData)
    }
}
