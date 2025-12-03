package com.example.appsindempart_grupo14.viewmodel

import com.example.appsindempart_grupo14.model.Mascota

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val mascotas: List<Mascota> = emptyList(),
    val mascotaNombreDraft: String = "",
    val mascotaTipoDraft: String? = null,
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorTelefono: String? = null,
    val errorPassword: String? = null,
    val errorConfirm: String? = null,
    val errorGeneral: String? = null,
    val cargando: Boolean = false,
    val exito: Boolean = false
)
