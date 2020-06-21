package com.raywenderlich.android.creatures.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature_card.view.*


class CreatureCardAdapter(private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureCardAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature_card,false)) //we are using the extension method in app folder
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])

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
            val imageResource = context.resources.getIdentifier(creature.uri, null,context.packageName)
            itemView.creatureImage.setImageResource(imageResource)
            itemView.nickName.text = creature.nickname
            setBackgroundColors(context,imageResource)

        }

        override fun onClick(view: View) {
            //get the context and create an intent for the Detail Screen
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

        //a method to set the background colors
        private fun setBackgroundColors(context: Context, imageResource: Int){
            val image = BitmapFactory.decodeResource(context.resources, imageResource)
            Palette.from(image).generate{ palette ->
              val backgroundColor = palette!!.getDominantColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                itemView.creatureCard.setBackgroundColor(backgroundColor)
                itemView.nickNameHolder.setBackgroundColor(backgroundColor)
               val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                itemView.nickName.setTextColor(textColor)
            }

        }

        //a fun to determine if the background color is light or Dark and adjust the textColor accordingly
        private fun isColorDark(color: Int): Boolean{
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114  * Color.blue(color)) / 255
            return darkness >= 0.5

        }
    }
}