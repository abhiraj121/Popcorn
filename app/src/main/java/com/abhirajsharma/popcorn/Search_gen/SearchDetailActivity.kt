package com.abhirajsharma.popcorn.Search_gen

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhirajsharma.popcorn.Db.FavoriteContract
import com.abhirajsharma.popcorn.Db.FavoriteDbHelper
import com.abhirajsharma.popcorn.R
import com.abhirajsharma.popcorn.api_fetching.Client
import com.abhirajsharma.popcorn.api_fetching.Service
import com.abhirajsharma.popcorn.trailers.Trailer
import com.abhirajsharma.popcorn.trailers.TrailerAdapter
import com.abhirajsharma.popcorn.trailers.TrailerResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchDetailActivity : AppCompatActivity() {

    private val THE_MOVIE_DB_API_TOKEN = "c669377735364869546a41a514a03c0f"
    private var mDb: SQLiteDatabase? = null
    private var showName:String? = null
    private var showId:Int? = null
    private var show: SearchItem?= null
//    val itemToBeSearched = intent.getStringExtra("newSearchItem")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val actionBar: android.app.ActionBar? = actionBar
        setSupportActionBar(toolbar2)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val dbHelper = FavoriteDbHelper(this)
        mDb = dbHelper.writableDatabase

        show = intent.getParcelableExtra("searchItem") as SearchItem?
        showName = show?.original_title
        plotsynopsis.text = show?.overview.toString()
        userrating.text = show?.vote_average.toString()
        releasedate.text = show?.release_date.toString()
        showId = show?.id
        val poster = "https://image.tmdb.org/t/p/w500${show?.poster_path}"
        Glide.with(this).load(poster).placeholder(R.drawable.loading).into(imageView)
        collapsing_toolbar.title = showName
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }

    private fun exists(searchItem: String): Boolean {
        val projection = arrayOf(
            FavoriteContract.FavoriteEntry._ID,
            FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
            FavoriteContract.FavoriteEntry.COLUMN_TITLE,
            FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
            FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
            FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
        )
        val selection: String = FavoriteContract.FavoriteEntry.COLUMN_TITLE + " =?"
        val selectionArgs = arrayOf(searchItem)
        val limit = "1"
        val cursor: Cursor = mDb!!.query(
            FavoriteContract.FavoriteEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null,
            limit
        )
        val exists: Boolean = cursor.count > 0
        cursor.close()
        return exists
    }

    private fun initViews() {
        var trailerList: List<SearchItem>? = null
        trailerList = ArrayList()
        val adapter = SearchAdapter(this, trailerList)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        trailerRecyclerView.layoutManager = mLayoutManager
        trailerRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        loadJSON()
    }

    private fun loadJSON() {
        try{
            val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
            val call: Call<TrailerResponse> = apiService.getMovieTrailer(showId!!, THE_MOVIE_DB_API_TOKEN)

            call.enqueue(object : Callback<TrailerResponse?> {
                override fun onResponse(call: Call<TrailerResponse?>, response: Response<TrailerResponse?>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val trailer: List<Trailer> = response.body()!!.results!!
                            val firstAdapter = TrailerAdapter(applicationContext, trailer)
                            val firstManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                            trailerRecyclerView.layoutManager = firstManager
                            trailerRecyclerView.adapter = firstAdapter
                            trailerRecyclerView.smoothScrollToPosition(0)
                        }
                    }
                }

                override fun onFailure(call: Call<TrailerResponse?>, t: Throwable) {
                    Log.d("Error", t.message!!)
                    Toast.makeText(applicationContext, "Error fetching trailer", Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}