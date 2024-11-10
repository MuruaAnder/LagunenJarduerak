import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome_screen") {
        composable("welcome_screen") { WelcomeScreen(navController) }
        composable("main_screen") { MainScreen(navController) }
    }
}



@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Lagunen jarduerak App",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EA)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { navController.navigate("main_screen") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EA),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.5f) // Ancho del 50% de la pantalla
                            .height(48.dp) // Altura del botón
                    ) {
                        Text("Entrar")
                    }
                }
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var actividades by remember { mutableStateOf(listOf<Actividad>()) }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var mostrarFavoritos by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Actividades") },
                actions = {
                    Button(
                        onClick = { mostrarFavoritos = !mostrarFavoritos },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mostrarFavoritos) Color(0xFFFFEB3B) else Color.Gray,
                            contentColor = if (mostrarFavoritos) Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(36.dp)
                    ) {
                        Text(if (mostrarFavoritos) "Mostrando Favoritos" else "Mostrar Favoritos")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la actividad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF1F1F1),
                    focusedIndicatorColor = Color(0xFF6200EA),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción de la actividad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF1F1F1),
                    focusedIndicatorColor = Color(0xFF6200EA),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        if (nombre.isNotBlank() && descripcion.isNotBlank()) {
                            actividades = actividades + Actividad(nombre, descripcion)
                            nombre = ""
                            descripcion = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EA),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Añadir Actividad")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val actividadesFiltradas = if (mostrarFavoritos) {
                actividades.filter { it.isFavorite }
            } else {
                actividades
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(actividadesFiltradas) { actividad ->
                    ActividadItem(
                        actividad = actividad,
                        onDelete = {
                            actividades = actividades.filter { it != actividad }
                        },
                        onToggleFavorite = {
                            actividades = actividades.map {
                                if (it == actividad) it.copy(isFavorite = !it.isFavorite) else it
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ActividadItem(
    actividad: Actividad,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Nombre: ${actividad.nombre}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Descripción: ${actividad.descripcion}",
                fontSize = 16.sp,
                color = Color(0xFF3E2723)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Borrar")
                }

                Button(
                    onClick = onToggleFavorite,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (actividad.isFavorite) Color(0xFFFFEB3B) else Color.Gray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (actividad.isFavorite) "Favorito" else "Añadir a Fav")
                }
            }
        }
    }
}

data class Actividad(
    val nombre: String,
    val descripcion: String,
    val isFavorite: Boolean = false
)

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MyApp()
}
