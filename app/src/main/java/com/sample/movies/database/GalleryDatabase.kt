package com.sample.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.movies.Item

@Database(entities = [Item::class], version = 1)
abstract class GalleryDatabase: RoomDatabase() {
    abstract fun galleryDao(): GalleryDao
}