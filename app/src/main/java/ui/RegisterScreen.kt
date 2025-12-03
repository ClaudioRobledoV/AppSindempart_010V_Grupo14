package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appsindempart_grupo14.viewmodel.RegistroViewModel

@Composable
fun RegisterScreen(
    viewModel: RegistroViewModel,
    onVolver: () -> Unit = {},
    onSuccess: (String) -> Unit
) {
    val ui by viewModel.ui.collectAsState()


    LaunchedEffect(ui.exito) {
        if (ui.exito) {
            onSuccess(ui.email)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {


        item {
            TextButton(onClick = onVolver) {
                Text("← Volver")
            }
        }


        item {
            Text("Crear cuenta", style = MaterialTheme.typography.titleLarge)
        }


        item {
            OutlinedTextField(
                value = ui.nombre,
                onValueChange = viewModel::onChangeNombre,
                label = { Text("Nombre completo") },
                isError = ui.errorNombre != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorNombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }


        item {
            OutlinedTextField(
                value = ui.email,
                onValueChange = viewModel::onChangeEmail,
                label = { Text("Correo electrónico") },
                isError = ui.errorEmail != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorEmail?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }


        item {
            OutlinedTextField(
                value = ui.telefono,
                onValueChange = viewModel::onChangeTelefono,
                label = { Text("Teléfono (opcional)") },
                isError = ui.errorTelefono != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorTelefono?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }


        item {
            OutlinedTextField(
                value = ui.password,
                onValueChange = viewModel::onChangePass,
                label = { Text("Contraseña") },
                isError = ui.errorPassword != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorPassword?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }

        item {
            OutlinedTextField(
                value = ui.confirmPassword,
                onValueChange = viewModel::onChangeConfirm,
                label = { Text("Confirmar contraseña") },
                isError = ui.errorConfirm != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
            ui.errorConfirm?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }


        item {
            Button(
                onClick = viewModel::registrar,
                enabled = !ui.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ui.cargando) "Registrando…" else "Crear cuenta")
            }

            ui.errorGeneral?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
