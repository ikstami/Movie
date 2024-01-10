package com.sample.movies

import com.google.gson.annotations.SerializedName

class MovieResponse {
    @SerializedName("r")
    lateinit var galleryItems: List<GalleryItem>
}