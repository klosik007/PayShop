package com.pklos.payshop.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seen_items")
data class SeenItems(
    @PrimaryKey val id: Int,
    @ColumnInfo(name="item_id") val itemId: Int?
)
