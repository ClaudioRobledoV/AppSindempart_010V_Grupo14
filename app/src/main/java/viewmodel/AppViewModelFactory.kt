package com.example.appsindempart_grupo14.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appsindempart_grupo14.repository.AuthRepository

class AppViewModelFactory(
    private val repo: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegistroViewModel::class.java) ->
                RegistroViewModel(repo) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(repo) as T

            else -> throw IllegalArgumentException("ViewModel no reconocido")
        }
    }
}
