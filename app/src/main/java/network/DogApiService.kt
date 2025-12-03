package com.example.appsindempart_grupo14.network

import retrofit2.http.GET

// La respuesta de la API
data class DogResponse(
    val message: List<String>,
    val status: String
)

interface DogApiService {

    @GET("breeds/image/random/10")
    suspend fun getRandomDogs(): DogResponse
}