package com.abhirajsharma.popcorn.api_fetching

import com.abhirajsharma.popcorn.Search_gen.SearchItemResponse
import com.abhirajsharma.popcorn.movies_gen.MoviesResponse
import com.abhirajsharma.popcorn.shows_gen.ShowsResponse
import com.abhirajsharma.popcorn.trailers.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey: String): Call<MoviesResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(@Path("movie_id") id: Int, @Query("api_key") apiKey: String): Call<TrailerResponse>

    @GET("tv/popular")
    fun getPopularShows(@Query("api_key") apiKey: String): Call<ShowsResponse>

    @GET("tv/top_rated")
    fun getTopRatedShows(@Query("api_key") apiKey: String): Call<ShowsResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirShows(@Query("api_key") apiKey: String): Call<ShowsResponse>

    @GET("tv/{show_id}/videos")
    fun getShowsTrailer(@Path("show_id") id: Int, @Query("api_key") apiKey: String): Call<TrailerResponse>

    @GET("search/multi")
    fun getSearchItem(@Query("api_key") apiKey: String, @Query("query")query: String): Call<SearchItemResponse>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int, @Query("api_key") apiKey: String): Call<MoviesResponse>

//    @GET("movie/{movie_id}/reviews")
//    fun getReview(@Path("movie_id") id: Int, @Query("api_key") apiKey: String?): Call<Review?>?

}