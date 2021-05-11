package com.miraxh.dreamer.data.dream

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.util.DbUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors.toList

class DreamRepository(val app: Application) {

    var dreamData = MutableLiveData<List<Dream>>()
    private val dreamDAO = DreamDatabase.getDatabase(app)
        .dreamDAO()
    private lateinit var auth: FirebaseAuth

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<Dream> = dreamDAO.getAll()
            //dreamDAO.deleteAll()
            if (data.isNullOrEmpty()) {
                auth = FirebaseAuth.getInstance()
                val firebaseDreamList = DbUtil(auth, Firebase.firestore).getAllDreams()
                val localDreamList = mutableListOf<Dream>()
                firebaseDreamList.get()
                    .addOnSuccessListener { document ->
                        document.forEach {
                            localDreamList.add(
                                Dream(
                                    dreamID = it.data["dreamID"] as String,
                                    title = it.data["title"] as String,
                                    description = it.data["description"] as String,
                                    rate = (it.data["rate"] as Double).toFloat(),
                                    date = it.data["date"] as String,
                                    tags = (it.data["tags"] as ArrayList<String>).toMutableList(),
                                    images = (it.data["images"] as ArrayList<String>).toMutableList(),
                                    audios = (it.data["audios"] as ArrayList<String>).toMutableList()
                                )
                            )
                        }
                        dreamData.postValue(localDreamList)
                        insertAllDreams(localDreamList)
                    }
            } else {
                //caso in cui il db abbia delle entry
                dreamData.postValue(data)
                withContext(Dispatchers.Main) {
                    //do not delete
                }
            }
        }
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            dreamDAO.deleteAll()
        }
    }

    fun refreshData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<Dream> = dreamDAO.getAll()
            dreamData.postValue(data)
        }
    }

    fun insertNewDream(dream: Dream) {
        CoroutineScope(Dispatchers.IO).launch {
            dreamDAO.deleteDream(dream.dreamID)
            dreamDAO.insertDream(dream)
            refreshData()
        }
    }

    fun insertAllDreams(dreamList: MutableList<Dream>) {
        dreamList.forEach {
            insertNewDream(it)
        }
    }

    fun deleteDream(dreamID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dreamDAO.deleteDream(dreamID)
            refreshData()
        }
    }
}