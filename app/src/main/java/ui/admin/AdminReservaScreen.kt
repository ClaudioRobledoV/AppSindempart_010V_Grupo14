package com.example.appsindempart_grupo14.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appsindempart_grupo14.viewmodel.admin.AdminReservasViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun AdminReservasScreen(
    viewModel: AdminReservasViewModel,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val ui by viewModel.ui.collectAsState()


    LaunchedEffect(Unit) { viewModel.cargar() }


    var reservaEditando by remember { mutableStateOf<String?>(null) }

    if (reservaEditando != null) {
        EditarFechaHoraDialog(
            reservaId = reservaEditando!!,
            onDismiss = { reservaEditando = null },
            onConfirm = { nuevaFecha, nuevaHora ->
                viewModel.editarFechaHora(reservaEditando!!, nuevaFecha, nuevaHora)
                reservaEditando = null
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Administración de Reservas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Volver al panel")
        }

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(20.dp))


        when {
            ui.cargando -> {
                CircularProgressIndicator()
            }

            ui.error != null -> {
                Text(
                    ui.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ui.reservas) { r ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {

                                Text("Usuario: ${r.emailUsuario}")
                                Text("Mascota: ${r.mascotaNombre}")
                                Text("Fecha: ${r.fecha}")
                                Text("Hora: ${r.hora}")
                                Text("Estado: ${r.estado}")

                                Spacer(Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    Button(
                                        onClick = { reservaEditando = r.id }
                                    ) { Text("Editar") }

                                    Button(
                                        onClick = { viewModel.cancelarReserva(r.id) }
                                    ) { Text("Cancelar") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
