package com.raywenderlich.android.datadrop.ui.droplist

import androidx.recyclerview.widget.DiffUtil
import com.raywenderlich.android.datadrop.model.Drop

class DropDiffCallback(private val oldDrops: List<Drop>,
                       private val newDrops: List<Drop>) : DiffUtil.Callback(){
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDrops[oldItemPosition].id == newDrops[newItemPosition].id
    }

    override fun getOldListSize() = oldDrops.size
    override fun getNewListSize() = newDrops.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDrop = oldDrops[oldItemPosition]
        val newDrop = newDrops[newItemPosition]

        return oldDrop.dropMessage == newDrop.dropMessage &&
                oldDrop.latLng == newDrop.latLng &&
                oldDrop.markerColor == newDrop.markerColor
    }


}