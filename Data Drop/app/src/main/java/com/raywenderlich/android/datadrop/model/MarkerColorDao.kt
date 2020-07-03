package com.raywenderlich.android.datadrop.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarkerColorDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(markerColor: MarkerColor)

    @Query("DELETE FROM marker_color_table")
    fun deleteAll()

    @Query("SELECT * FROM marker_color_table ORDER BY displayString ASC")
    fun getAllMarkerColor(): LiveData<List<MarkerColor>>
}