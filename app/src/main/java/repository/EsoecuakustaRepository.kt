package com.example.appsindempart_grupo14.repository

import com.example.appsindempart_grupo14.model.Especialista
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface EspecialistaRepository {
    suspend fun listar(): List<Especialista>
    suspend fun crear(e: Especialista): Result<Unit>
    suspend fun actualizar(e: Especialista): Result<Unit>
    suspend fun eliminar(id: String): Result<Unit>
}

class InMemoryEspecialistaRepository : EspecialistaRepository {
    private val mutex = Mutex()
    private val datos = mutableListOf(
        Especialista(
            id = "1",
            nombre = "Macarena Zapata",
            bio = "Médica Veterinaria con 8+ años de experiencia en atención general y control preventivo.",
            servicios = listOf("Consulta general", "Control de salud", "Vacunación", "Urgencias menores"),
            horario = "Lun-Vie 09:00–18:00 | Sáb 10:00–14:00"
        )
    )

    override suspend fun listar(): List<Especialista> = mutex.withLock {
        delay(100)
        datos.toList()
    }

    override suspend fun crear(e: Especialista): Result<Unit> = mutex.withLock {
        delay(120)
        if (datos.any { it.id == e.id }) Result.failure(IllegalArgumentException("ID ya existe"))
        else { datos.add(e); Result.success(Unit) }
    }

    override suspend fun actualizar(e: Especialista): Result<Unit> = mutex.withLock {
        delay(120)
        val idx = datos.indexOfFirst { it.id == e.id }
        if (idx == -1) Result.failure(IllegalArgumentException("No existe"))
        else { datos[idx] = e; Result.success(Unit) }
    }

    override suspend fun eliminar(id: String): Result<Unit> = mutex.withLock {
        delay(120)
        val ok = datos.removeIf { it.id == id }
        if (ok) Result.success(Unit) else Result.failure(IllegalArgumentException("No existe"))
    }
}