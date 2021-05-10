package com.miraxh.dreamer.data.dream

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(dream: Dream)

    @Query("DELETE FROM dreams WHERE dreamID=:id")
    suspend fun deleteDream(id : Int)

    @Query("DELETE FROM dreams")
    suspend fun deleteAll()
}