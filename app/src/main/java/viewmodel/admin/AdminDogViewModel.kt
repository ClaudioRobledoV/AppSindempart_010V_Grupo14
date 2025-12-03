package com.example.appsindempart_grupo14.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsindempart_grupo14.network.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminDogViewModel(
    private val repo: DogRepository = DogRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(AdminDogUiState())
    val ui: StateFlow<AdminDogUiState> = _ui

    fun loadImages() = viewModelScope.launch {
        _ui.value = AdminDogUiState(loading = true)

        try {

            val list = repo.obtenerImagenes()

            _ui.value = AdminDogUiState(
                loading = false,
                images = list
            )

        } catch (e: Exception) {
            _ui.value = AdminDogUiState(
                loading = false,
                error = e.message ?: "Error cargando imágenes"
            )
        }
    }
}
