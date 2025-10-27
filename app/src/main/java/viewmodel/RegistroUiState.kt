package com.example.appsindempart_grupo14.viewmodel

import com.example.appsindempart_grupo14.model.Mascota
import com.example.appsindempart_grupo14.model.TipoMascota

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val mascotas: List<Mascota> = emptyList(),
    val mascotaNombreDraft: String = "",
    val mascotaTipoDraft: TipoMascota? = null,
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorTelefono: String? = null,
    val errorPassword: String? = null,
    val errorConfirm: String? = null,
    val errorMascotaNombre: String? = null,
    val errorMascotaTipo: String? = null,

    val cargando: Boolean = false,
    val exito: Boolean = false,
    val errorGeneral: String? = null
) {
    val tieneErrores: Boolean
        get() = listOf(
            errorNombre, errorEmail, errorTelefono, errorPassword,
            errorConfirm, errorMascotaNombre, errorMascotaTipo, errorGeneral
        ).any { it != null }

    val cantidadMascotas: Int get() = mascotas.size

    override fun toString(): String =
        "RegistroUiState(nombre=$nombre, email=$email, telefono=$telefono, mascotas=${mascotas.size}, cargando=$cargando, exito=$exito, errorGeneral=$errorGeneral)"
}