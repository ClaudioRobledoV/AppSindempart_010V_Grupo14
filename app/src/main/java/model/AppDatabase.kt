package com.example.appsindempart_grupo14.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsindempart_grupo14.dao.MascotaDao
import com.example.appsindempart_grupo14.dao.ReservaDao
import com.example.appsindempart_grupo14.dao.UsuarioDao

@Database(
    entities = [
        UsuarioEntity::class,
        MascotaEntity::class,
        ReservaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun reservaDao(): ReservaDao
}
