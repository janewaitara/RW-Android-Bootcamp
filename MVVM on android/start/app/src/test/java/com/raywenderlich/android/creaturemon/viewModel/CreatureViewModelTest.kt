package com.raywenderlich.android.creaturemon.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureAttributes
import com.raywenderlich.android.creaturemon.model.CreatureGenerator
import com.raywenderlich.android.creaturemon.model.CreatureRepository
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CreatureViewModelTest{
    //property of the viewModel being tested
    private lateinit var creatureViewModel: CreatureViewModel
    
    //test rule to swap the background executor normally used with asynchronous thread executor
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    //property for the mock creatureGenerator for the viewModel
    @Mock
    lateinit var mockGenerator: CreatureGenerator

    //property for the mock roomRepository for the viewModel
    @Mock
    lateinit var repository: CreatureRepository


    @Before
    fun setup(){
        // init the mocks and setup the creature viewModel to be tested 
        MockitoAnnotations.initMocks(this)
        creatureViewModel = CreatureViewModel(mockGenerator, repository)
    }
    //test to setup the creature being generated in the viewModel
    @Test
    fun testSetupCreature() {
        //arrange the expected value as a stubCreature
        val attributes = CreatureAttributes(10,3,7)
        val stubCreature = Creature(attributes,87,"Test Creature")

        //use when to return stub creature
        `when`(mockGenerator.generateCreature(attributes)).thenReturn(stubCreature)

        creatureViewModel.intelligence = 10
        creatureViewModel.strength = 3
        creatureViewModel.endurance = 7

        //perform the action being tested
        creatureViewModel.updateCreature()

        //assert whether the viewModel creature is the stubCreature we are expecting
        assertEquals( stubCreature, creatureViewModel.creature)

    }

    //test to save creature with blank attributes - names can't be saved
    @Test
    fun testCantSaveCreatureWithBlankName(){
        creatureViewModel.intelligence = 10
        creatureViewModel.strength = 3
        creatureViewModel.endurance = 7
        creatureViewModel.drawable = 1
        creatureViewModel.name = ""

        val canSaveCreature = creatureViewModel.canSaveCreature()
        assertEquals(false,canSaveCreature)
    }

    //test to save creature with blank attributes - strength can't be saved
    @Test
    fun testCantSaveCreatureWithoutStrength(){
        creatureViewModel.intelligence = 10
        creatureViewModel.strength = 0
        creatureViewModel.endurance = 7
        creatureViewModel.drawable = 1
        creatureViewModel.name = "Home Cow"

        val canSaveCreature = creatureViewModel.canSaveCreature()
        assertEquals(false,canSaveCreature)
    }


}