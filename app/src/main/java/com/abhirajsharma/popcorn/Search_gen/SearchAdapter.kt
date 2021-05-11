package com.abhirajsharma.popcorn.Search_gen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhirajsharma.popcorn.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_card.view.*

class SearchAdapter(var mContext: Context, var searchList: List<SearchItem>) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem = searchList[pos]
                    val intent = Intent(mContext, SearchDetailActivity::class.java)
                    intent.putExtra("searchItem", clickedDataItem)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.MyViewHolder {
        val li = LayoutInflater.from(parent.context)
        val view: View = li.inflate(R.layout.search_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = searchList.size

    override fun onBindViewHolder(holder: SearchAdapter.MyViewHolder, position: Int) {
        holder.itemView.searchTitle.text = searchList[position].original_title
        holder.itemView.searchOverview.text = searchList[position].overview
        val poster = "https://image.tmdb.org/t/p/w500" + searchList[position].poster_path
        Glide.with(mContext).load(poster).placeholder(R.drawable.loading).into(holder.itemView.searchImageView)

    }
}
