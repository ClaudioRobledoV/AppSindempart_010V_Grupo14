package com.example.appsindempart_grupo14

import com.example.appsindempart_grupo14.model.Usuario
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UsuarioModelTest {

    @Test
    fun `crear usuario asigna valores correctamente`() {
        val u = Usuario(
            nombreCompleto = "Juan Pérez",
            email = "juan@test.com",
            telefono = "912345678",
            hashedPassword = "hash(abc123)",
            rol = "cliente",
            mascotas = emptyList()
        )

        assertEquals("Juan Pérez", u.nombreCompleto)
        assertEquals("juan@test.com", u.email)
        assertEquals("912345678", u.telefono)
        assertEquals("cliente", u.rol)
    }
}
