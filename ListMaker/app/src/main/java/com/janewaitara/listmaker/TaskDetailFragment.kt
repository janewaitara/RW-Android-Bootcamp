package com.janewaitara.listmaker

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class TaskDetailFragment : Fragment() {

    lateinit var list: TaskList
    lateinit var taskListRecyclerView: RecyclerView
    lateinit var addTaskButton: FloatingActionButton
    lateinit var listDataManager: ListDataManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listDataManager = ViewModelProvider(this).get(ListDataManager::class.java)

        //getting the data passed in as safe arguments
        arguments?.let {
            val args = TaskDetailFragmentArgs.fromBundle(it)
            list = listDataManager.readLists().filter { list->
                list.name == args.listString}[0]
        }
        activity?.let {
            taskListRecyclerView = view.findViewById(R.id.task_list_recyclerView)
            taskListRecyclerView.layoutManager = LinearLayoutManager(it)
            taskListRecyclerView.adapter = TaskListAdapter(list)
            it.toolbar.title = list.name

            addTaskButton = view.findViewById(R.id.add_task_button)
            addTaskButton.setOnClickListener{
                showCreateTaskDialog()
            }
        }

    }

    companion object {

        private val ARG_LIST = "list"

        fun newInstance(list: TaskList): TaskDetailFragment {

            val bundle = Bundle() //object containing key, value pairs
            //accessing your list within a bundle
            bundle.putParcelable(ARG_LIST,list)
            val fragment = TaskDetailFragment()
            fragment.arguments = bundle //using arguments to pass data into the fragment
            return fragment

        }
    }

    private fun showCreateTaskDialog() {
        activity?.let {
            val taskEditText = EditText(it)
            taskEditText.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(it)
                .setTitle(R.string.task_to_add)
                .setView(taskEditText)
                .setPositiveButton(R.string.add_task) { dialog, _ ->

                    val task = taskEditText.text.toString()
                    list.tasks.add(task)
                    listDataManager.saveList(list)
                    dialog.dismiss()
                }
                .create().show()
        }
    }
}
