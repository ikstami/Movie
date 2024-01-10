package com.sample.movies

import com.google.gson.annotations.SerializedName

data class GalleryItem(
    var Title: String = "",
    var Year: String = "",
    var imdbID: String = "",
    var Type: String = "",
    @SerializedName("Poster") var Poster: String = ""
)