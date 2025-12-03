package com.example.appsindempart_grupo14.viewmodel

import com.example.appsindempart_grupo14.model.Usuario

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val exito: Boolean = false,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorGeneral: String? = null,
    val usuario: Usuario? = null
)