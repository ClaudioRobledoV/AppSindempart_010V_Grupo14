package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.appsindempart_grupo14.model.Especialista
import com.example.appsindempart_grupo14.repository.EspecialistaRepository

@Composable
fun EspecialistasAdminScreen(
    repo: EspecialistaRepository,
    onVolver: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var especialista by remember { mutableStateOf<Especialista?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val snack = remember { SnackbarHostState() }

    fun cargar() {
        scope.launch {
            cargando = true
            error = null
            runCatching { repo.listar() }
                .onSuccess { lista ->
                    especialista = lista.firstOrNull()
                }
                .onFailure { error = it.message ?: "Error al cargar" }
            cargando = false
        }
    }

    LaunchedEffect(Unit) { cargar() }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) }
    ) { pv ->
        Column(
            Modifier
                .padding(pv)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onVolver) { Text("← Volver") }
                Button(onClick = ::cargar, enabled = !cargando) { Text("Actualizar") }
            }

            when {
                cargando -> Text("Cargando…")
                error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
                especialista == null -> {
                    Text("No hay especialista configurado.")
                    Button(onClick = {
                        scope.launch {

                            val def = Especialista(
                                id = "1",
                                nombre = "Macarena Zapata",
                                bio = "Médica Veterinaria con 8+ años de experiencia en atención general y control preventivo.",
                                servicios = listOf("Consulta general", "Control de salud", "Vacunación", "Urgencias menores"),
                                horario = "Lun-Vie 09:00–18:00 | Sáb 10:00–14:00"
                            )
                            val res = repo.crear(def)
                            if (res.isSuccess) {
                                snack.showSnackbar("Especialista restaurado")
                                cargar()
                            } else {
                                snack.showSnackbar("No se pudo crear el especialista")
                            }
                        }
                    }) { Text("Restaurar especialista por defecto") }
                }
                else -> {

                    EspecialistaCardEditableSingleton(
                        e = especialista!!,
                        onGuardar = { actualizado ->
                            scope.launch {
                                val res = repo.actualizar(actualizado)
                                if (res.isSuccess) {
                                    especialista = actualizado
                                    snack.showSnackbar("Guardado")
                                } else {
                                    snack.showSnackbar("No se pudo guardar")
                                }
                            }
                        }
                    )
                    Text(
                        text = "Nota: En este modo sólo puedes editar el especialista único. " +
                                "No se permite añadir ni eliminar.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun EspecialistaCardEditableSingleton(
    e: Especialista,
    onGuardar: (Especialista) -> Unit
) {
    var nombre by remember(e.id) { mutableStateOf(e.nombre) }
    var bio by remember(e.id) { mutableStateOf(e.bio) }
    var serviciosText by remember(e.id) { mutableStateOf(e.servicios.joinToString(", ")) }
    var horario by remember(e.id) { mutableStateOf(e.horario) }

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bio, onValueChange = { bio = it },
                label = { Text("Bio") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = serviciosText, onValueChange = { serviciosText = it },
                label = { Text("Servicios (separados por coma)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = horario, onValueChange = { horario = it },
                label = { Text("Horario") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onGuardar(
                        e.copy(
                            nombre = nombre.trim(),
                            bio = bio.trim(),
                            servicios = serviciosText.split(",").map { it.trim() }.filter { it.isNotBlank() },
                            horario = horario.trim()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar cambios") }
        }
    }
}