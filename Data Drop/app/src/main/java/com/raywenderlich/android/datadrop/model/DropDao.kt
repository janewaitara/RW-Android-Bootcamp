package com.raywenderlich.android.datadrop.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DropDao {
    @Insert
    fun insert(drop: Drop)

    @Delete
    fun clearDrops(vararg drop: Drop) //used the vararg to indicate that any number of drops can be deleted

    @Query("SELECT * FROM drop_table ORDER BY dropMessage ASC")
    fun getAllDrops(): LiveData<List<Drop>>
}