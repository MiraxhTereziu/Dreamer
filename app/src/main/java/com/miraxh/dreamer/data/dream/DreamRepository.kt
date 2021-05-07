package com.miraxh.dreamer.data.dream

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.util.DbUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DreamRepository(val app: Application) {

    var dreamData = MutableLiveData<List<Dream>>()
    private val dreamDAO = DreamDatabase.getDatabase(app)
        .dreamDAO()
    private lateinit var auth: FirebaseAuth

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<Dream>? = dreamDAO.getAll()
            //dreamDAO.deleteAll()
            if (data.isNullOrEmpty()) {
                auth = FirebaseAuth.getInstance()
                var firebaseDreamList = DbUtil(auth, Firebase.firestore).getAllDreams()
                var localDreamList = mutableListOf<Dream>()
                firebaseDreamList.get()
                    .addOnSuccessListener { document ->
                        document.forEach {
                            localDreamList.add(
                                Dream(
                                    dreamID = 0,
                                    title = it.data["title"] as String,
                                    description = it.data["description"] as String,
                                    rate = (it.data["rate"] as String).toFloat(),
                                    date = it.data["date"] as String,
                                    audios = mutableListOf(),
                                    tags = mutableListOf(),
                                    images = mutableListOf()
                                )
                            )
                        }
                        dreamData.postValue(localDreamList)
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
            dreamDAO.deleteDream(dream.dreamID)
            dreamDAO.insertDream(dream)
        }
    }

    fun deleteDream(dream: Dream) {
        CoroutineScope(Dispatchers.IO).launch {
            dreamDAO.deleteDream(dream.dreamID)
        }
    }
}