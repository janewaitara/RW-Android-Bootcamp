package com.janewaitara.listmaker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager

class ListDataManager(application: Application) : AndroidViewModel(application){

    private val context = application.applicationContext

    fun saveList(list: TaskList){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPref.putStringSet(list.name,list.tasks.toHashSet())  //doesn't save an array but a set
        sharedPref.apply() //saves preference
    }

    fun readLists(): ArrayList<TaskList>{

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val content = sharedPrefs.all    //contains a map of keys and values
        val taskLists = ArrayList<TaskList>()

        for (taskList in content) {
            val taskItems = ArrayList(taskList.value as HashSet<String>)   //get saved hashSet and convert it into an arrayList
            val list = TaskList(taskList.key,taskItems)//create a taskList from it
            taskLists.add(list)
        }

        return taskLists
    }

}

