package com.raywenderlich.android.datadrop.model

import android.preference.PreferenceManager
import com.raywenderlich.android.datadrop.app.DataDropApplication
import com.raywenderlich.android.datadrop.ui.map.MapType

object MapPrefs {

    private const val KEY_MAKER_COLOR = "KEY_MAKER_COLOR"
    private const val KEY_MAP_TYPE = "KEY_MAP_TYPE"

    /**
     * A function that access the default sharedPrefs via Preference manager
     * */
    fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(DataDropApplication.getAppContext())

    fun saveMakerColor(markerColor: String){
        val editor = sharedPrefs().edit()
        editor.putString(KEY_MAKER_COLOR,markerColor).apply()
    }
    fun saveMapType(mapType: String){
     sharedPrefs().edit().putString(KEY_MAP_TYPE,mapType).apply()
    }

    fun getMarkerColor(): String = sharedPrefs().getString(KEY_MAKER_COLOR,MarkerColor.RED_COLOR)!!

    fun getMapType(): String = sharedPrefs().getString(KEY_MAP_TYPE, "Normal")!!
}