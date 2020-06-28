package com.raywenderlich.android.datadrop.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.raywenderlich.android.datadrop.app.Injection
import com.raywenderlich.android.datadrop.model.Drop

class DropsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Injection.provideDropRepository()
    private val allDrops = repository.getDrops()

    fun getDrops() = allDrops

    fun insert(drop: Drop, listener: DropInsertListener ) = repository.addDrop(drop, listener)

    fun clearAllDrops(listener: ClearAllDropsListener){
        repository.clearAllDrops(listener)
    }

    fun clearDrop(drop: Drop,listener: ClearDropsListener){
        repository.clearDrop(drop, listener)
    }
}