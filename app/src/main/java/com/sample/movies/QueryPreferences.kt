package com.sample.movies

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

private const val PREF_SEARCH_QUERY = "searchQuery"
private const val PREF_SEARCH_QUERY_YEAR = "searchQueryYear"
object QueryPreferences {
    fun getStoredQueryByYear(context: Context): Pair<String, String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val query = prefs.getString(PREF_SEARCH_QUERY, "")!!
        val year = prefs.getString(PREF_SEARCH_QUERY_YEAR, "")!!
        return Pair(query, year)
    }
    fun setStoredQueryByYear(context: Context, query: String, year:String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .putString(PREF_SEARCH_QUERY_YEAR, year)
            .apply()
    }

}
