package com.raywenderlich.android.creaturemon.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.raywenderlich.android.creaturemon.model.*
import java.text.FieldPosition

class CreatureViewModel(private val generator: CreatureGenerator = CreatureGenerator()): ViewModel() {

    /**Will be used to send generated creatures to the view layer*/
    private val creatureLiveData = MutableLiveData<Creature>()

    fun getCreatureLiveData(): LiveData<Creature> = creatureLiveData

    /**
     * Properties for the generated creature*/
    var name = ""
    var intelligence = 0
    var strength = 0
    var endurance = 0
    var drawable = 0

    lateinit var creature: Creature

    fun updateCreature(){
        val attributes = CreatureAttributes(intelligence, strength, endurance)
        creature = generator.generateCreature(attributes, name, drawable)
        creatureLiveData.postValue(creature)
    }
    /**
    * Called when a user selects a value in the creature attribute dropDowns*/
    fun attributesSelected(attributeType: AttributeType, position: Int){
        when(attributeType){
            AttributeType.INTELLIGENCE ->
                intelligence = AttributeStore.INTELLIGENCE[position].value

            AttributeType.STRENGTH ->
                strength = AttributeStore.STRENGTH[position].value

            AttributeType.ENDURANCE ->
                endurance = AttributeStore.ENDURANCE[position].value
        }

        /**
         * Passes the new creature to the view along with the selected attributes in the live data
         * */
        updateCreature()
    }

    /**
     * Called when a user selects an avatar in the creature attribute */
    fun drawableSelected(drawable: Int){
        this.drawable = drawable
        updateCreature()
    }

}