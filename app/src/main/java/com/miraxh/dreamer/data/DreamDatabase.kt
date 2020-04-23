package com.miraxh.dreamer.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Dream::class], version = 1, exportSchema = false)
abstract class DreamDatabase : RoomDatabase() {

    abstract fun dreamDAO(): DreamDAO

    companion object {
        @Volatile
        private var INSTANCE: DreamDatabase? = null

        fun getDatabase(context: Context): DreamDatabase {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DreamDatabase::class.java,
                        "dream.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}