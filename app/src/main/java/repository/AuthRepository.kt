package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.Usuario

interface AuthRepository {

    suspend fun esEmailUnico(email: String): Boolean

    suspend fun registrar(usuario: Usuario): Result<Unit>

    suspend fun login(email: String, password: String): Result<Usuario>

    // ¿EXISTE ESTA LÍNEA?
    suspend fun getUsuario(email: String): Usuario?

    suspend fun updateUsuario(usuario: Usuario): Result<Unit>

    suspend fun deleteUsuario(email: String): Result<Unit>
}
