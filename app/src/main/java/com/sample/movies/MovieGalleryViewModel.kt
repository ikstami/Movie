package com.sample.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.sample.movies.api.OmbdFetchr
import org.json.JSONObject.NULL

class MovieGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    private val galleryRepository = GalleryRepository.get()
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    var itemLiveData: LiveData<List<Item>> = galleryRepository.getMovies()

    private val omdbFetchr = OmbdFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()
    private val mutableSearchTerms = MutableLiveData<Pair<String, String>>()

    init {
        mutableSearchTerms.value = QueryPreferences.getStoredQueryByYear(app)
        galleryItemLiveData = mutableSearchTerms.switchMap { (searchTerm1, searchTerm2) ->
            omdbFetchr.searchMoviesByYear(searchTerm1, searchTerm2)
        }
    }

    fun fetchMoviesByYear(query: String = "", year: String = "") {
        QueryPreferences.setStoredQueryByYear(app, query, year)
        mutableSearchTerms.value = Pair(query, year)
    }
    fun addMovie(movie: GalleryItem) {
        galleryRepository.addMovie(movie)
    }
    fun deleteMovie() {
        galleryRepository.deleteMovie()
    }
    fun updateItem(item: Item, del:Int){
        item.del = del
        galleryRepository.updateItem(item)
    }
}