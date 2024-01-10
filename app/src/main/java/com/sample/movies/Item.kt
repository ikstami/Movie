package com.sample.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery")

data class Item(
    var title: String = "",
    var year: String = "",
    var url: String = "",
    @PrimaryKey var id: String = "",
    var del: Int = 0
)