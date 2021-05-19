package com.pklos.payshop.db

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class SeenItems(
    @PrimaryKey val id: Int,
    @ColumnInfo(name="item_id") val itemId: Int?
)
