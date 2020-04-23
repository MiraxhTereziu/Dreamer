package com.miraxh.dreamer.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

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
                insertDummyData()
                Log.i("REPO_TEST", "1")
            } else {
                //caso in cui il db abbia delle entry
                dreamData.postValue(data)
                withContext(Dispatchers.Main) {
                    //do not delete
                }
            }
        }
    }

    fun refreshData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<Dream>? = dreamDAO.getAll()
            dreamData.postValue(data)
        }
    }

    suspend fun insertDummyData() {
        val calendar = Calendar.getInstance()
        val dayNumberFormatter = SimpleDateFormat("dd")
        val monthFormatter = SimpleDateFormat("MMMM")
        val dayNumber = dayNumberFormatter.format(calendar.time)
        val monthName = monthFormatter.format(calendar.time)

        val time = "${dayNumber}° $monthName"

        val description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s."

        dreamDAO.insertDream(Dream(1, time, "Special dinner", description))
        dreamDAO.insertDream(Dream(2, time, "Storm in the summer", description))
        dreamDAO.insertDream(Dream(3, time, "Star wars", description))
        dreamDAO.insertDream(Dream(4, time, "Snow in august", description))
        dreamDAO.insertDream(Dream(5, time, "Stop this flame", description))
        dreamDAO.insertDream(Dream(6, time, "Run", description))
        dreamDAO.insertDream(Dream(7, time, "Good news", description))
        dreamDAO.insertDream(Dream(8, time, "King Kuta", description))
    }
}