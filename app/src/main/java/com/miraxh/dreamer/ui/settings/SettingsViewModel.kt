package com.miraxh.dreamer.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.miraxh.dreamer.data.dream.DreamRepository
import com.miraxh.dreamer.ui.add.AddViewModel

class SettingsViewModel(val app: Application) : AndroidViewModel(app) {
    private val dreamRepository = DreamRepository(app)

    fun delete(){
        dreamRepository.deleteAll()
        dreamRepository.refreshData()
    }

}
