package com.example.appsindempart_grupo14.model

object Validacion {

    //validarnombre
    fun nombreValido(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre no puede estar vacío"
            nombre.length > 50 -> "Solo letras y espacios (máx. 50)"
            else -> null
        }
    }

    //validaremail
    fun emailValido(email: String): String? {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return if (regex.matches(email)) null else "Correo inválido"
    }

    //validar pw
    fun passwordValida(password: String): String? {
        return if (password.length >= 4) null
        else "Debe tener mínimo 4 caracteres"
    }

    //confirmar pw
    fun confirmarPassword(pass: String, confirm: String): String? {
        return if (pass == confirm) null
        else "Las contraseñas no coinciden"
    }

    //validar telefono
    fun telefonoValido(telefono: String?): String? {
        if (telefono.isNullOrBlank()) return null
        val regex = Regex("^\\+?\\d{8,15}$")
        return if (regex.matches(telefono)) null else "Teléfono inválido"
    }
}
