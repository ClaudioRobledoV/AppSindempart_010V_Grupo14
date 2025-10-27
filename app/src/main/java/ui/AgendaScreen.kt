package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsindempart_grupo14.model.TipoMascota
import com.example.appsindempart_grupo14.model.Reserva
import com.example.appsindempart_grupo14.repository.ReservaRepository
import kotlinx.coroutines.launch

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onVerReservas: () -> Unit = {},
    onVolver: () -> Unit = {},
    emailUsuario: String,
    reservaRepo: ReservaRepository
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var tipoMascota by rememberSaveable { mutableStateOf<TipoMascota?>(null) }
    var mascotaNombre by rememberSaveable { mutableStateOf("") }
    var mascotaEdad by rememberSaveable { mutableStateOf("") }   // guardamos como texto, convertimos al guardar
    var mascotaPeso by rememberSaveable { mutableStateOf("") }   // idem
    var mascotaRaza by rememberSaveable { mutableStateOf("") }
    var tipoAtencion by rememberSaveable { mutableStateOf<String?>(null) }
    var fecha by rememberSaveable { mutableStateOf("") }
    var hora by rememberSaveable { mutableStateOf("") }
    val especialistaFijo = "Macarena Zapata"
    val tiposAtencion = listOf("Consulta general", "Urgencia menor", "Vacunación", "Control")
    var errTipoMascota by rememberSaveable { mutableStateOf<String?>(null) }
    var errNombre by rememberSaveable { mutableStateOf<String?>(null) }
    var errEdad by rememberSaveable { mutableStateOf<String?>(null) }
    var errPeso by rememberSaveable { mutableStateOf<String?>(null) }
    var errRaza by rememberSaveable { mutableStateOf<String?>(null) }
    var errTipoAtencion by rememberSaveable { mutableStateOf<String?>(null) }
    var errFecha by rememberSaveable { mutableStateOf<String?>(null) }
    var errHora by rememberSaveable { mutableStateOf<String?>(null) }
    var cargando by rememberSaveable { mutableStateOf(false) }
    var exito by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) }
    ) { inner ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(inner)
                .fillMaxSize()
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onVolver) { Text("← Volver") }
                    Text("Agenda de atención", fontSize = 20.sp)
                    Spacer(Modifier.width(1.dp))
                }
            }


            item {
                OutlinedTextField(
                    value = especialistaFijo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Especialista") },
                    modifier = Modifier.fillMaxWidth()
                )
            }


            item {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = tipoMascota?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de mascota") },
                    isError = errTipoMascota != null,
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = { expanded = true }) {
                    Text(if (tipoMascota == null) "Seleccionar" else "Cambiar tipo")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    TipoMascota.values().forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t.name) },
                            onClick = { tipoMascota = t; errTipoMascota = null; expanded = false }
                        )
                    }
                }
                errTipoMascota?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                OutlinedTextField(
                    value = mascotaNombre,
                    onValueChange = { mascotaNombre = it; errNombre = null },
                    label = { Text("Nombre de la mascota") },
                    singleLine = true,
                    isError = errNombre != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                errNombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                OutlinedTextField(
                    value = mascotaEdad,
                    onValueChange = { mascotaEdad = it; errEdad = null },
                    label = { Text("Edad (años)") },
                    singleLine = true,
                    isError = errEdad != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                errEdad?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                OutlinedTextField(
                    value = mascotaPeso,
                    onValueChange = { mascotaPeso = it; errPeso = null },
                    label = { Text("Peso (kg)") },
                    singleLine = true,
                    isError = errPeso != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                errPeso?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                OutlinedTextField(
                    value = mascotaRaza,
                    onValueChange = { mascotaRaza = it; errRaza = null },
                    label = { Text("Raza (ej.: Quiltro o específica)") },
                    singleLine = true,
                    isError = errRaza != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                errRaza?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = tipoAtencion ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de atención") },
                    isError = errTipoAtencion != null,
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = { expanded = true }) {
                    Text(if (tipoAtencion == null) "Seleccionar" else "Cambiar tipo")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    tiposAtencion.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t) },
                            onClick = { tipoAtencion = t; errTipoAtencion = null; expanded = false }
                        )
                    }
                }
                errTipoAtencion?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it; errFecha = null },
                    label = { Text("Fecha (YYYY-MM-DD)") },
                    singleLine = true,
                    isError = errFecha != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                errFecha?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }


            item {
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it; errHora = null },
                    label = { Text("Hora (HH:MM)") },
                    singleLine = true,
                    isError = errHora != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                errHora?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }

            item { Divider() }


            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {

                            errTipoMascota = if (tipoMascota == null) "Obligatorio" else null
                            errNombre = if (mascotaNombre.isBlank()) "Obligatorio" else null

                            val edadInt = mascotaEdad.toIntOrNull()
                            errEdad = when {
                                mascotaEdad.isBlank() -> "Obligatorio"
                                edadInt == null || edadInt < 0 || edadInt > 40 -> "Edad inválida"
                                else -> null
                            }

                            val pesoDouble = mascotaPeso.replace(",", ".").toDoubleOrNull()
                            errPeso = when {
                                mascotaPeso.isBlank() -> "Obligatorio"
                                pesoDouble == null || pesoDouble <= 0.0 || pesoDouble > 120.0 -> "Peso inválido"
                                else -> null
                            }

                            errRaza = if (mascotaRaza.isBlank()) "Obligatorio" else null
                            errTipoAtencion = if (tipoAtencion.isNullOrBlank()) "Obligatorio" else null
                            errFecha = if (fecha.isBlank()) "Obligatorio" else null
                            errHora = if (hora.isBlank()) "Obligatorio" else null

                            val hayErrores = listOf(
                                errTipoMascota, errNombre, errEdad, errPeso,
                                errRaza, errTipoAtencion, errFecha, errHora
                            ).any { it != null }

                            if (hayErrores) return@Button

                            cargando = true
                            exito = false

                            val nueva = Reserva(
                                id = System.currentTimeMillis().toString(),
                                emailUsuario = emailUsuario,
                                mascota = tipoMascota!!.name,
                                mascotaNombre = mascotaNombre.trim(),
                                mascotaEdad = edadInt!!,
                                mascotaPeso = pesoDouble!!,
                                mascotaRaza = mascotaRaza.trim(),
                                especialista = especialistaFijo,
                                tipoAtencion = tipoAtencion!!,
                                fecha = fecha.trim(),
                                hora = hora.trim()
                            )

                            scope.launch {
                                val res = reservaRepo.crearReserva(nueva)
                                cargando = false
                                if (res.isSuccess) {
                                    exito = true
                                    snackbar.showSnackbar("Reserva creada con éxito")

                                } else {
                                    snackbar.showSnackbar(res.exceptionOrNull()?.message ?: "Error al crear la reserva")
                                }
                            }
                        }
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                            Text("Agendando…")
                        } else {
                            Text("Agendar")
                        }
                    }

                    Button(onClick = onVerReservas) { Text("Mis reservas") }
                }
            }

            if (exito) {
                item {
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text("✅ Reserva creada")
                            Text("Mascota: $mascotaNombre (${tipoMascota?.name})")
                            Text("Especialista: $especialistaFijo")
                            Text("Atención: $tipoAtencion")
                            Text("Fecha: $fecha • Hora: $hora")
                            Text("Edad: ${mascotaEdad} años • Peso: ${mascotaPeso} kg • Raza: $mascotaRaza")
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}