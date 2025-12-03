package com.example.appsindempart_grupo14.viewmodel.admin

data class AdminDogUiState(
    val loading: Boolean = false,
    val images: List<String> = emptyList(),
    val error: String? = null
)