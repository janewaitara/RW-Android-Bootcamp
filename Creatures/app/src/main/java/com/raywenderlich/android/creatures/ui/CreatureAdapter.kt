package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creatures.view.*

class CreatureAdapter(private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creatures,false)) //we are using the extension method in app folder
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])

    }

    fun updateCreatures(creatures: List<Creature>){
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)//setting the viewHolder as the click Listener for the itemView
        }

        fun bind(creature: Creature){
           this.creature = creature
            val context = itemView.context
            //set the creature image and fullName using data from the creature
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null,context.packageName))
            itemView.fullName.text = creature.fullName
            itemView.nickName.text = creature.nickname

        }

        override fun onClick(view: View) {
            //get the context and create an intent for the Detail Screen
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }
    }
}