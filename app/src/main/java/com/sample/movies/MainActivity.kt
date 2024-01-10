package com.sample.movies

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), MovieGalleryFragment.Callbacks  {
    private lateinit var movieGalleryViewModel: MovieGalleryViewModel
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val faddist = findViewById<FloatingActionButton>(R.id.add)
        movieGalleryViewModel = ViewModelProviders.of(this).get(MovieGalleryViewModel::class.java)
        val isFragmentContainerEmpty =
            savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainerMain, MovieGalleryFragment.newInstance())
                .commit()
        }
        faddist.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("fragmentToOpen", "firstFragment")
            startActivity(intent)
        }
    }
    override fun noItems()
    {
        val fragment = MainNotFoundFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newIntent(context: Context): Intent
        {
            return Intent(context, MainActivity::class.java)
        }
    }
}