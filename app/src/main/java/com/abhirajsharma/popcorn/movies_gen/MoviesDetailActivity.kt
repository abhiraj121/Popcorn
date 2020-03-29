package com.abhirajsharma.popcorn.movies_gen

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.userrating
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesDetailActivity : AppCompatActivity() {

    private val THE_MOVIE_DB_API_TOKEN = "c669377735364869546a41a514a03c0f"
    private var mDb: SQLiteDatabase? = null
    private var movieName:String? = null
    private var movieId:Int? = null
    private var movie:Movies ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val actionBar: android.app.ActionBar? = actionBar
        setSupportActionBar(toolbar2)
        actionBar?.setDisplayHomeAsUpEnabled(true)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dbHelper = FavoriteDbHelper(this)
        mDb = dbHelper.writableDatabase

        movie = intent.getParcelableExtra("movies") as Movies?
        movieName = movie?.originalTitle
        plotsynopsis.text = movie?.overview.toString()
        userrating.text = movie?.voteAverage.toString()
        releasedate.text = movie?.releaseDate.toString()
        movieId = movie?.id
        val poster = "https://image.tmdb.org/t/p/w500${movie?.posterPath}"
        Glide.with(this).load(poster).placeholder(R.drawable.loading).into(imageView)
        collapsing_toolbar.title = movieName

        if (exists(movieName!!)) {
            materialFavoriteButton.isFavorite = true
            materialFavoriteButton.setOnFavoriteChangeListener { buttonView, favorite ->
                if (favorite) {
//                    saveFavorite()
                    Snackbar.make(buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT).show()
                } else {
                    val favoriteDbHelper = FavoriteDbHelper(this@MoviesDetailActivity)
                    favoriteDbHelper.deleteFavorite(movieId!!)
                    Snackbar.make(buttonView, "Removed from Favorite", Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            materialFavoriteButton.setOnFavoriteChangeListener { buttonView, favorite ->
                if (favorite) {
//                    saveFavorite()
                    Snackbar.make(buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT).show()
                } else {
                    movieId = intent.extras!!.getInt("id")
                    val favoriteDbHelper = FavoriteDbHelper(this@MoviesDetailActivity)
                    favoriteDbHelper.deleteFavorite(movieId!!)
                    Snackbar.make(buttonView, "Removed from Favorite", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
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
        var trailerList: List<Trailer>? = null
        trailerList = ArrayList()
        val adapter = TrailerAdapter(this, trailerList)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        trailerRecyclerView.layoutManager = mLayoutManager
        trailerRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        loadJSON()
    }

    private fun loadJSON() {
        try{
            val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
            val call: Call<TrailerResponse> = apiService.getMovieTrailer(movieId!!, THE_MOVIE_DB_API_TOKEN)

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
                    Toast.makeText(this@MoviesDetailActivity, "Error fetching trailer", Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

//    private fun loadReview() {
//        try {
//            if (THE_MOVIE_DB_API_TOKEN.isEmpty()) {
//                Toast.makeText(
//                    applicationContext,
//                    "Please get your API Key",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return
//            } else {
//                val Client = Client()
//                val apiService: Service = Client.getMyClient()!!.create(Service::class.java)
//                val call: Call<Review> =
//                    apiService.getReview(movie_id, THE_MOVIE_DB_API_TOKEN)
//                call.enqueue(object : Callback<Review?> {
//                    override fun onResponse(
//                        call: Call<Review?>,
//                        response: Response<Review?>
//                    ) {
//                        if (response.isSuccessful()) {
//                            if (response.body() != null) {
//                                val reviewResults: List<ReviewResult> = response.body().getResults()
//                                val recyclerView2 =
//                                    findViewById<View>(R.id.review_recyclerview) as MultiSnapRecyclerView
//                                val firstManager = LinearLayoutManager(
//                                    applicationContext,
//                                    LinearLayoutManager.VERTICAL,
//                                    false
//                                )
//                                recyclerView2.layoutManager = firstManager
//                                recyclerView2.adapter = ReviewAdapter(
//                                    applicationContext,
//                                    reviewResults
//                                )
//                                recyclerView2.smoothScrollToPosition(0)
//                            }
//                        }
//                    }
//
//                    override fun onFailure(
//                        call: Call<Review?>,
//                        t: Throwable
//                    ) {
//                    }
//                })
//            }
//        } catch (e: Exception) {
//            Log.d("Error", e.message)
//            Toast.makeText(this, "unable to fetch data", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun saveFavorite() {
//        val favoriteDbHelper = FavoriteDbHelper(activity)
//        val favorite = Movies()
//        val rate = movie!!.voteAverage
//        favorite.setId(movie_id)
//        favorite.setOriginalTitle(movieName)
//        favorite.setPosterPath(imageView)
//        favorite.setVoteAverage(rate)
//        favorite.setOverview(synopsis)
//        favoriteDbHelper.addFavorite(favorite)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            R.id.share -> {
//                shareContent()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun shareContent() {
//        val bitmap = getBitmapFromView(imageView)
//        try {
//            val file = File(this.externalCacheDir, "logicchip.png")
//            val fOut = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
//            fOut.flush()
//            fOut.close()
//            file.setReadable(true, false)
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra(Intent.EXTRA_TEXT, movieName)
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
//            intent.type = "image/png"
//            startActivity(Intent.createChooser(intent, "Share image via"))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun getBitmapFromView(view: View): Bitmap {
//        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(returnedBitmap)
//        val bgDrawable: Drawable? = view.background
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas)
//        } else {
//            canvas.drawColor(Color.WHITE)
//        }
//        view.draw(canvas)
//        return returnedBitmap
//    }


}
