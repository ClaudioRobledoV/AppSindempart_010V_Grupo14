package com.example.appsindempart_grupo14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.appsindempart_grupo14.repository.InMemoryAuthRepository
import com.example.appsindempart_grupo14.repository.InMemoryEspecialistaRepository
import com.example.appsindempart_grupo14.repository.InMemoryReservaRepository
import com.example.appsindempart_grupo14.ui.*
import com.example.appsindempart_grupo14.ui.theme.AppSindempart_Grupo14Theme

// Rutas de navegación
sealed class Screen {
    object Inicio : Screen()
    object Register : Screen()
    object Login : Screen()
    object Agenda : Screen()
    object Reservas : Screen()
    object Profesional : Screen()
    object PerfilUsuario : Screen()
    object AdminEspecialistas : Screen()
    object PerfilEspecialista : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSindempart_Grupo14Theme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Repositorios únicos compartidos en toda la app ✅
                    val authRepo = remember { InMemoryAuthRepository() }
                    val reservaRepo = remember { InMemoryReservaRepository() }
                    val especRepo = remember { InMemoryEspecialistaRepository() }

                    // Estado de navegación y sesión
                    var current by remember { mutableStateOf<Screen>(Screen.Inicio) }
                    var emailUsuario by remember { mutableStateOf<String?>(null) }
                    val especialistaNombre = "Macarena Zapata"

                    when (current) {
                        is Screen.Inicio -> InicioScreen(
                            onCrearCuenta = { current = Screen.Register },
                            onIngresar = { current = Screen.Login },
                            onProfesional = { current = Screen.Profesional },
                            onVerPerfil = { current = Screen.PerfilEspecialista }
                        )

                        // Registro de usuario (pasa authRepo) ✅
                        is Screen.Register -> RegisterScreen(
                            authRepo = authRepo,
                            onSuccess = { email ->
                                emailUsuario = email
                                current = Screen.Agenda
                            }
                        )

                        // Login real (mismo repo) ✅
                        is Screen.Login -> LoginScreen(
                            authRepo = authRepo,
                            onVolver = { current = Screen.Inicio },
                            onSuccess = { email ->
                                emailUsuario = email
                                current = Screen.Agenda
                            }
                        )

                        // Crear reserva
                        is Screen.Agenda -> AgendaScreen(
                            emailUsuario = emailUsuario ?: "usuario@demo.com", // fallback para pruebas
                            reservaRepo = reservaRepo,
                            onVerReservas = { current = Screen.Reservas },
                            onVolver = { current = Screen.Inicio }
                        )

                        // Reservas del usuario
                        is Screen.Reservas -> MisReservasScreen(
                            emailUsuario = emailUsuario ?: "usuario@demo.com",
                            reservaRepo = reservaRepo,
                            onNuevaReserva = { current = Screen.Agenda },
                            onVolver = { current = Screen.Agenda }
                        )

                        // Rol profesional
                        is Screen.Profesional -> ProfesionalScreen(
                            reservaRepo = reservaRepo,
                            especialistaNombre = especialistaNombre,
                            onVolver = { current = Screen.Inicio }
                        )

                        // Perfil del usuario (opcional)
                        is Screen.PerfilUsuario -> {
                            val email = emailUsuario
                            if (email == null) {
                                current = Screen.Login
                            } else {
                                UserProfileScreen(
                                    authRepo = authRepo,
                                    emailUsuario = email,
                                    onVolver = { current = Screen.Inicio },
                                    onEliminado = {
                                        emailUsuario = null
                                        current = Screen.Inicio
                                    }
                                )
                            }
                        }

                        // Admin de especialista único (opcional)
                        is Screen.AdminEspecialistas -> EspecialistasAdminScreen(
                            repo = especRepo,
                            onVolver = { current = Screen.Inicio }
                        )

                        // Perfil público del especialista
                        is Screen.PerfilEspecialista -> EspecialistaPerfilScreen(
                            especialistaNombre = especialistaNombre,
                            onAgendar = { current = Screen.Agenda },
                            onVolver = { current = Screen.Inicio }
                        )
                    }
                }
            }
        }
    }
}