package com.janewaitara.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TodoListAdapter(val lists: ArrayList<TaskList>): RecyclerView.Adapter<TodoListViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        //create new View Holder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_view_holder, parent,false) // dont want to automatically attach it ,RV will do that
        return TodoListViewHolder(view)

    }

    override fun getItemCount(): Int {
        //tells the RV how many items ae in the list
        return lists.size
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        //Customizes the view
        holder.listPositionTextView.text = (position + 1).toString()
        holder.listTitleTextView.text = lists[position].name
    }

    //updates the RV with the list
    fun addList(list: TaskList) {
        lists.add(list)
        notifyItemInserted(lists.size - 1)

    }
}