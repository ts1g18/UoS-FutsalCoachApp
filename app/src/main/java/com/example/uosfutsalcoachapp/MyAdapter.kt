package com.example.uosfutsalcoachapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_view_tactic.view.*
import kotlinx.android.synthetic.main.tactic_item.view.*

class MyAdapter(private val tacticList: MutableList<TacticItem>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var selected_position = -1;
    var removedPosition : Int? = null

    //called by recycler view when its time to create a new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.tactic_item,
            parent, false
        )

        return MyViewHolder(itemView)
    }

    //called over and over again (many times per second) , e.g. when adding item or when scrolling
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tacticList[position]
        holder.imageView.setImageResource(currentItem.imageResource) //we added these properties in constructor of TacticItem
        holder.textView1.text = currentItem.text1
        if (selected_position == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#DCDCDC"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        holder.itemView.setOnClickListener {
            selected_position = position
            notifyDataSetChanged()
        }

    }

    override fun getItemCount() = tacticList.size

    //represents one row in our list
    //each view holder will hold references to the views in our row layout (tactic_item.xml)
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //create 3 properties that store the references to the 3 views in our row layout (tactic_item.xml)
        // this is how the viewHolder caches this references
        val imageView: ImageView = itemView.image_view //equivalent to findViewById
        val textView1: TextView = itemView.text_view_1
    }

    fun getSelectedPosition() : Int? {
        var position = selected_position
        return position;
    }
}

