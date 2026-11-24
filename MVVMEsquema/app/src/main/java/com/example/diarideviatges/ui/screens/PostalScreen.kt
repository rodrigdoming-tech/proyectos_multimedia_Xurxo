package com.example.diarideviatges.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.diarideviatges.model.RouteEntry
import com.example.diarideviatges.ui.components.DifficultyBadge
import com.example.diarideviatges.util.ChronicleFormatter
import com.example.diarideviatges.viewmodel.TravelDiaryViewModel
import java.util.Locale

/**
 * Pantalla 2: maqueta la ruta como una tarjeta visual de viaje (postal)
 * y permite exportar la crónica con el Storage Access Framework.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostalScreen(
    routeId: String?,
    navController: NavHostController,
    viewModel: TravelDiaryViewModel
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // La ruta se recupera del StateFlow del ViewModel a partir del id de navegación
    val route = remember(uiState.savedRoutes, routeId) {
        uiState.savedRoutes.firstOrNull { it.id == routeId }
    }
    val context = LocalContext.current

    // Exportación con el contrato nativo CreateDocument (Storage Access Framework)
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        if (uri != null && route != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    stream.write(
                        ChronicleFormatter.buildChronicleMarkdown(route).toByteArray(Charsets.UTF_8)
                    )
                }
                Toast.makeText(context, "Crónica exportada correctamente", Toast.LENGTH_LONG).show()
            } catch (_: Exception) {
                Toast.makeText(context, "No se pudo exportar la crónica", Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Postal del viaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (route == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se ha encontrado la ruta seleccionada.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PostalCard(route = route)

                Button(
                    onClick = {
                        val safeName = route.routeName
                            .lowercase()
                            .replace(Regex("[^a-z0-9]+"), "_")
                            .trim('_')
                        exportLauncher.launch("cronica_$safeName.md")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Exportar crónica (.md / .txt)")
                }
            }
        }
    }
}

/** Tarjeta visual de viaje con cabecera degradada, insignia y relato. */
@Composable
private fun PostalCard(route: RouteEntry, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column {
            // Cabecera con la fecha y el destino destacado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF00639B), Color(0xFF4A9ED3))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = route.date,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = route.destination,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = route.routeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DifficultyBadge(difficulty = route.difficulty)
                    if (route.isSolo) {
                        Text(
                            text = "🚶 Ruta en solitario",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = "Presupuesto estimado: " +
                            String.format(Locale.getDefault(), "%.2f €", route.estimatedBudget),
                    style = MaterialTheme.typography.bodyMedium
                )

                HorizontalDivider()

                Text(
                    text = "Crónica de la jornada",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = route.notes.ifBlank { "(Sin notas de la experiencia)" },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
