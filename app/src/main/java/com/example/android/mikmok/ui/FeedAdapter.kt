package com.example.android.mikmok.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mikmok.R
import com.example.android.mikmok.data.Item

class FeedAdapter(
    private var listener: OnClickListener,
    private var itemse: List<Item>
) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    interface OnClickListener {
        fun onClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentItem = itemse[position]
        holder.itemView.setOnClickListener {
            listener.onClick(currentItem)
        }
    }

    override fun getItemCount() = itemse.size

    class FeedViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        // val binding = something.bind(viewItem)
    }
}
