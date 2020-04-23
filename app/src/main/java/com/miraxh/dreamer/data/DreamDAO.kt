package com.miraxh.dreamer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DreamDAO {

    @Query("SELECT * FROM dreams")
    fun getAll(): List<Dream>

    @Insert
    suspend fun insertDream(dream: Dream)

    @Insert
    suspend fun insertDreams(dreams: List<Dream>)

    @Query("DELETE FROM dreams")
    suspend fun deleteAll()

}