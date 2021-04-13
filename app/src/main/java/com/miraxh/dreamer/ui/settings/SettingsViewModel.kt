package com.miraxh.dreamer.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.miraxh.dreamer.data.dream.DreamRepository

class SettingsViewModel(val app: Application) : AndroidViewModel(app) {
    private val dreamRepository = DreamRepository(app)

    fun delete(){
        dreamRepository.deleteAll()
        dreamRepository.refreshData()
    }

}
