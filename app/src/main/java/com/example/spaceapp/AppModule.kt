package com.example.spaceapp

import com.example.spaceapp.api.SpaceApi
import com.example.spaceapp.repository.SpaceRepository
import com.example.spaceapp.viewmodel.SpaceViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // 1. Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaceApi::class.java)  // your API interface
    }

    // 2. Repository gets the API service injected
    single { SpaceRepository(get()) }

    // 3. ViewModel gets the repository injected
    viewModel { SpaceViewModel(get()) }
}



