package com.miraxh.dreamer.data.dream

import android.content.Context
import androidx.room.*

@Database(entities = [Dream::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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