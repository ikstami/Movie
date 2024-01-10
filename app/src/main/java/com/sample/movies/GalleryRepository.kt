package com.sample.movies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.sample.movies.database.GalleryDatabase

import java.util.concurrent.Executors

private const val DATABASE_NAME = "gallery"
class GalleryRepository private constructor(context: Context) {
    private val database: GalleryDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            GalleryDatabase::class.java,
            DATABASE_NAME
        )
        .build()
    private val executor = Executors.newSingleThreadExecutor()
    fun getMovies(): LiveData<List<Item>> = database.galleryDao().getmovies()

    fun addMovie(movie: GalleryItem) {
        var item = Item()
        item.id = movie.imdbID
        item.title = movie.Title
        item.year = movie.Year
        item.url = movie.Poster
        executor.execute {
            if (database.galleryDao().getmovie(item.id)!=item){
                database.galleryDao().addmovie(item)
            }
        }
    }
    fun updateItem(item: Item){
        executor.execute {
            database.galleryDao().updateItem(item)
        }
    }

    fun deleteMovie() {
        executor.execute {
            database.galleryDao().deletemovie()
        }
    }

    companion object {
        private var INSTANCE: GalleryRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GalleryRepository(context)
            }
        }
        fun get(): GalleryRepository {
            return INSTANCE ?:
            throw
            IllegalStateException("Repository must be initialized")
        }
    }

}