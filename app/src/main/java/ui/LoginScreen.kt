package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appsindempart_grupo14.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authRepo: AuthRepository,
    onVolver: () -> Unit = {},
    onSuccess: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    val snack = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(snack) }) { pv ->
        Column(
            Modifier
                .padding(pv)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onVolver) { Text("← Volver") }
            Text("Ingresar", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Correo") }, singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ), modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pass, onValueChange = { pass = it },
                label = { Text("Contraseña") }, singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ), modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    cargando = true
                    scope.launch {
                        val res = authRepo.login(email.trim(), pass)
                        cargando = false
                        if (res.isSuccess) onSuccess(res.getOrNull()!!.email)
                        else snack.showSnackbar(res.exceptionOrNull()?.message ?: "Error al ingresar")
                    }
                },
                enabled = !cargando,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (cargando) "Ingresando…" else "Ingresar") }
        }
    }
}