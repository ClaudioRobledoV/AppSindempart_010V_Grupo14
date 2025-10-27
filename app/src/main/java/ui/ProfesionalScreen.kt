package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appsindempart_grupo14.model.Reserva
import com.example.appsindempart_grupo14.repository.ReservaRepository
import kotlinx.coroutines.launch

@Composable
fun ProfesionalScreen(
    reservaRepo: ReservaRepository,
    especialistaNombre: String = "Macarena Zapata",
    onVolver: () -> Unit = {}
) {
    var reservas by remember { mutableStateOf(emptyList<Reserva>()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun cargar() {
        scope.launch {
            cargando = true
            error = null
            runCatching { reservaRepo.listarReservasPorEspecialista(especialistaNombre) }
                .onSuccess { reservas = it }
                .onFailure { error = it.message ?: "Error al cargar" }
            cargando = false
        }
    }

    LaunchedEffect(Unit) { cargar() }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding() // 👈 evita que el teclado tape botones/inputs si los agregas
    ) {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onVolver) { Text("← Volver") }
                Text(
                    "Agenda de $especialistaNombre",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = ::cargar, enabled = !cargando) { Text("Actualizar") }
            }
        }

        Spacer(Modifier.height(8.dp))


        when {
            cargando -> {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Cargando…")
                    CircularProgressIndicator(strokeWidth = 2.dp)
                }
                Spacer(Modifier.height(8.dp))
            }
            error != null -> {
                Text(error!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }
            reservas.isEmpty() -> {
                Text("No hay reservas registradas.")
                Spacer(Modifier.height(8.dp))
            }
        }


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 40.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            val proximas = reservas.filter { it.estado == "Próxima" }
            if (proximas.isNotEmpty()) {
                item { Text("Próximas", style = MaterialTheme.typography.titleSmall) }
                items(proximas, key = { it.id }) { r ->
                    ReservaCardProfesional(
                        r = r,
                        onCompletar = {
                            scope.launch {
                                val res = reservaRepo.actualizarEstado(r.id, "Completada")
                                if (res.isSuccess) cargar()
                            }
                        },
                        onCancelar = {
                            scope.launch {
                                val res = reservaRepo.cancelarReserva(r.id)
                                if (res.isSuccess) cargar()
                            }
                        }
                    )
                }
                item { Divider() }
            }


            val completadas = reservas.filter { it.estado == "Completada" }
            if (completadas.isNotEmpty()) {
                item { Text("Completadas", style = MaterialTheme.typography.titleSmall) }
                items(completadas, key = { it.id }) { r ->
                    ReservaCardSimple(r)
                }
                item { Divider() }
            }


            val canceladas = reservas.filter { it.estado == "Cancelada" }
            if (canceladas.isNotEmpty()) {
                item { Text("Canceladas", style = MaterialTheme.typography.titleSmall) }
                items(canceladas, key = { it.id }) { r ->
                    ReservaCardSimple(r)
                }
            }
        }
    }
}

@Composable
private fun ReservaCardProfesional(
    r: Reserva,
    onCompletar: () -> Unit,
    onCancelar: () -> Unit
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            // Tipo y atención
            Text("${r.mascota} • ${r.tipoAtencion}")
            // 👉 Detalle completo de la mascota
            Text("Mascota: ${r.mascotaNombre} • ${r.mascotaEdad} años • ${r.mascotaPeso} kg • ${r.mascotaRaza}")
            // Cliente y cita
            Text("Paciente: ${r.emailUsuario}")
            Text("${r.fecha} a las ${r.hora}")
            Text("Estado: ${r.estado}")

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onCompletar, enabled = r.estado == "Próxima") {
                    Text("Marcar como Completada")
                }
                Button(onClick = onCancelar, enabled = r.estado == "Próxima") {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
private fun ReservaCardSimple(r: Reserva) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text("${r.mascota} • ${r.tipoAtencion}")
            Text("Mascota: ${r.mascotaNombre} • ${r.mascotaEdad} años • ${r.mascotaPeso} kg • ${r.mascotaRaza}")
            Text("${r.fecha} a las ${r.hora}")
            Text("Estado: ${r.estado}")
        }
    }
}