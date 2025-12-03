package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appsindempart_grupo14.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onVolver: () -> Unit = {},
    onSuccess: (String) -> Unit
) {
    val ui by viewModel.ui.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(ui.exito) {
        if (ui.exito) {
            onSuccess(ui.email)
        }
    }

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

            // EMAIL
            OutlinedTextField(
                value = ui.email,
                onValueChange = viewModel::onChangeEmail,
                label = { Text("Correo") },
                isError = ui.errorEmail != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorEmail?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            // PASSWORD
            OutlinedTextField(
                value = ui.password,
                onValueChange = viewModel::onChangePassword,
                label = { Text("Contraseña") },
                isError = ui.errorPassword != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorPassword?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }


            Button(
                onClick = {
                    scope.launch { viewModel.login() }
                },
                enabled = !ui.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ui.cargando) "Ingresando…" else "Ingresar")
            }
            ui.errorGeneral?.let {
                LaunchedEffect(it) {
                    snack.showSnackbar(it)
                }
            }
        }
    }
}
