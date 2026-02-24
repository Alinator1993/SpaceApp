package com.example.spaceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceapp.repository.SpaceRepository
import kotlinx.coroutines.launch

class SpaceViewModel(private val spaceRepository: SpaceRepository = SpaceRepository()): ViewModel() {

    private val _spaceState = MutableLiveData<SpaceState>(SpaceState.Loading)
    val spaceState: LiveData<SpaceState> = _spaceState

    init {
        fetchSpaces()
    }

    private fun fetchSpaces() {
        viewModelScope.launch {
            _spaceState.value = SpaceState.Loading
            val result = spaceRepository.getSpace()
            _spaceState.value = if(result.isSuccess){
                val spaces = result.getOrNull() ?: emptyList()
                SpaceState.Success(spaces)
            } else{
                SpaceState.Error(result.exceptionOrNull() ?.message ?: "Unknown error")
            }
        }
    }
}