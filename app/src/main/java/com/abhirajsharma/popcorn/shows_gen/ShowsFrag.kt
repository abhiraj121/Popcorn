package com.abhirajsharma.popcorn.shows_gen

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
import kotlinx.android.synthetic.main.shows_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ShowsFrag : Fragment() {

    private val THE_MOVIE_DB_API_TOKEN = "c669377735364869546a41a514a03c0f"
    private val msg = "You are not connected to Internet."

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        loadPopularShows()
        loadTopRatedShows()
        loadOnTheAirShows()
        return inflater.inflate(R.layout.shows_layout, container, false)
    }

    private fun loadPopularShows() {
        val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
        val call: Call<ShowsResponse> = apiService.getPopularShows(THE_MOVIE_DB_API_TOKEN)

        call.enqueue(object : Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                val shows = response.body()!!.results as List<TvShows>
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val firstAdapter = ShowsAdapter(context!!, shows)
                        val firstManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        try {
                            firstRecyclerView.layoutManager = firstManager
                            firstRecyclerView.adapter = firstAdapter
                        }catch (e:Exception){
                            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTopRatedShows() {
        val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
        val call: Call<ShowsResponse> = apiService.getTopRatedShows(THE_MOVIE_DB_API_TOKEN)

        call.enqueue(object : Callback<ShowsResponse?> {
            override fun onResponse(call: Call<ShowsResponse?>?, response: Response<ShowsResponse?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val shows = response.body()!!.results as List<TvShows>
                        val secondAdapter = ShowsAdapter(context!!, shows)
                        val secondManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        secondRecyclerView.layoutManager = secondManager
                        secondRecyclerView.adapter = secondAdapter
                    }
                }
            }
            override fun onFailure(call: Call<ShowsResponse?>?, t: Throwable) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadOnTheAirShows() {
        val apiService: Service = Client().getMyClient()!!.create(Service::class.java)
        val call: Call<ShowsResponse> = apiService.getOnTheAirShows(THE_MOVIE_DB_API_TOKEN)

        call.enqueue(object : Callback<ShowsResponse?> {
            override fun onResponse(call: Call<ShowsResponse?>?, response: Response<ShowsResponse?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val shows = response.body()!!.results as List<TvShows>
                        val thirdAdapter = ShowsAdapter(context!!, shows)
                        val thirdManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        thirdRecyclerView.layoutManager = thirdManager
                        thirdRecyclerView.adapter = thirdAdapter
                    }
                }
            }
            override fun onFailure(call: Call<ShowsResponse?>?, t: Throwable) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
