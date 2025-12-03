package com.example.appsindempart_grupo14.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appsindempart_grupo14.viewmodel.admin.AdminDogViewModel

@Composable
fun AdminDogPanelScreen(
    viewModel: AdminDogViewModel = viewModel(),
    onVolver: () -> Unit,
    onVerReservas: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val ui by viewModel.ui.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "Panel Administrador",
            style = MaterialTheme.typography.titleLarge
        )


        Button(onClick = onVerReservas, modifier = Modifier.fillMaxWidth()) {
            Text("📋 Ver Reservas del Sistema")
        }

        Button(onClick = { viewModel.loadImages() }, modifier = Modifier.fillMaxWidth()) {
            Text("🐶 Cargar Imágenes Aleatorias de Razas")
        }

        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("← Volver al Inicio")
        }

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
        ) {
            Text("Cerrar sesión")
        }

        when {
            ui.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            ui.error != null -> {
                Text(
                    ui.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            ui.images.isNotEmpty() -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(ui.images) { url ->
                        Card(
                            elevation = CardDefaults.cardElevation(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(url),
                                contentDescription = "Dog",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
