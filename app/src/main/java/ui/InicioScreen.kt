package com.example.appsindempart_grupo14.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsindempart_grupo14.R

@Composable
fun InicioScreen(
    onCrearCuenta: () -> Unit,
    onIngresar: () -> Unit,
    onProfesional: () -> Unit,
    onVerPerfil: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Sindempart",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Sistema de atención veterinaria y reservas",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(onClick = onCrearCuenta, modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta nueva")
            }

            Button(onClick = onIngresar, modifier = Modifier.fillMaxWidth()) {
                Text("Ingresar con cuenta existente")
            }

            OutlinedButton(onClick = onProfesional, modifier = Modifier.fillMaxWidth()) {
                Text("Soy profesional")
            }

            Button(onClick = onVerPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Ver perfil de Macarena")
            }
        }
    }
}