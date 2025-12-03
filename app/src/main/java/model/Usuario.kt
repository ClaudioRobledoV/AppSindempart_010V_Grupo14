package com.example.appsindempart_grupo14.model

data class Usuario(
    val nombreCompleto: String,
    val email: String,
    val telefono: String?,
    val hashedPassword: String,
    val rol: String = "cliente",
    val mascotas: List<Mascota>
)
