package com.example.appsindempart_grupo14.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appsindempart_grupo14.model.MascotaEntity

@Dao
interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(mascota: MascotaEntity)

    @Query("SELECT * FROM mascotas WHERE emailUsuario = :email")
    suspend fun obtenerMascotasUsuario(email: String): List<MascotaEntity>

    @Update
    suspend fun actualizar(mascota: MascotaEntity)

    @Delete
    suspend fun eliminar(mascota: MascotaEntity)

    @Query("DELETE FROM mascotas WHERE emailUsuario = :email")
    suspend fun eliminarMascotasUsuario(email: String)
}
