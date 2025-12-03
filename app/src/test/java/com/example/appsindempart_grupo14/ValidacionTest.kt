package com.example.appsindempart_grupo14

import com.example.appsindempart_grupo14.model.Validacion
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidacionTest {

    @Test
    fun `email valido debe retornar null`() {
        val result = Validacion.emailValido("usuario@test.com")
        assertNull(result)
    }

    @Test
    fun `email invalido debe retornar mensaje de error`() {
        val result = Validacion.emailValido("usuario-invalido")
        assertNotNull(result)
        assertEquals("Correo inválido", result)
    }

    @Test
    fun `password valida debe retornar null`() {
        val result = Validacion.passwordValida("abcdef")
        assertNull(result)
    }

    @Test
    fun `password corta debe fallar`() {
        val result = Validacion.passwordValida("123")
        assertEquals("Debe tener mínimo 4 caracteres", result)
    }

    @Test
    fun `confirmar password debe coincidir`() {
        val result = Validacion.confirmarPassword("abc123", "abc123")
        assertNull(result)
    }

    @Test
    fun `confirmar password distinta debe fallar`() {
        val result = Validacion.confirmarPassword("abc123", "xyz789")
        assertEquals("Las contraseñas no coinciden", result)
    }
}
