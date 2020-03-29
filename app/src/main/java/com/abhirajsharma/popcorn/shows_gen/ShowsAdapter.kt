package com.abhirajsharma.popcorn.shows_gen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhirajsharma.popcorn.movies_gen.MoviesDetailActivity
import com.abhirajsharma.popcorn.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.shows_card.view.*

class ShowsAdapter(var mContext: Context, var showsList: List<TvShows>) : RecyclerView.Adapter<ShowsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val li = LayoutInflater.from(viewGroup.context)
        val view: View = li.inflate(R.layout.shows_card, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val vote = (showsList[i].voteAverage!!).toString()
        val poster = "https://image.tmdb.org/t/p/w500" + showsList[i].posterPath
        viewHolder.itemView.showsTitle.text = showsList[i].name
        viewHolder.itemView.showsUserrating.text = vote
        Glide.with(mContext).load(poster).placeholder(R.drawable.loading).into(viewHolder.itemView.showsThumbnail)
    }

    override fun getItemCount() =  showsList.size

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem = showsList[pos]
                    val intent = Intent(mContext, ShowsDetailActivity::class.java)
                    intent.putExtra("shows", clickedDataItem)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }
            }
        }
    }
}