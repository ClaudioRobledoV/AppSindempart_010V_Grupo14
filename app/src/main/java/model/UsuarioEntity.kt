package com.example.appsindempart_grupo14.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val email: String,
    val nombreCompleto: String,
    val telefono: String?,
    val hashedPassword: String,
    val rol: String = "cliente"
)
