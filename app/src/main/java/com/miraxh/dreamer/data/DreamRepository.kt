package com.miraxh.dreamer.data

import android.app.Application
import android.util.Log
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
            val data:List<Dream>? = dreamDAO.getAll()
            if (data.isNullOrEmpty()) {
                //caso in cui il db sia vuoto
                insertDummyData()
                Log.i("REPO_TEST", "1")
            } else {
                //caso in cui il db abbia delle entry
                dreamData.postValue(data)
                withContext(Dispatchers.Main){
                    //do not delete
                }
            }
        }
    }

    fun refreshData(){
        CoroutineScope(Dispatchers.IO).launch {
            val data:List<Dream>? = dreamDAO.getAll()
            dreamData.postValue(data)
        }
    }

    suspend fun insertDummyData() {
        dreamDAO.insertDream(Dream(1, "11/11/11", "Test1", "Questo è un test"))
        dreamDAO.insertDream(Dream(2, "11/11/11", "Test2", "Questo è un secondo test"))
        dreamDAO.insertDream(Dream(3, "11/11/11", "Test3", "Questo è un terzo test"))
    }

}