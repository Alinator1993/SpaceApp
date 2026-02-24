package com.example.spaceapp.remote

import com.example.spaceapp.api.SpaceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

    val api: SpaceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaceApi::class.java)
    }
}