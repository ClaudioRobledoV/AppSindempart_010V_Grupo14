package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.example.appsindempart_grupo14.repository.RoomAuthRepository

@Composable
fun UserProfileScreen(
    authRepo: RoomAuthRepository,
    emailUsuario: String,
    onVolver: () -> Unit = {},
    onEliminado: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var loaded by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }


    LaunchedEffect(emailUsuario) {
        val u = authRepo.getUsuario(emailUsuario)
        if (u != null) {
            nombre = u.nombreCompleto
            telefono = u.telefono ?: ""
        }
        loaded = true
    }

    if (!loaded) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(snackbarHost = { SnackbarHost(snack) }) { pv ->
        Column(
            Modifier
                .padding(pv)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onVolver) { Text("← Volver") }

                Button(
                    onClick = onCerrarSesion,     // ⭐ CORRECTO
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text("Cerrar sesión")
                }
            }

            Text("Mi perfil", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono (opcional)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    saving = true
                    scope.launch {
                        val actual = authRepo.getUsuario(emailUsuario)
                        if (actual != null) {
                            val actualizado = actual.copy(
                                nombreCompleto = nombre.trim(),
                                telefono = telefono.ifBlank { null }
                            )
                            val res = authRepo.updateUsuario(actualizado)
                            saving = false
                            if (res.isSuccess) snack.showSnackbar("Perfil actualizado")
                            else snack.showSnackbar(res.exceptionOrNull()?.message ?: "Error al actualizar")
                        } else {
                            saving = false
                            snack.showSnackbar("Usuario no encontrado")
                        }
                    }
                },
                enabled = !saving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (saving) "Guardando…" else "Guardar cambios")
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        val res = authRepo.deleteUsuario(emailUsuario)
                        if (res.isSuccess) onEliminado()
                        else snack.showSnackbar("No se pudo eliminar")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar cuenta", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
