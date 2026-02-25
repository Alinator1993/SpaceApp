package com.example.spaceapp.repository

import com.example.spaceapp.api.SpaceApi
import com.example.spaceapp.model.SpaceResult
import com.example.spaceapp.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//TODO: HILT DI

class SpaceRepository(private val api: SpaceApi) {

    suspend fun getSpace():Result<List<SpaceResult>> = withContext(Dispatchers.IO){
        try {
            val response = api.searchSpace("json")
            val spaces = response.results
            Result.success(spaces)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}