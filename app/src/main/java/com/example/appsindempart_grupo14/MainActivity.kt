package com.example.appsindempart_grupo14

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.appsindempart_grupo14.model.DatabaseProvider
import com.example.appsindempart_grupo14.repository.*
import com.example.appsindempart_grupo14.ui.*
import com.example.appsindempart_grupo14.ui.admin.AdminDogPanelScreen
import com.example.appsindempart_grupo14.ui.admin.AdminReservasScreen
import com.example.appsindempart_grupo14.ui.theme.AppSindempart_Grupo14Theme
import com.example.appsindempart_grupo14.viewmodel.LoginViewModel
import com.example.appsindempart_grupo14.viewmodel.RegistroViewModel
import com.example.appsindempart_grupo14.viewmodel.admin.AdminReservasViewModel
import kotlinx.coroutines.launch

sealed class Screen {
    object Inicio : Screen()
    object Register : Screen()
    object Login : Screen()
    object Agenda : Screen()
    object Reservas : Screen()
    object PerfilUsuario : Screen()
    object PerfilEspecialista : Screen()
    object AdminPanel : Screen()
    object AdminReservas : Screen()
}

class MainActivity : ComponentActivity() {

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val session = SessionManager(this)

        setContent {

            // Crear admin inicial
            LaunchedEffect(Unit) {
                AdminSeeder.createAdminIfNeeded(db)
            }

            AppSindempart_Grupo14Theme {

                Surface(color = MaterialTheme.colorScheme.background) {

                    val authRepo = remember { RoomAuthRepository(db) }
                    val reservaRepo = remember { RoomReservaRepository(db) }
                    val especRepo = remember { InMemoryEspecialistaRepository() }

                    var current by remember { mutableStateOf<Screen>(Screen.Inicio) }
                    var emailUsuario by remember { mutableStateOf<String?>(null) }

                    val emailGuardado by session.emailUsuario.collectAsState(initial = null)

                    //autologin
                    LaunchedEffect(emailGuardado) {
                        if (emailGuardado != null && emailUsuario == null) {

                            emailUsuario = emailGuardado

                            val userDB = authRepo.getUsuario(emailGuardado!!)

                            current = if (userDB?.rol == "admin") {
                                Screen.AdminPanel
                            } else {
                                Screen.Agenda
                            }
                        }
                    }

                    fun cerrarSesion() {
                        lifecycleScope.launch { session.cerrarSesion() }
                        emailUsuario = null
                        current = Screen.Inicio
                    }

                    val especialistaNombre = "Macarena Zapata"

                    when (current) {

                        //Inicio
                        is Screen.Inicio -> InicioScreen(
                            onCrearCuenta = { current = Screen.Register },
                            onIngresar = { current = Screen.Login },
                            onProfesional = { current = Screen.Login },
                            onVerPerfil = { current = Screen.PerfilEspecialista }
                        )

                        //Registro
                        is Screen.Register -> {
                            val vm = remember { RegistroViewModel(authRepo) }

                            RegisterScreen(
                                viewModel = vm,
                                onVolver = { current = Screen.Inicio },
                                onSuccess = { email ->

                                    emailUsuario = email

                                    lifecycleScope.launch {
                                        session.guardarSesion(email)

                                        val userDB = authRepo.getUsuario(email)

                                        current = if (userDB?.rol == "admin") {
                                            Screen.AdminPanel
                                        } else {
                                            Screen.Agenda
                                        }
                                    }
                                }
                            )
                        }

                        //login
                        is Screen.Login -> {
                            val vm = remember { LoginViewModel(authRepo) }

                            LoginScreen(
                                viewModel = vm,
                                onVolver = { current = Screen.Inicio },
                                onSuccess = { email ->

                                    emailUsuario = email

                                    lifecycleScope.launch {
                                        session.guardarSesion(email)

                                        val userDB = authRepo.getUsuario(email)

                                        current = if (userDB?.rol == "admin") {
                                            Screen.AdminPanel
                                        } else {
                                            Screen.Agenda
                                        }
                                    }
                                }
                            )
                        }

                        //agendar
                        is Screen.Agenda -> AgendaScreen(
                            emailUsuario = emailUsuario ?: return@Surface,
                            reservaRepo = reservaRepo,
                            onVerReservas = { current = Screen.Reservas },
                            onVolver = { cerrarSesion() },
                            onCerrarSesion = { cerrarSesion() }
                        )

                        //reservas
                        is Screen.Reservas -> MisReservasScreen(
                            emailUsuario = emailUsuario ?: return@Surface,
                            reservaRepo = reservaRepo,
                            onNuevaReserva = { current = Screen.Agenda },
                            onVolver = { current = Screen.Agenda },
                            onCerrarSesion = { cerrarSesion() }
                        )

                        //perfilusuario

                        is Screen.PerfilUsuario -> {

                            val email = emailUsuario ?: run {
                                current = Screen.Login
                                return@Surface
                            }

                            UserProfileScreen(
                                authRepo = authRepo,
                                emailUsuario = email,
                                onVolver = { current = Screen.Agenda },
                                onEliminado = { cerrarSesion() },
                                onCerrarSesion = { cerrarSesion() }
                            )
                        }

                        //perfilespecialista

                        is Screen.PerfilEspecialista -> EspecialistaPerfilScreen(
                            especialistaNombre = especialistaNombre,
                            onAgendar = { current = Screen.Agenda },
                            onVolver = { current = Screen.Inicio },
                            onCerrarSesion = { cerrarSesion() }
                        )

                        //paneladmin
                        is Screen.AdminPanel -> AdminDogPanelScreen(
                            onVolver = { current = Screen.Inicio },
                            onVerReservas = { current = Screen.AdminReservas },
                            onCerrarSesion = { cerrarSesion() }
                        )

                        //adminreservas
                        is Screen.AdminReservas -> AdminReservasScreen(
                            viewModel = AdminReservasViewModel(reservaRepo),
                            onVolver = { current = Screen.AdminPanel },
                            onCerrarSesion = { cerrarSesion() }
                        )
                    }
                }
            }
        }
    }
}
