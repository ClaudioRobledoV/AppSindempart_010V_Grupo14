package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.AppDatabase
import com.example.appsindempart_grupo14.model.Mascota
import com.example.appsindempart_grupo14.model.MascotaEntity
import com.example.appsindempart_grupo14.model.Usuario
import com.example.appsindempart_grupo14.model.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomAuthRepository(private val db: AppDatabase) : AuthRepository {

    private val usuarioDao = db.usuarioDao()
    private val mascotaDao = db.mascotaDao()

    private fun hash(p: String) = "hash($p)"

    // --------------------------------------------------
    // EMAIL ÚNICO
    // --------------------------------------------------
    override suspend fun esEmailUnico(email: String): Boolean =
        withContext(Dispatchers.IO) {
            usuarioDao.obtener(email) == null
        }

    // --------------------------------------------------
    // REGISTRO
    // --------------------------------------------------
    override suspend fun registrar(usuario: Usuario): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {

                val entity = UsuarioEntity(
                    email = usuario.email,
                    nombreCompleto = usuario.nombreCompleto,
                    telefono = usuario.telefono,
                    hashedPassword = usuario.hashedPassword,
                    rol = usuario.rol
                )

                usuarioDao.insertar(entity)

                usuario.mascotas.forEach {
                    mascotaDao.insertar(
                        MascotaEntity(
                            emailUsuario = usuario.email,
                            nombre = it.nombre,
                            tipo = it.tipo
                        )
                    )
                }

                Result.success(Unit)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun getUsuario(email: String): Usuario? =
        withContext(Dispatchers.IO) {
            val entity = usuarioDao.obtener(email) ?: return@withContext null

            val mascotas = mascotaDao.obtenerMascotasUsuario(email).map {
                Mascota(it.nombre, it.tipo)
            }

            Usuario(
                email = entity.email,
                nombreCompleto = entity.nombreCompleto,
                telefono = entity.telefono,
                hashedPassword = entity.hashedPassword,
                rol = entity.rol,
                mascotas = mascotas
            )
        }




    override suspend fun login(email: String, password: String): Result<Usuario> =
        withContext(Dispatchers.IO) {
            try {

                val entity = usuarioDao.obtener(email)
                    ?: return@withContext Result.failure(Exception("Usuario no encontrado"))

                if (entity.hashedPassword != hash(password)) {
                    return@withContext Result.failure(Exception("Contraseña incorrecta"))
                }

                val mascotas = mascotaDao.obtenerMascotasUsuario(email).map {
                    Mascota(it.nombre, it.tipo)
                }

                Result.success(
                    Usuario(
                        email = entity.email,
                        nombreCompleto = entity.nombreCompleto,
                        telefono = entity.telefono,
                        hashedPassword = entity.hashedPassword,
                        rol = entity.rol,
                        mascotas = mascotas
                    )
                )

            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun updateUsuario(usuario: Usuario): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {

                val entity = UsuarioEntity(
                    email = usuario.email,
                    nombreCompleto = usuario.nombreCompleto,
                    telefono = usuario.telefono,
                    hashedPassword = usuario.hashedPassword,
                    rol = usuario.rol
                )

                usuarioDao.actualizar(entity)
                Result.success(Unit)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteUsuario(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                usuarioDao.eliminar(email)
                mascotaDao.eliminarMascotasUsuario(email)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
