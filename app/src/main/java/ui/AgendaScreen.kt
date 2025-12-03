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
import com.example.appsindempart_grupo14.model.Reserva
import com.example.appsindempart_grupo14.repository.ReservaRepository
import kotlinx.coroutines.launch

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onVerReservas: () -> Unit = {},
    onVolver: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},    // ⭐ CORRECTO
    emailUsuario: String,
    reservaRepo: ReservaRepository
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var tipoMascota by rememberSaveable { mutableStateOf<String?>(null) }
    var mascotaNombre by rememberSaveable { mutableStateOf("") }
    var mascotaEdad by rememberSaveable { mutableStateOf("") }
    var mascotaPeso by rememberSaveable { mutableStateOf("") }
    var mascotaRaza by rememberSaveable { mutableStateOf("") }
    var tipoAtencion by rememberSaveable { mutableStateOf<String?>(null) }
    var fecha by rememberSaveable { mutableStateOf("") }
    var hora by rememberSaveable { mutableStateOf("") }

    val especialistaFijo = "Macarena Zapata"

    val tiposMascota = listOf("Gato", "Perro")
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

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { inner ->
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
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(onClick = onVolver) {
                        Text("← Volver")
                    }

                    Text(
                        "Agenda de atención",
                        fontSize = 20.sp
                    )

                    Button(
                        onClick = onCerrarSesion,
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text("Cerrar sesión")
                    }
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
                    value = tipoMascota ?: "",
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
                    tiposMascota.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t) },
                            onClick = {
                                tipoMascota = t
                                errTipoMascota = null
                                expanded = false
                            }
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    Button(
                        onClick = {
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

                    Button(onClick = onVerReservas) {
                        Text("Mis reservas")
                    }
                }
            }

            if (exito) {
                item {
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text("✅ Reserva creada")
                            Text("Mascota: $mascotaNombre (${tipoMascota})")
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
