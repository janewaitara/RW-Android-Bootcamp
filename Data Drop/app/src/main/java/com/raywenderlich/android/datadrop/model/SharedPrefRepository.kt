package com.raywenderlich.android.datadrop.model

import android.content.Context
import com.google.gson.Gson
import com.raywenderlich.android.datadrop.app.DataDropApplication

object SharedPrefRepository: DropRepository {

    //we are using gson to convert Drop data into a string format that can be stored in shared prefs
    private val gson = Gson()

    private const val SHARED_PREFS_REPOSITORY = "SHARED_PREFS_REPOSITORY"  //Custom string to hold the shared pref name

    //fun to return a named shared preferences by called getSharedPreferences on the App context
    private fun sharedPrefs() = DataDropApplication.getAppContext().getSharedPreferences(
            SHARED_PREFS_REPOSITORY, Context.MODE_PRIVATE //means custom shared pref will only be accessible on your app
    )
    //saving drops
    override fun addDrop(drop: Drop) {
        //we are using the drop id as the key
        sharedPrefs().edit().putString(drop.id,gson.toJson(drop)).apply()
    }

    override fun getDrops(): List<Drop> {
        //getting all the keys from our custom sharedPrefs and turn them into a list of drops
        return sharedPrefs().all.keys
                .map { sharedPrefs().getString(it, "")}
                .filterNot {it.isNullOrBlank()}
                .map { gson.fromJson(it, Drop::class.java)  }
    }

    override fun clearDrop(drop: Drop) {
        sharedPrefs().edit().remove(drop.id).apply()
    }

    override fun clearAllDrops() {
        sharedPrefs().edit().clear().apply()
    }
}