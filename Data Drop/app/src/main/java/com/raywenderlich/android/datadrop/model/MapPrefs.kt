package com.raywenderlich.android.datadrop.model

import android.preference.PreferenceManager
import com.raywenderlich.android.datadrop.app.DataDropApplication

object MapPrefs {

    private const val KEY_MAKER_COLOR = "KEY_MAKER_COLOR"

    /**
     * A function that access the default sharedPrefs via Preference manager
     * */
    fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(DataDropApplication.getAppContext())

    fun saveMakerColor(markerColor: String){
        val editor = sharedPrefs().edit()
        editor.putString(KEY_MAKER_COLOR,markerColor).apply()
    }

    fun getMarkerColor(): String = sharedPrefs().getString(KEY_MAKER_COLOR,"Red")!!
}