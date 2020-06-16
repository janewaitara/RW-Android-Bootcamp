package com.janewaitara.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val listPositionTextView = itemView.findViewById<TextView>(R.id.item_number)
    val listTitleTextView = itemView.findViewById<TextView>(R.id.item_string)

}