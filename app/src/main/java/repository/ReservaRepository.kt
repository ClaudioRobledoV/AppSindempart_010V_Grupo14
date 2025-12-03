package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.Reserva
import kotlinx.coroutines.delay

interface ReservaRepository {

    // usuario normal
    suspend fun listarReservas(emailUsuario: String): List<Reserva>
    suspend fun crearReserva(reserva: Reserva): Result<Unit>
    suspend fun modificarReserva(reserva: Reserva): Result<Unit>
    suspend fun cancelarReserva(id: String): Result<Unit>
    suspend fun actualizarEstado(id: String, nuevoEstado: String): Result<Unit>

    // profesional
    suspend fun listarReservasPorEspecialista(nombre: String): List<Reserva>

    // admin
    suspend fun listarTodas(): List<Reserva>
    suspend fun listarPorEstado(estado: String): List<Reserva>
    suspend fun listarPorFecha(fecha: String): List<Reserva>
}

class InMemoryReservaRepository : ReservaRepository {

    private val reservas = mutableListOf<Reserva>()

    override suspend fun listarReservas(emailUsuario: String): List<Reserva> {
        delay(100)
        return reservas.filter { it.emailUsuario.equals(emailUsuario, ignoreCase = true) }
    }

    override suspend fun crearReserva(reserva: Reserva): Result<Unit> {
        delay(100)
        reservas.add(reserva)
        return Result.success(Unit)
    }

    override suspend fun modificarReserva(reserva: Reserva): Result<Unit> {
        delay(100)
        val index = reservas.indexOfFirst { it.id == reserva.id }
        return if (index != -1) {
            reservas[index] = reserva
            Result.success(Unit)
        } else {
            Result.failure(Exception("Reserva no encontrada"))
        }
    }

    override suspend fun cancelarReserva(id: String): Result<Unit> {
        delay(100)
        val index = reservas.indexOfFirst { it.id == id }
        return if (index != -1) {
            reservas[index] = reservas[index].copy(estado = "Cancelada")
            Result.success(Unit)
        } else {
            Result.failure(Exception("Reserva no encontrada"))
        }
    }

    override suspend fun actualizarEstado(id: String, nuevoEstado: String): Result<Unit> {
        delay(100)
        val index = reservas.indexOfFirst { it.id == id }
        return if (index != -1) {
            reservas[index] = reservas[index].copy(estado = nuevoEstado)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Reserva no encontrada"))
        }
    }

    override suspend fun listarReservasPorEspecialista(nombre: String): List<Reserva> {
        delay(100)
        return reservas.filter { it.especialista.equals(nombre, ignoreCase = true) }
    }


    override suspend fun listarTodas(): List<Reserva> {
        delay(100)
        return reservas.sortedWith(compareBy({ it.fecha }, { it.hora }))
    }


    override suspend fun listarPorEstado(estado: String): List<Reserva> {
        delay(100)
        return reservas.filter { it.estado.equals(estado, ignoreCase = true) }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }


    override suspend fun listarPorFecha(fecha: String): List<Reserva> {
        delay(100)
        return reservas.filter { it.fecha == fecha }
            .sortedBy { it.hora }
    }
}
