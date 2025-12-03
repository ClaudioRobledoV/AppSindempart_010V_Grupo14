package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.AppDatabase
import com.example.appsindempart_grupo14.model.Reserva
import com.example.appsindempart_grupo14.model.ReservaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomReservaRepository(
    private val db: AppDatabase
) : ReservaRepository {
    private val dao = db.reservaDao()
    private fun toEntity(r: Reserva) = ReservaEntity(
        id = r.id,
        emailUsuario = r.emailUsuario,
        mascota = r.mascota,
        mascotaNombre = r.mascotaNombre,
        mascotaEdad = r.mascotaEdad,
        mascotaPeso = r.mascotaPeso,
        mascotaRaza = r.mascotaRaza,
        especialista = r.especialista,
        tipoAtencion = r.tipoAtencion,
        fecha = r.fecha,
        hora = r.hora,
        estado = r.estado
    )

    private fun toDomain(e: ReservaEntity) = Reserva(
        id = e.id,
        emailUsuario = e.emailUsuario,
        mascota = e.mascota,
        mascotaNombre = e.mascotaNombre,
        mascotaEdad = e.mascotaEdad,
        mascotaPeso = e.mascotaPeso,
        mascotaRaza = e.mascotaRaza,
        especialista = e.especialista,
        tipoAtencion = e.tipoAtencion,
        fecha = e.fecha,
        hora = e.hora,
        estado = e.estado
    )

    override suspend fun crearReserva(reserva: Reserva): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dao.insertar(toEntity(reserva))
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun listarReservas(emailUsuario: String): List<Reserva> =
        withContext(Dispatchers.IO) {
            dao.obtenerPorUsuario(emailUsuario).map(::toDomain)
        }


    override suspend fun listarReservasPorEspecialista(nombre: String): List<Reserva> =
        withContext(Dispatchers.IO) {
            dao.obtenerPorEspecialista(nombre).map(::toDomain)
        }

    override suspend fun actualizarEstado(id: String, nuevoEstado: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dao.actualizarEstado(id, nuevoEstado)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun cancelarReserva(id: String): Result<Unit> =
        actualizarEstado(id, "Cancelada")


    override suspend fun modificarReserva(reserva: Reserva): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dao.actualizarFechaHora(
                    id = reserva.id,
                    fecha = reserva.fecha,
                    hora = reserva.hora
                )
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun listarTodas(): List<Reserva> =
        withContext(Dispatchers.IO) {
            dao.obtenerTodas().map(::toDomain)
        }


    override suspend fun listarPorEstado(estado: String): List<Reserva> =
        withContext(Dispatchers.IO) {
            dao.obtenerPorEstado(estado).map(::toDomain)
        }


    override suspend fun listarPorFecha(fecha: String): List<Reserva> =
        withContext(Dispatchers.IO) {
            dao.obtenerPorFecha(fecha).map(::toDomain)
        }
}
