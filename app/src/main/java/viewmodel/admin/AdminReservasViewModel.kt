package com.example.appsindempart_grupo14.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsindempart_grupo14.model.Reserva
import com.example.appsindempart_grupo14.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminReservasUiState(
    val cargando: Boolean = false,
    val reservas: List<Reserva> = emptyList(),
    val filtroEspecialista: String? = null,
    val filtroFecha: String? = null,
    val filtroEstado: String? = null,
    val buscarTelefono: String = "",
    val error: String? = null
)

class AdminReservasViewModel(
    private val repo: ReservaRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(AdminReservasUiState())
    val ui: StateFlow<AdminReservasUiState> = _ui

    fun cargar() = viewModelScope.launch {
        _ui.value = _ui.value.copy(cargando = true, error = null)
        try {
            val lista = repo.listarTodas()
            _ui.value = _ui.value.copy(cargando = false, reservas = lista)
        } catch (e: Exception) {
            _ui.value = _ui.value.copy(cargando = false, error = e.message)
        }
    }

    fun cancelarReserva(id: String) = viewModelScope.launch {
        repo.cancelarReserva(id)
        cargar()
    }


    fun editarFechaHora(id: String, nuevaFecha: String, nuevaHora: String) =
        viewModelScope.launch {

            _ui.value = _ui.value.copy(cargando = true)

            try {
                val reservaActual = _ui.value.reservas.firstOrNull { it.id == id }
                    ?: throw Exception("Reserva no encontrada")

                val r = reservaActual.copy(
                    fecha = nuevaFecha,
                    hora = nuevaHora
                )

                repo.modificarReserva(r)
                cargar()

            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    cargando = false,
                    error = e.message ?: "Error editando reserva"
                )
            }
        }
}
