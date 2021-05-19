package com.pklos.payshop.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SeenItems::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun seenItemsDao(): SeenItemsDao
}