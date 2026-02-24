package com.example.spaceapp.model

data class SpaceResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<SpaceResult>
)