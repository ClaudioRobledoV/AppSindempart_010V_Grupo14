package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsindempart_grupo14.R

@Composable
fun EspecialistaPerfilScreen(
    especialistaNombre: String,
    onVolver: () -> Unit = {},
    onAgendar: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}     // ⭐ NUEVO
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onVolver) { Text("← Volver") }
            Text(
                text = "Perfil del Especialista",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(40.dp))
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.maca_perfil),
                contentDescription = "Foto de Macarena Zapata",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                especialistaNombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Médica Veterinaria — Atención General y Control",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }


        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Sobre Macarena",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Profesional con más de 8 años de experiencia en atención de animales domésticos. " +
                            "Especialista en control preventivo, vacunación, cuidados post operatorios " +
                            "y asesoramiento nutricional.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }
        }


        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Servicios disponibles",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("• Consulta general")
                Text("• Control de salud")
                Text("• Vacunación y desparasitación")
                Text("• Atención de urgencias menores")
            }
        }


        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Horarios de atención",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("Lunes a Viernes: 09:00 a 18:00 hrs")
                Text("Sábados: 10:00 a 14:00 hrs")
            }
        }


        Button(
            onClick = onAgendar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Agendar cita con Macarena")
        }


        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
        ) {
            Text("Cerrar sesión")
        }
    }
}
