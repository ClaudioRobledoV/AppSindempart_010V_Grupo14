package com.example.appsindempart_grupo14.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsindempart_grupo14.model.Usuario
import com.example.appsindempart_grupo14.model.Validacion
import com.example.appsindempart_grupo14.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onChangeEmail(v: String) {
        _ui.value = _ui.value.copy(email = v, errorEmail = null)
    }

    fun onChangePassword(v: String) {
        _ui.value = _ui.value.copy(password = v, errorPassword = null)
    }

    fun login() = viewModelScope.launch {
        var st = _ui.value
        val eEmail = Validacion.emailValido(st.email)
        val ePass  = Validacion.passwordValida(st.password)

        st = st.copy(
            errorEmail = eEmail,
            errorPassword = ePass,
            errorGeneral = null
        )
        _ui.value = st


        if (listOf(eEmail, ePass).any { it != null }) return@launch


        _ui.value = st.copy(cargando = true)

        val result = repo.login(st.email, st.password)

        if (result.isFailure) {
            _ui.value = st.copy(
                cargando = false,
                errorGeneral = result.exceptionOrNull()?.message
                    ?: "Credenciales incorrectas"
            )
            return@launch
        }

        val usuario: Usuario? = repo.getUsuario(st.email)

        _ui.value = st.copy(
            cargando = false,
            exito = true,
            usuario = usuario
        )
    }
}
