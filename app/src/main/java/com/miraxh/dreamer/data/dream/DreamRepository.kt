package com.miraxh.dreamer.data.dream

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DreamRepository(val app: Application) {

    var dreamData = MutableLiveData<List<Dream>>()
    private val dreamDAO = DreamDatabase.getDatabase(app)
        .dreamDAO()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<Dream>? = dreamDAO.getAll()
            //dreamDAO.deleteAll()
            if (data.isNullOrEmpty()) {
                //caso in cui il db sia vuoto
                //insertDummyData()
            } else {
                //caso in cui il db abbia delle entry
                dreamData.postValue(data)
                withContext(Dispatchers.Main) {
                    //do not delete
                }
            }
        }
    }

    fun deleteAll(){
        CoroutineScope(Dispatchers.IO).launch {
            dreamDAO.deleteAll()
        }
    }

    fun refreshData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<Dream>? = dreamDAO.getAll()
            dreamData.postValue(data)
        }
    }

    fun insertNewDream(dream: Dream) {
        CoroutineScope(Dispatchers.IO).launch {
            dreamDAO.insertDream(dream)
        }
    }
}