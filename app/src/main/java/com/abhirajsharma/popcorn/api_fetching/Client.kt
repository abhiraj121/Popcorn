package com.abhirajsharma.popcorn.api_fetching

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    val BASE_URL = "https://api.themoviedb.org/3/"
    var retrofit: Retrofit ?= null

    fun getMyClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}