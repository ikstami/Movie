package com.sample.movies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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

class MovieGalleryFragment : Fragment() {
    /**
     * Требуемый интерфейс
     */
    interface Callbacks {
        fun noItems()
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
        movieGalleryViewModel =
            ViewModelProviders.of(this).get(MovieGalleryViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_recycler, container, false)
        movieRecyclerView = view.findViewById(R.id.main_movie_recycler_view)
        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        movieRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieGalleryViewModel.itemLiveData.observe(
            viewLifecycleOwner,
            Observer { items ->
                items?.let {
                    if (items.isEmpty()) {
                        callbacks?.noItems()
                    }
                }
                movieRecyclerView.adapter = MovieAdapter(items)
            })

    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    private inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: Item
        private val titleTextView: TextView = itemView.findViewById(R.id.movie_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.year)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val solvedCheckBox: CheckBox = view.findViewById(R.id.checkSolve)
        private var isProgrammaticChange = false
        init {
            solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (!isProgrammaticChange) {
                    if (isChecked)
                        movieGalleryViewModel.updateItem(item, 1)
                    else
                        movieGalleryViewModel.updateItem(item, 0)
                }
            }
        }
        fun bind(item: Item) {
            this.item = item
            titleTextView.text = this.item.title
            dateTextView.text = this.item.year
            Picasso.get()
                .load(item.url)
                .placeholder(R.drawable.bill_up_close)
                .into(imageView)
            isProgrammaticChange = true
            if (this.item.del==1) {
                solvedCheckBox.apply {
                    isChecked = true
                    jumpDrawablesToCurrentState()
                }
            }
            isProgrammaticChange = false
        }
    }

    private inner class MovieAdapter(private val items: List<Item>) : RecyclerView.Adapter<MovieHolder>()  {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MovieHolder {
            val view = layoutInflater.inflate(
                R.layout.main_movies_watch,
                parent,
                false
            ) as View
            return MovieHolder(view)
        }
        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_delete_database_movies -> {
                movieGalleryViewModel.deleteMovie()
                Toast.makeText(
                    context,
                    R.string.delete_movies_from_database_success,
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance() = MovieGalleryFragment()
    }
}