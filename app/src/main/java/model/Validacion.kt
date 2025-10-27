package com.example.appsindempart_grupo14.model

object Validacion {

    private val nombreRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,50}$")
    private val emailRegex  = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    private val passRegex   = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\\$%]).{8,}$")
    private val fonoRegex   = Regex("^\\+?\\d{8,15}$")

    fun nombreValido(nombre: String): String? =
        when {
            nombre.isBlank() -> "El nombre no puede estar vacío"
            !nombreRegex.matches(nombre) -> "Solo letras y espacios (máx. 50)"
            else -> null
        }

    fun emailValido(email: String): String? =
        when {
            email.isBlank() -> "El correo no puede estar vacío"
            !emailRegex.matches(email) -> "Formato de correo inválido"
            else -> null
        }

    fun passwordValida(pass: String): String? =
        if (!passRegex.matches(pass))
            "Min 8, con mayúscula, minúscula, número y @#$%"
        else null

    fun confirmarPassword(pass: String, confirm: String): String? =
        if (pass != confirm) "Las contraseñas no coinciden" else null


    fun telefonoValido(telefono: String?): String? =
        if (telefono.isNullOrBlank()) null
        else if (!fonoRegex.matches(telefono)) "Teléfono inválido" else null

    fun nombreMascotaValido(nombre: String): String? =
        when {
            nombre.isBlank() -> "El nombre de la mascota es obligatorio"
            nombre.length > 50 -> "Máximo 50 caracteres"
            else -> null
        }
}