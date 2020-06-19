package com.janewaitara.listmaker

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TodoListAdapter.TodoListClickListener {

    private lateinit var todoListRecyclerView: RecyclerView

    private val listDataManager: ListDataManager = ListDataManager(this)

    companion object{
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123 //used in startActivityResult as an identifier for that activity being started to help get the result back
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val lists = listDataManager.readLists()
        todoListRecyclerView = findViewById(R.id.list_recyclerview)
        todoListRecyclerView.layoutManager = LinearLayoutManager(this) //knowing about layout when placing items
        todoListRecyclerView.adapter = TodoListAdapter(lists, this) //passing the list read from the manager

        fab.setOnClickListener { _  ->
            showCreateTodoListDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //called for every activity launched and returns a result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE){
            data?.let { //check if there is data and unpack it
                val list = data.getParcelableExtra<TaskList>(INTENT_LIST_KEY)!! //getting the data
                listDataManager.saveList(list) //saving the list
                updateList()
            }
        }
    }

    private fun updateList() {

        val lists = listDataManager.readLists()
        todoListRecyclerView.adapter = TodoListAdapter(lists, this) //refreshing the RV

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCreateTodoListDialog(){
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)
        val myDialog = AlertDialog.Builder(this)
        val todoTitleEditText = EditText(this)
        todoTitleEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS

        myDialog.setTitle(dialogTitle)
        myDialog.setView(todoTitleEditText)
        myDialog.setPositiveButton(positiveButtonTitle){
            dialog, _ ->  //takes a dialog and an int to know which button was tapped

            val adapter = todoListRecyclerView.adapter as TodoListAdapter
            val list = TaskList(todoTitleEditText.text.toString())
            listDataManager.saveList(list) //saving the list
            adapter.addList(list) // updating the RecyclerView with the list

            dialog.dismiss()
            showTaskListItems(list)
        }
        myDialog.create().show()

    }

    private fun showTaskListItems(list: TaskList){
        val taskListItem = Intent(this, DetailActivity::class.java)
        taskListItem.putExtra(INTENT_LIST_KEY,list)
        startActivityForResult(taskListItem, LIST_DETAIL_REQUEST_CODE)
    }

    override fun listItemClicked(list: TaskList) {
        showTaskListItems(list)
    }

}
