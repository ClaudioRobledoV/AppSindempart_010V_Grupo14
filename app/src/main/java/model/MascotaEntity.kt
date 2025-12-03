package com.example.appsindempart_grupo14.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emailUsuario: String,
    val nombre: String,
    val tipo: String
)