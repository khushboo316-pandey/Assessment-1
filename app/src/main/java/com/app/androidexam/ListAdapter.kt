package com.app.androidexam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private var items: List<String>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val subtitle: TextView = view.findViewById(R.id.itemSubtitle)
        val image: ImageView = view.findViewById(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item
        holder.subtitle.text = "Subtitle for $item"
        // Use Glide or Picasso to load image
    }

    fun updateData(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}

