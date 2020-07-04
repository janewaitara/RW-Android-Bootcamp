package com.raywenderlich.android.datadrop.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.raywenderlich.android.datadrop.app.DataDropApplication

class MarkerColorViewModel(application: Application): AndroidViewModel(application) {
    //dao property
    private val markerColorDao = DataDropApplication.database.markerColorDao()
    //liveData property
    private val allMarkerColors = markerColorDao.getAllMarkerColor()

    //getter for all markerColors
    fun getMarkerColors() = allMarkerColors
}
