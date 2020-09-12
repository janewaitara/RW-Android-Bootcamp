package com.raywenderlich.android.creaturemon.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureRepository
import com.raywenderlich.android.creaturemon.model.room.RoomRepository

class AllCreaturesViewModel(private  val repository : CreatureRepository = RoomRepository()): ViewModel() {


    /**Will be used to send generated creatures to the view layer*/
    private val allCreaturesLiveData = repository.getAllCreatures()

    fun getAllCreaturesLiveData(): LiveData<List<Creature>> = allCreaturesLiveData

    fun clearAllCreatures() = repository.clearAllCreatures()
}