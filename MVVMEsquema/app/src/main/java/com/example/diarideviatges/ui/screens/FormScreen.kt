package com.example.diarideviatges.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.diarideviatges.model.Difficulty
import com.example.diarideviatges.model.RouteEntry
import com.example.diarideviatges.model.label
import com.example.diarideviatges.ui.components.DifficultyBadge
import com.example.diarideviatges.viewmodel.TravelDiaryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Pantalla principal del formulario.
 * Permite crear rutas, consultar el histórico,
 * abrir rutas guardadas y eliminarlas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavHostController,
    viewModel: TravelDiaryViewModel
) {

    // Observamos el estado del ViewModel.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showHistory by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()

    val dateFieldInteraction = remember {
        MutableInteractionSource()
    }


    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Diari de Viatges")
                }
            )
        }

    ) { innerPadding ->


        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {


            Text(
                text = "Nueva ruta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )


            // -----------------------------
            // NOMBRE DE LA RUTA
            // -----------------------------

            OutlinedTextField(

                value = uiState.routeName,

                onValueChange =
                    viewModel::onRouteNameChange,

                label = {
                    Text("Nombre de la ruta")
                },

                isError =
                    uiState.nameError != null,

                supportingText = {

                    uiState.nameError?.let {

                        Text(
                            text = it,
                            color =
                                MaterialTheme.colorScheme.error
                        )
                    }
                },

                singleLine = true,

                modifier =
                    Modifier.fillMaxWidth()
            )


            // -----------------------------
            // DESTINO
            // -----------------------------

            OutlinedTextField(

                value = uiState.destination,

                onValueChange =
                    viewModel::onDestinationChange,

                label = {
                    Text("Destino")
                },

                isError =
                    uiState.destinationError != null,

                supportingText = {

                    uiState.destinationError?.let {

                        Text(
                            text = it,
                            color =
                                MaterialTheme.colorScheme.error
                        )
                    }
                },

                singleLine = true,

                modifier =
                    Modifier.fillMaxWidth()
            )


            // -----------------------------
            // DIFICULTAD
            // -----------------------------

            Text(
                text = "Nivel de dificultad",
                style = MaterialTheme.typography.titleSmall
            )


            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Difficulty.entries.forEach { difficulty ->

                    FilterChip(

                        selected =
                            uiState.selectedDifficulty ==
                                    difficulty,

                        onClick = {
                            viewModel.onDifficultySelected(
                                difficulty
                            )
                        },

                        label = {
                            Text(
                                difficulty.label()
                            )
                        }
                    )
                }
            }


            // -----------------------------
            // PRESUPUESTO
            // -----------------------------

            OutlinedTextField(

                value = uiState.budget,

                onValueChange =
                    viewModel::onBudgetChange,

                label = {
                    Text("Presupuesto estimado")
                },

                isError =
                    uiState.budgetError != null,

                supportingText = {

                    uiState.budgetError?.let {

                        Text(
                            text = it,
                            color =
                                MaterialTheme.colorScheme.error
                        )
                    }
                },

                prefix = {
                    Text("€ ")
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                singleLine = true,

                modifier =
                    Modifier.fillMaxWidth()
            )


            // -----------------------------
            // FECHA
            // -----------------------------

            OutlinedTextField(

                value = uiState.date,

                onValueChange = { },

                readOnly = true,

                label = {
                    Text("Fecha de la ruta")
                },

                supportingText = {

                    if (uiState.date.isBlank()) {

                        Text(
                            "Selecciona la fecha en el calendario"
                        )
                    }
                },

                trailingIcon = {

                    IconButton(
                        onClick = {
                            showDatePicker = true
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.DateRange,

                            contentDescription =
                                "Seleccionar fecha"
                        )
                    }
                },

                interactionSource =
                    dateFieldInteraction,

                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource =
                            dateFieldInteraction,

                        indication = null
                    ) {

                        showDatePicker = true
                    }
            )


            // -----------------------------
            // RUTA EN SOLITARIO
            // -----------------------------

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Text(
                    text = "Ruta hecha en solitario",
                    style =
                        MaterialTheme.typography.bodyLarge
                )


                Switch(

                    checked =
                        uiState.isSolo,

                    onCheckedChange =
                        viewModel::onSoloChange
                )
            }


            // -----------------------------
            // NOTAS
            // -----------------------------

            OutlinedTextField(

                value =
                    uiState.notes,

                onValueChange =
                    viewModel::onNotesChange,

                label = {
                    Text(
                        "Notas de la experiencia"
                    )
                },

                placeholder = {
                    Text(
                        "Relato de la jornada, puntos de interés…"
                    )
                },

                minLines = 3,

                modifier =
                    Modifier.fillMaxWidth()
            )


            // -----------------------------
            // GENERAR POSTAL
            // -----------------------------

            Button(

                onClick = {

                    viewModel
                        .generatePostal()
                        ?.let { routeId ->

                            navController.navigate(
                                "postal/$routeId"
                            )
                        }
                },

                enabled =
                    uiState.isFormValid,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)

            ) {

                Text(
                    "Generar postal / crónica"
                )
            }


            HorizontalDivider()


            // -----------------------------
            // HISTÓRICO
            // -----------------------------

            TextButton(

                onClick = {

                    showHistory =
                        !showHistory
                },

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.List,

                    contentDescription = null
                )


                Spacer(
                    Modifier.width(8.dp)
                )


                Text(
                    "Consultar historial (${uiState.savedRoutes.size})"
                )


                Spacer(
                    Modifier.weight(1f)
                )


                Icon(

                    imageVector =

                        if (showHistory) {

                            Icons.Default.KeyboardArrowUp

                        } else {

                            Icons.Default.KeyboardArrowDown
                        },

                    contentDescription = null
                )
            }


            // Botón para borrar todas las rutas.
            if (uiState.savedRoutes.isNotEmpty()) {

                TextButton(

                    onClick = {
                        viewModel.clearHistory()
                    },

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    Text(
                        "Borrar historial"
                    )
                }
            }


            // -----------------------------
            // LISTA DEL HISTÓRICO
            // -----------------------------

            AnimatedVisibility(
                visible = showHistory
            ) {

                if (
                    uiState.savedRoutes.isEmpty()
                ) {

                    Text(

                        text =
                            "Aún no has guardado ninguna ruta.",

                        style =
                            MaterialTheme.typography.bodyMedium
                    )

                } else {


                    Column(

                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)

                    ) {


                        uiState
                            .savedRoutes
                            .reversed()
                            .forEach { route ->


                                HistoryCard(

                                    route = route,


                                    // Abre la postal
                                    onOpen = {

                                        navController.navigate(
                                            "postal/${route.id}"
                                        )
                                    },


                                    // Elimina solo esta ruta
                                    onDelete = {

                                        viewModel.deleteRoute(
                                            route.id
                                        )
                                    }
                                )
                            }
                    }
                }
            }
        }
    }


    // -----------------------------
    // DATE PICKER
    // -----------------------------

    if (showDatePicker) {

        val selectedMillis =
            datePickerState.selectedDateMillis


        DatePickerDialog(

            onDismissRequest = {

                showDatePicker = false
            },


            confirmButton = {

                TextButton(

                    onClick = {

                        selectedMillis?.let { millis ->

                            viewModel.onDateSelected(
                                formatDate(millis)
                            )
                        }

                        showDatePicker = false
                    }

                ) {

                    Text("Aceptar")
                }
            },


            dismissButton = {

                TextButton(

                    onClick = {

                        showDatePicker = false
                    }

                ) {

                    Text("Cancelar")
                }
            }

        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }
}


