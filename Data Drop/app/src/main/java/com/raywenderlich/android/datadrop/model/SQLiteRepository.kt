package com.raywenderlich.android.datadrop.model

import com.raywenderlich.android.datadrop.app.DataDropApplication

class SQLiteRepository : DropRepository {

    private val database = DropDbHelper(DataDropApplication.getAppContext()).writableDatabase

    override fun addDrop(drop: Drop) {

    }

    override fun getDrops(): List<Drop> {
       return emptyList()
    }

    override fun clearDrop(drop: Drop) {

    }

    override fun clearAllDrops() {

    }
}