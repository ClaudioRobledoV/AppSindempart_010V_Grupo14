package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.Usuario
import com.example.appsindempart_grupo14.model.UsuarioEntity
import com.example.appsindempart_grupo14.model.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AdminSeeder {

    private const val ADMIN_EMAIL = "admin@sindem.cl"

    suspend fun createAdminIfNeeded(db: AppDatabase) = withContext(Dispatchers.IO) {

        val usuarioDao = db.usuarioDao()

        val exists = usuarioDao.obtener(ADMIN_EMAIL) != null
        if (exists) return@withContext

        // Crear usuario administrador
        val adminEntity = UsuarioEntity(
            email = ADMIN_EMAIL,
            nombreCompleto = "Administrador",
            telefono = "999999999",
            hashedPassword = "hash(123456)",
            rol = "admin"
        )

        usuarioDao.insertar(adminEntity)
    }
}
