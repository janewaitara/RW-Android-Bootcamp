package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.CreatureStore
import kotlinx.android.synthetic.main.list_item_creatures.view.creatureImage
import kotlinx.android.synthetic.main.list_item_creatures_with_food.view.*

class CreatureWithFoodAdapter(private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureWithFoodAdapter.ViewHolder>() {

    //improve the scrolling performance with a Recycled ViewPool
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val holder =  ViewHolder(parent.inflate(R.layout.list_item_creatures_with_food,false)) //we are using the extension method in app folder
        holder.itemView.foodRecyclerView.setRecycledViewPool(viewPool)
        //snap each food item into place as the user scrolls horizontally so that food items are fully on the screen
        LinearSnapHelper().attachToRecyclerView(holder.itemView.foodRecyclerView)
        return holder
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val adapter = FoodAdapter(mutableListOf())

        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)//setting the viewHolder as the click Listener for the itemView
        }

        fun bind(creature: Creature){
           this.creature = creature
            val context = itemView.context
            //set the creature image and fullName using data from the creature
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null,context.packageName))
            setupFoods()
        }

        override fun onClick(view: View) {
            //get the context and create an intent for the Detail Screen
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }
        private fun setupFoods(){
            itemView.foodRecyclerView.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,false)
            itemView.foodRecyclerView.adapter = adapter

            val foods = CreatureStore.getCreatureFoods(creature)
            adapter.updateFood(foods)
        }

    }
}