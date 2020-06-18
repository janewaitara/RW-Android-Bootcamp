package com.janewaitara.listmaker

import android.os.Parcel
import android.os.Parcelable

class TaskList(val name: String,
               val tasks: ArrayList<String> = ArrayList()): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!, //gets the name of the list and needs to be unwrapped
        parcel.createStringArrayList()!! //gets the list itself
    )

    companion object CREATOR : Parcelable.Creator<TaskList> {
        override fun createFromParcel(parcel: Parcel): TaskList = TaskList(parcel) //creates a parcel. When called, the constructor is called
        override fun newArray(size: Int): Array<TaskList?> = arrayOfNulls(size)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name) //converting list into a parcel
        dest.writeStringList(tasks)
    }

    override fun describeContents(): Int = 0 //only used with file descriptors

}