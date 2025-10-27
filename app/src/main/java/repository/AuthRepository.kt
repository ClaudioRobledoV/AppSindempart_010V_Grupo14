package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.Usuario
import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun esEmailUnico(email: String): Boolean
    suspend fun registrar(usuario: Usuario): Result<Unit>
    suspend fun login(email: String, password: String): Result<Usuario>
    suspend fun getUsuario(email: String): Usuario?
    suspend fun updateUsuario(usuario: Usuario): Result<Unit>
    suspend fun deleteUsuario(email: String): Result<Unit>
}

class InMemoryAuthRepository : AuthRepository {
    private val usuarios = mutableListOf<Usuario>()

    private fun hash(p: String) = "hash($p)"

    override suspend fun esEmailUnico(email: String): Boolean {
        delay(150)
        return usuarios.none { it.email.equals(email, ignoreCase = true) }
    }

    override suspend fun registrar(usuario: Usuario): Result<Unit> {
        delay(250)
        if (!esEmailUnico(usuario.email)) {
            return Result.failure(IllegalStateException("El correo ya existe"))
        }
        usuarios.add(usuario)
        return Result.success(Unit)
    }

    override suspend fun login(email: String, password: String): Result<Usuario> {
        delay(200)
        val u = usuarios.firstOrNull { it.email.equals(email, ignoreCase = true) }
            ?: return Result.failure(IllegalStateException("Usuario no encontrado"))
        return if (u.hashedPassword == hash(password)) Result.success(u)
        else Result.failure(IllegalArgumentException("Contraseña incorrecta"))
    }

    // 🔹 CRUD usuario
    override suspend fun getUsuario(email: String): Usuario? {
        delay(150)
        return usuarios.firstOrNull { it.email.equals(email, ignoreCase = true) }
    }

    override suspend fun updateUsuario(usuario: Usuario): Result<Unit> {
        delay(200)
        val idx = usuarios.indexOfFirst { it.email.equals(usuario.email, ignoreCase = true) }
        return if (idx != -1) {
            usuarios[idx] = usuario
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("Usuario no encontrado"))
        }
    }

    override suspend fun deleteUsuario(email: String): Result<Unit> {
        delay(200)
        val removed = usuarios.removeIf { it.email.equals(email, ignoreCase = true) }
        return if (removed) Result.success(Unit)
        else Result.failure(IllegalStateException("Usuario no encontrado"))
    }
}