/**
 * Tarjeta que representa una ruta guardada
 * dentro del histórico.
 */
@Composable
private fun HistoryCard(

    route: RouteEntry,

    onOpen: () -> Unit,

    onDelete: () -> Unit

) {


    ElevatedCard(

        modifier =
            Modifier.fillMaxWidth()

    ) {


        Column(

            modifier =
                Modifier.padding(12.dp),

            verticalArrangement =
                Arrangement.spacedBy(4.dp)

        ) {


            Text(

                text =
                    route.routeName,

                style =
                    MaterialTheme.typography.titleMedium
            )


            Text(

                text =
                    "${route.destination} · ${route.date}",

                style =
                    MaterialTheme.typography.bodyMedium
            )


            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp),

                verticalAlignment =
                    Alignment.CenterVertically

            ) {


                DifficultyBadge(
                    route.difficulty
                )


                Text(

                    text = String.format(
                        Locale.getDefault(),
                        "%.2f €",
                        route.estimatedBudget
                    ),

                    style =
                        MaterialTheme.typography.bodySmall
                )


                if (route.isSolo) {

                    Text(

                        text =
                            "🚶 En solitario",

                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }


            // -----------------------------
            // BOTONES DEL HISTÓRICO
            // -----------------------------

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.End

            ) {


                TextButton(

                    onClick = onOpen

                ) {

                    Text("Abrir")
                }


                TextButton(

                    onClick = onDelete

                ) {

                    Text("Eliminar")
                }
            }
        }
    }
}


/**
 * Convierte la fecha del DatePicker
 * al formato día/mes/año.
 */
private fun formatDate(
    millis: Long
): String {

    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(
        Date(millis)
    )
}