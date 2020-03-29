package com.abhirajsharma.popcorn.movies_gen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhirajsharma.popcorn.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_card.view.*

class MoviesAdapter(var mContext: Context, var movieList: List<Movies>) : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val li = LayoutInflater.from(viewGroup.context)
        val view: View = li.inflate(R.layout.movie_card, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val vote = (movieList[i].voteAverage!!).toString()
        val poster = "https://image.tmdb.org/t/p/w500" + movieList[i].posterPath
        viewHolder.itemView.title.text = movieList[i].originalTitle
        viewHolder.itemView.userrating.text = vote
        Glide.with(mContext).load(poster).placeholder(R.drawable.loading).into(viewHolder.itemView.thumbnail)
    }

    override fun getItemCount(): Int = movieList.size

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem = movieList[pos]
                    val intent = Intent(mContext, MoviesDetailActivity::class.java)
                    intent.putExtra("movies", clickedDataItem)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }
            }
        }
    }
}