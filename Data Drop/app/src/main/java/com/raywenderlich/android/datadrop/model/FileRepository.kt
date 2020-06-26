package com.raywenderlich.android.datadrop.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.raywenderlich.android.datadrop.app.DataDropApplication
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileRepository: DropRepository {

    private val gson: Gson
        get(){
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Drop::class.java, DropTypeAdapter())
            return builder.create()
        }

    private fun getContext() = DataDropApplication.getAppContext()

    override fun addDrop(drop: Drop) {
        //writing the drop to a file
        val string = gson.toJson(drop) //convert the drop data into a json string

        try {
            val dropStream = dropOutputString(drop)
            dropStream.write(string.toByteArray())
            dropStream.close()
        }catch (e: IOException){
            Log.e("FileRepository","Error saving drop")
        }

    }

    override fun getDrops(): List<Drop> {
       return emptyList()
    }

    override fun clearDrop(drop: Drop) {
        TODO("Not yet implemented")
    }

    override fun clearAllDrops() {
        TODO("Not yet implemented")
    }

    //returns dropsDirectory in internal storage by calling getDir on the context.getDir creates the directory if it doesn't exist
    private fun dropsDirectory() = getContext().getDir("drops",Context.MODE_PRIVATE)

    //retrieves a file object using dropDirectory
    private fun dropFile(fileName: String) = File(dropsDirectory(), fileName)

    //uses the drop id to create a file name for the drop. ".drop" is used to show the file type
    private fun dropFileName(drop: Drop) = drop.id + ".drop"

    //get an outPutString to write to
    private fun dropOutputString(drop: Drop): FileOutputStream{
        return FileOutputStream(dropFile(dropFileName(drop)))
    }

}