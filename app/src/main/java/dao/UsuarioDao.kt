package com.example.appsindempart_grupo14.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appsindempart_grupo14.model.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun obtener(email: String): UsuarioEntity?

    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios WHERE email = :email")
    suspend fun eliminar(email: String)
}
