package com.example.spaceapp.api

import com.example.spaceapp.model.SpaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceApi {

    @GET("articles/")
    suspend fun searchSpace(@Query("format")letter : String = "json"): SpaceResponse
}