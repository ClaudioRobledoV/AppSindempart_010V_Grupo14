package com.example.appsindempart_grupo14.network

class DogRepository {

    private val api = RetrofitClient.api

    suspend fun obtenerImagenes(): List<String> {
        val response = api.getRandomDogs()

        if (response.status != "success") {
            throw Exception("Error al cargar imágenes")
        }

        return response.message
    }
}
