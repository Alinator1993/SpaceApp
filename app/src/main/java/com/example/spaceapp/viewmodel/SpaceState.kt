package com.example.spaceapp.viewmodel

import com.example.spaceapp.model.SpaceResult

sealed class SpaceState {
    object Loading : SpaceState()
    data class Success(val spaces:List<SpaceResult>) : SpaceState()
    data class Error(val message: String) : SpaceState()
}