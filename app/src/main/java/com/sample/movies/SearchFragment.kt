package com.sample.movies

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.squareup.picasso.Picasso
import org.json.JSONObject.NULL
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {
    interface Callbacks {
        fun onSelected(galleryItem: GalleryItem)
    }

    private var callbacks: Callbacks? = null
    private lateinit var movieGalleryViewModel: MovieGalleryViewModel
    private lateinit var movieRecyclerView: RecyclerView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val title = arguments?.getString("addgalleryItemTitle")
        val year = arguments?.getString("addgalleryItemYear")
        movieGalleryViewModel =
            ViewModelProviders.of(this).get(MovieGalleryViewModel::class.java)
        movieGalleryViewModel.fetchMoviesByYear(title.toString(),year.toString())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.search_recycler, container, false)
        movieRecyclerView = view.findViewById(R.id.movie_recycler_view)
        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        movieRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                galleryItems?.let {
                    movieRecyclerView.adapter = MovieAdapter(galleryItems)
                }
            })

    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var galleryItem: GalleryItem
        private val titleTextView: TextView = itemView.findViewById(R.id.movie_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.year)
        private val genreTextView: TextView = itemView.findViewById(R.id.genre)
        val imageView: ImageView = itemView.findViewById(R.id.imagePosterView)
        init {
            view.setOnClickListener(this)
        }
        fun bind(galleryItem: GalleryItem) {
            this.galleryItem = galleryItem
            titleTextView.text = this.galleryItem.Title
            dateTextView.text = this.galleryItem.Year
            genreTextView.text = this.galleryItem.Type
        }
        override fun onClick(v: View) {
            Toast.makeText(
                context,
                "Выбран фильм:${galleryItem.Title}",
                Toast.LENGTH_SHORT
            ).show()
            callbacks?.onSelected(galleryItem)
        }
    }

    private inner class MovieAdapter(private val galleryItems: List<GalleryItem>) : RecyclerView.Adapter<MovieHolder>()  {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MovieHolder {
            val view = layoutInflater.inflate(
                R.layout.search_item,
                parent,
                false
            ) as View
            return MovieHolder(view)
        }
        override fun getItemCount(): Int = galleryItems.size
        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val galleryItem = galleryItems[position]
            Picasso.get()
                .load(galleryItem.Poster)
                .placeholder(R.drawable.bill_up_close)
                .into(holder.imageView)
            holder.bind(galleryItem)
        }
    }

    companion object {
        fun newInstance(title: String, year: String?): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putString("addgalleryItemTitle", title)
            args.putString("addgalleryItemYear", year)
            fragment.arguments = args
            return fragment
        }
    }
}