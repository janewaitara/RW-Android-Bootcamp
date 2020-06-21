package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creatures.view.*

class CreatureAdapter(private val creatures: List<Creature>): RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creatures,false)) //we are using the extension method in app folder
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private lateinit var creature: Creature

        fun bind(creature: Creature){
           this.creature = creature
            val context = itemView.context
            //set the creature image and fullName using data from the creature
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null,context.packageName))
            itemView.fullName.text = creature.fullName

        }
    }
}