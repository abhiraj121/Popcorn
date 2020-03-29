package com.abhirajsharma.popcorn.trailers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abhirajsharma.popcorn.R
import kotlinx.android.synthetic.main.trailer_card.view.*

class TrailerAdapter(var mContext: Context, var trailerList: List<Trailer>) : RecyclerView.Adapter<TrailerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.trailer_card, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        viewHolder.itemView.trailerTitle.text = trailerList[i].name
    }

    override fun getItemCount(): Int {
        return trailerList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem: Trailer = trailerList[pos]
                    val videoId: String = trailerList[pos].key
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("VIDEO_ID", videoId)
                    mContext.startActivity(intent)
                    Toast.makeText(mContext, "You clicked " + clickedDataItem.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
