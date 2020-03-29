package com.abhirajsharma.popcorn.movies_gen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhirajsharma.popcorn.R
import com.abhirajsharma.popcorn.api_fetching.Client
import com.abhirajsharma.popcorn.api_fetching.Service
import kotlinx.android.synthetic.main.movies_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MoviesFrag : Fragment() {

    private val THE_MOVIE_DB_API_TOKEN = "c669377735364869546a41a514a03c0f"
    private val msg = "You are not connected to Internet."

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            loadPopularMovies()
            loadTopRatedMovies()
            loadUpcomingovies()
        }catch (e:Exception){
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show()
        }


        return inflater.inflate(R.layout.movies_layout, container, false)
    }

    private fun loadPopularMovies() {
        val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
        val call: Call<MoviesResponse> = apiService.getPopularMovies(THE_MOVIE_DB_API_TOKEN)

        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                val movies = response.body()!!.results as List<Movies>
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val firstAdapter = MoviesAdapter(context!!, movies)
                        val firstManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        firstRecyclerView.layoutManager = firstManager
                        firstRecyclerView.adapter = firstAdapter
                    }
                }
            }

            override fun onFailure(call: Call<MoviesResponse?>?, t: Throwable) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTopRatedMovies() {
        val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
        val call: Call<MoviesResponse> = apiService.getTopRatedMovies(THE_MOVIE_DB_API_TOKEN)

        call.enqueue(object : Callback<MoviesResponse?> {
            override fun onResponse(call: Call<MoviesResponse?>?, response: Response<MoviesResponse?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val movies = response.body()!!.results as List<Movies>
                        val secondAdapter = MoviesAdapter(context!!, movies)
                        val secondManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        secondRecyclerView.layoutManager = secondManager
                        secondRecyclerView.adapter = secondAdapter
                    }
                }
            }
            override fun onFailure(call: Call<MoviesResponse?>?, t: Throwable) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadUpcomingovies() {
        val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
        val call: Call<MoviesResponse> = apiService.getUpcomingMovies(THE_MOVIE_DB_API_TOKEN)

        call.enqueue(object : Callback<MoviesResponse?> {
            override fun onResponse(call: Call<MoviesResponse?>?, response: Response<MoviesResponse?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val movies = response.body()!!.results as List<Movies>
                        val thirdAdapter = MoviesAdapter(context!!, movies)
                        val thirdManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        thirdRecyclerView.layoutManager = thirdManager
                        thirdRecyclerView.adapter = thirdAdapter
                    }
                }
            }
            override fun onFailure(call: Call<MoviesResponse?>?, t: Throwable) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
