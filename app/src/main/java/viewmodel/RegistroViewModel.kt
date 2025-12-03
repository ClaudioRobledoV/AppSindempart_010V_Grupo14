package com.example.appsindempart_grupo14.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsindempart_grupo14.model.Usuario
import com.example.appsindempart_grupo14.model.Validacion
import com.example.appsindempart_grupo14.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RegistroUiState())
    val ui: StateFlow<RegistroUiState> = _ui

    fun onChangeNombre(v: String) {
        _ui.value = _ui.value.copy(nombre = v, errorNombre = null)
    }

    fun onChangeEmail(v: String) {
        _ui.value = _ui.value.copy(email = v, errorEmail = null)
    }

    fun onChangeTelefono(v: String) {
        _ui.value = _ui.value.copy(telefono = v, errorTelefono = null)
    }

    fun onChangePass(v: String) {
        _ui.value = _ui.value.copy(password = v, errorPassword = null)
    }

    fun onChangeConfirm(v: String) {
        _ui.value = _ui.value.copy(confirmPassword = v, errorConfirm = null)
    }

    fun registrar() = viewModelScope.launch {
        var st = _ui.value

        val eNombre  = Validacion.nombreValido(st.nombre)
        val eEmail   = Validacion.emailValido(st.email)
        val ePass    = Validacion.passwordValida(st.password)
        val eConfirm = Validacion.confirmarPassword(st.password, st.confirmPassword)
        val eTel     = Validacion.telefonoValido(st.telefono.ifBlank { null })

        st = st.copy(
            errorNombre = eNombre,
            errorEmail = eEmail,
            errorPassword = ePass,
            errorConfirm = eConfirm,
            errorTelefono = eTel,
            errorGeneral = null
        )
        _ui.value = st


        if (listOf(eNombre, eEmail, ePass, eConfirm, eTel).any { it != null }) return@launch
        _ui.value = st.copy(cargando = true)


        val emailUnico = repo.esEmailUnico(st.email)
        if (!emailUnico) {
            _ui.value = st.copy(
                cargando = false,
                errorEmail = "El correo ya existe"
            )
            return@launch
        }


        val rolAsignado =
            if (st.email.trim().endsWith("@admin.com")) "admin"
            else "cliente"


        val usuario = Usuario(
            nombreCompleto = st.nombre.trim(),
            email = st.email.trim(),
            telefono = st.telefono.ifBlank { null },
            hashedPassword = "hash(${st.password})",
            rol = rolAsignado,
            mascotas = emptyList()
        )

        val res = repo.registrar(usuario)

        _ui.value = if (res.isSuccess) {
            st.copy(cargando = false, exito = true)
        } else {
            st.copy(
                cargando = false,
                errorGeneral = res.exceptionOrNull()?.message
            )
        }
    }
}
