package com.example.appsindempart_grupo14.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsindempart_grupo14.model.ReservaEntity

@Dao
interface ReservaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(reserva: ReservaEntity)

    @Query("SELECT * FROM reservas WHERE emailUsuario = :email ORDER BY fecha, hora")
    suspend fun obtenerPorUsuario(email: String): List<ReservaEntity>

    @Query("SELECT * FROM reservas WHERE especialista = :nombre ORDER BY fecha, hora")
    suspend fun obtenerPorEspecialista(nombre: String): List<ReservaEntity>

    @Query(
        """
        UPDATE reservas
        SET fecha = :fecha, hora = :hora
        WHERE id = :id
        """
    )
    suspend fun actualizarFechaHora(id: String, fecha: String, hora: String)

    @Query("UPDATE reservas SET estado = :nuevoEstado WHERE id = :id")
    suspend fun actualizarEstado(id: String, nuevoEstado: String)

    @Delete
    suspend fun eliminar(reserva: ReservaEntity)

    @Query("DELETE FROM reservas WHERE emailUsuario = :email")
    suspend fun eliminarReservasUsuario(email: String)


    @Query("SELECT * FROM reservas ORDER BY fecha, hora")
    suspend fun obtenerTodas(): List<ReservaEntity>


    @Query("SELECT * FROM reservas WHERE estado = :estado ORDER BY fecha, hora")
    suspend fun obtenerPorEstado(estado: String): List<ReservaEntity>


    @Query("SELECT * FROM reservas WHERE fecha = :fecha ORDER BY hora")
    suspend fun obtenerPorFecha(fecha: String): List<ReservaEntity>
}
