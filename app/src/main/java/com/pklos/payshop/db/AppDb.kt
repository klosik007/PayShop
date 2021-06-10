package com.pklos.payshop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SeenItems::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun seenItemsDao(): SeenItemsDao

    companion object{
        @Volatile private var instance: AppDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDb(context).also{instance = it}
        }

        private fun buildDb(context: Context) = Room.databaseBuilder(context,
                                                AppDb::class.java, "seen-items.db")
                                                .build()
    }
}