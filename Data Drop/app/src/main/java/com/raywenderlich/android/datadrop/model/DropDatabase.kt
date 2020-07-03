package com.raywenderlich.android.datadrop.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(Drop::class), (MarkerColor::class)],version = 1)
@TypeConverters(LatLngConverter::class)
abstract class DropDatabase: RoomDatabase() {

    abstract fun dropDao(): DropDao

    abstract fun markerColorDao(): MarkerColorDao

}