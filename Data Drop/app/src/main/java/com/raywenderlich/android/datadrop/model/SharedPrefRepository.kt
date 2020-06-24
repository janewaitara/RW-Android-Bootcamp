package com.raywenderlich.android.datadrop.model

import android.content.Context
import com.raywenderlich.android.datadrop.app.DataDropApplication

object SharedPrefRepository: DropRepository {

    private const val SHARED_PREFS_REPOSITORY = "SHARED_PREFS_REPOSITORY"  //Custom string to hold the shared pref name

    //fun to return a named shared preferences by called getSharedPreferences on the App context
    fun sharedPrefs() = DataDropApplication.getAppContext().getSharedPreferences(
            SHARED_PREFS_REPOSITORY, Context.MODE_PRIVATE //means custom shared pref will only be accessible on your app
    )
    override fun addDrop(drop: Drop) {
        TODO("Not yet implemented")
    }

    override fun getDrops(): List<Drop> {
        return emptyList()
    }

    override fun clearDrop(drop: Drop) {
        TODO("Not yet implemented")
    }

    override fun clearAllDrops() {
        TODO("Not yet implemented")
    }
}