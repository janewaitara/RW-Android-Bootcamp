package com.janewaitara.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TodoListAdapter(private val lists: ArrayList<TaskList>, private val clickListener :TodoListClickListener): RecyclerView.Adapter<TodoListViewHolder> (){

    interface TodoListClickListener{
        fun listItemClicked(list: TaskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        //create new View Holder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_view_holder, parent,false) // don't want to automatically attach it ,RV will do that
        return TodoListViewHolder(view)

    }

    override fun getItemCount(): Int {
        //tells the RV how many items ae in the list
        return lists.size
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        //Customizes the each row according to your data, called for each row in the list
        holder.listPositionTextView.text = (position + 1).toString()
        holder.listTitleTextView.text = lists[position].name
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        } //adding an onClickListener
    }

    //updates the RV with the list
    fun addList(list: TaskList) {
        lists.add(list)
        notifyItemInserted(lists.size - 1)

    }
}