package com.pklos.payshop.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SeenItemsDao {
    @Query("SELECT * FROM seen_items")
    fun getAllSeenItems() : List<SeenItems>

    @Insert
    fun insertSeenItem(vararg seenItems: SeenItems)

    @Delete
    fun delete(seenItem: SeenItems)
}