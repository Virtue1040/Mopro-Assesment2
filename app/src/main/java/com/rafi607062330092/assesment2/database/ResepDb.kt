package com.rafi607062330092.assesment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafi607062330092.assesment2.model.Resep
import com.rafi607062330092.assesment2.util.Converters

@Database(entities = [Resep::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ResepDb : RoomDatabase() {
    abstract val dao: ResepDao

    companion object {
        @Volatile
        private var INSTANCE: ResepDb? = null

        fun getInstance(context: Context): ResepDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ResepDb::class.java,
                        "resep.db"
                    ).build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}