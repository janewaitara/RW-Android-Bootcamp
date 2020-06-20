package com.janewaitara.listmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TodoListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TodoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoListFragment : Fragment(),TodoListAdapter.TodoListClickListener {


    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var listDataManager: ListDataManager

    //used when the fragment is in the process of being created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //when fragment requires a layout it must have in order to be presented within the activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            listDataManager = ViewModelProvider(this).get(ListDataManager::class.java) //instantiating the DataManager
        }
        val lists = listDataManager.readLists()
        todoListRecyclerView = view.findViewById(R.id.list_recyclerview)
        todoListRecyclerView.layoutManager = LinearLayoutManager(activity) //knowing about layout when placing items
        todoListRecyclerView.adapter = TodoListAdapter(lists, this) //passing the list read from the manager

        fab.setOnClickListener { _  ->
            showCreateTodoListDialog()
        }

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
         //informs objects when somethings happens
        fun onTodoListClicked(list: TaskList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment
         *
         * @return A new instance of fragment TodoListFragment.
         */

        fun newInstance(): TodoListFragment{
            return TodoListFragment()
        }
    }

    //when viewHolder is clicked, it is called and notifies the listener that something has happened(The listener is the activity)
    override fun listItemClicked(list: TaskList) {
            }

    fun addList(list: TaskList) {
        listDataManager.saveList(list)
        val todoAdapter = todoListRecyclerView.adapter as TodoListAdapter
        todoAdapter.addList(list)
    }

    fun saveList(list: TaskList) {
        listDataManager.saveList(list)
        updateList()
    }

    private fun showCreateTodoListDialog(){
        activity?.let { //fragments can gain access to a context if its available using the activity property

            val dialogTitle = getString(R.string.name_of_list)
            val positiveButtonTitle = getString(R.string.create_list)
            val myDialog = AlertDialog.Builder(it)
            val todoTitleEditText = EditText(it)
            todoTitleEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS

            myDialog.setTitle(dialogTitle)
            myDialog.setView(todoTitleEditText)
            myDialog.setPositiveButton(positiveButtonTitle){
                    dialog, _ ->  //takes a dialog and an int to know which button was tapped

                /*     val adapter = todoListRecyclerView.adapter as TodoListAdapter*/
                val list = TaskList(todoTitleEditText.text.toString())
                /*     listDataManager.saveList(list) //saving the list
                     adapter.addList(list) // updating the RecyclerView with the list*/
                addList(list)

                dialog.dismiss()
                showTaskListItems(list)
            }
            myDialog.create().show()
        }
    }
    private fun showTaskListItems(list: TaskList){

    }

    private fun updateList() {

        val lists = listDataManager.readLists()
        todoListRecyclerView.adapter = TodoListAdapter(lists, this) //refreshing the RV

    }



}
