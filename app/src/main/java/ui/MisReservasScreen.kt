package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.appsindempart_grupo14.model.Reserva
import com.example.appsindempart_grupo14.repository.ReservaRepository

@Composable
fun MisReservasScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onNuevaReserva: () -> Unit = {},
    onVolver: () -> Unit = {},
    emailUsuario: String,
    reservaRepo: ReservaRepository
) {
    val scope = rememberCoroutineScope()

    var reservas by remember { mutableStateOf(emptyList<Reserva>()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // ---- Estados para editar ----
    var showEditDialog by remember { mutableStateOf(false) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var editFecha by remember { mutableStateOf("") }
    var editHora by remember { mutableStateOf("") }

    fun cargar() {
        scope.launch {
            cargando = true
            error = null
            runCatching { reservaRepo.listarReservas(emailUsuario) }
                .onSuccess { reservas = it }
                .onFailure { error = it.message ?: "Error al cargar reservas" }
            cargando = false
        }
    }

    LaunchedEffect(emailUsuario) { cargar() }


    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Modificar reserva") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editFecha,
                        onValueChange = { editFecha = it },
                        label = { Text("Fecha (YYYY-MM-DD)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editHora,
                        onValueChange = { editHora = it },
                        label = { Text("Hora (HH:MM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "Solo puedes modificar reservas en estado 'Próxima'.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val id = editingId
                    if (id != null) {
                        val original = reservas.firstOrNull { it.id == id }
                        if (original != null && original.estado == "Próxima") {
                            scope.launch {
                                val actualizado = original.copy(
                                    fecha = editFecha.trim(),
                                    hora = editHora.trim()
                                )
                                val res = reservaRepo.modificarReserva(actualizado)
                                if (res.isSuccess) {
                                    showEditDialog = false
                                    cargar()
                                }
                            }
                        } else showEditDialog = false
                    } else showEditDialog = false
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }


    LazyColumn(
        modifier = modifier
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onVolver) { Text("← Volver") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { cargar() }, enabled = !cargando) { Text("Actualizar") }
                    Button(onClick = onNuevaReserva) { Text("Nueva reserva") }
                }
            }
        }


        when {
            cargando -> {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Cargando reservas…")
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                }
            }
            error != null -> {
                item { Text(error!!, color = MaterialTheme.colorScheme.error) }
            }
            reservas.isEmpty() -> {
                item { Text("Aún no tienes reservas.") }
            }
        }


        items(reservas, key = { it.id }) { r ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("🐾 ${r.mascotaNombre} (${r.mascota})")
                    Text("Edad: ${r.mascotaEdad} años • Peso: ${r.mascotaPeso} kg")
                    Text("Raza: ${r.mascotaRaza}")
                    Text("Atención: ${r.tipoAtencion}")
                    Text("Especialista: ${r.especialista}")
                    Text("📅 ${r.fecha} a las ${r.hora}")
                    Text("Estado: ${r.estado}")

                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        Button(
                            onClick = {
                                editingId = r.id
                                editFecha = r.fecha
                                editHora = r.hora
                                showEditDialog = true
                            },
                            enabled = r.estado == "Próxima"
                        ) { Text("Modificar") }


                        Button(
                            onClick = {
                                scope.launch {
                                    reservaRepo.cancelarReserva(r.id)
                                    cargar()
                                }
                            },
                            enabled = r.estado == "Próxima"
                        ) { Text("Cancelar") }
                    }
                }
            }
        }

        item { Divider() }
        item { Spacer(Modifier.height(40.dp)) }
    }
}