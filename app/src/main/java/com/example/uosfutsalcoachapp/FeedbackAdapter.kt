package com.example.uosfutsalcoachapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.feedback_item.view.*
import kotlinx.android.synthetic.main.member_item.view.*


class FeedbackAdapter(private val feedbackList: MutableList<FeedbackItem>) :
    RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>() {
    private var selected_position = -1;

    //called by recycler view when its time to create a new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.feedback_item,
            parent, false
        )

        return MyViewHolder(itemView)
    }

    //called over and over again (many times per second) , e.g. when adding item or when scrolling
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = feedbackList[position]
        holder.imageView.setImageResource(currentItem.imageResource) //we added these properties in constructor of TacticItem
        holder.author.text = currentItem.author
        holder.content.text = currentItem.content
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

    override fun getItemCount() = feedbackList.size

    //represents one row in our list
    //each view holder will hold references to the views in our row layout (tactic_item.xml)
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //create 3 properties that store the references to the 3 views in our row layout (ftactic_item.xml)
        // this is how the viewHolder caches this references
        val imageView: ImageView = itemView.image_view_feedback //equivalent to findViewById
        val author: TextView = itemView.author
        val content: TextView = itemView.content
    }
}

