package com.example.appsindempart_grupo14.model

data class Especialista(
    val id: String,
    val nombre: String,
    val bio: String,
    val servicios: List<String>,
    val horario: String
)