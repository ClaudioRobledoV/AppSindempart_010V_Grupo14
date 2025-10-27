package com.example.appsindempart_grupo14.model

data class Reserva(
    val id: String,
    val emailUsuario: String,
    val mascota: String,
    val mascotaNombre: String,
    val mascotaEdad: Int,
    val mascotaPeso: Double,
    val mascotaRaza: String,
    val especialista: String,
    val tipoAtencion: String,
    val fecha: String,
    val hora: String,
    val estado: String = "Próxima"
)