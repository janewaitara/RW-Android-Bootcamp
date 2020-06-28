package com.raywenderlich.android.datadrop.ui.droplist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.raywenderlich.android.datadrop.R
import com.raywenderlich.android.datadrop.app.Injection
import com.raywenderlich.android.datadrop.model.Drop
import com.raywenderlich.android.datadrop.viewModel.ClearDropsListener
import com.raywenderlich.android.datadrop.viewModel.DropsViewModel
import kotlinx.android.synthetic.main.activity_list.*

class DropListActivity : AppCompatActivity(),  DropListAdapter.DropListAdapterListener {

  private lateinit var dropsViewModel: DropsViewModel
  private val adapter = DropListAdapter(mutableListOf(), this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)

    title = getString(R.string.all_drops)

    listRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    listRecyclerView.adapter = adapter

    val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
    itemTouchHelper.attachToRecyclerView(listRecyclerView)

   dropsViewModel = ViewModelProviders.of(this).get(DropsViewModel::class.java)

   dropsViewModel.getDrops().observe(this, Observer<List<Drop>>{drops ->
     adapter.updateDrops(drops ?: emptyList()) //if the observed drops are null, we send an empty list to the adapter
   })

  }


  override fun deleteDropAtPosition(drop: Drop, position: Int) {
    dropsViewModel.clearDrop(drop, object : ClearDropsListener{
      override fun dropCleared(drop: Drop) {
        adapter.removeDropAtPosition(position)
        checkForEmptyState()
      }

    })
  }

  private fun checkForEmptyState() {
    emptyState.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.INVISIBLE
  }

  companion object {
    fun newIntent(context: Context) = Intent(context, DropListActivity::class.java)
  }
}
