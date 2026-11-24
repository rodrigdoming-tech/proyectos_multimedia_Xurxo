package com.example.diarideviatges.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diarideviatges.data.DiaryRepository
import com.example.diarideviatges.model.Difficulty
import com.example.diarideviatges.model.RouteEntry
import com.example.diarideviatges.model.TravelDiaryUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel MVVM: gestiona el estado reactivo con un MutableStateFlow privado
 * expuesto como StateFlow público inmutable. Todas las actualizaciones se hacen
 * de forma inmutable mediante .copy() sobre el estado actual.
 */
class TravelDiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DiaryRepository(application)

    private val _uiState = MutableStateFlow(TravelDiaryUiState())
    val uiState: StateFlow<TravelDiaryUiState> = _uiState.asStateFlow()

    init {
        // Al abrir la app se lee el histórico desde el almacenamiento interno privado
        viewModelScope.launch(Dispatchers.IO) {
            val savedRoutes = repository.loadRoutes()
            _uiState.update { it.copy(savedRoutes = savedRoutes) }
        }
    }

    // ---- Actualización del formulario (validación en tiempo real) ----

    fun onRouteNameChange(value: String) {
        _uiState.update {
            it.copy(
                routeName = value,
                nameError = if (value.isBlank()) "El nombre de la ruta es obligatorio" else null
            )
        }
    }

    fun onDestinationChange(value: String) {
        _uiState.update {
            it.copy(
                destination = value,
                destinationError = if (value.isBlank()) "El destino es obligatorio" else null
            )
        }
    }

    fun onDifficultySelected(difficulty: Difficulty) {
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
    }

    fun onBudgetChange(value: String) {
        val cleaned = value.filter { it.isDigit() || it == ',' || it == '.' }
        val normalized = cleaned.replace(',', '.')
        val budgetError = when {
            cleaned.isBlank() -> "El presupuesto es obligatorio"
            normalized.toDoubleOrNull() == null || normalized.toDouble() <= 0.0 ->
                "Introduce un importe válido mayor que 0"
            else -> null
        }
        _uiState.update { it.copy(budget = cleaned, budgetError = budgetError) }
    }

    fun onDateSelected(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun onSoloChange(isSolo: Boolean) {
        _uiState.update { it.copy(isSolo = isSolo) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    /**
     * Valida el formulario, crea la RouteEntry, la añade al histórico y la
     * guarda automáticamente en el JSON interno (filesDir). Devuelve el id de
     * la ruta generada (o null si el formulario no es válido) para navegar.
     */
    fun generatePostal(): String? {
        val current = _uiState.value
        if (!current.isFormValid) return null

        val entry = RouteEntry(
            id = UUID.randomUUID().toString(),
            routeName = current.routeName.trim(),
            destination = current.destination.trim(),
            difficulty = current.selectedDifficulty ?: return null,
            estimatedBudget = current.budget.replace(',', '.').toDouble(),
            date = current.date,
            isSolo = current.isSolo,
            notes = current.notes.trim()
        )

        _uiState.update {
            it.copy(
                savedRoutes = it.savedRoutes + entry,
                // Se reinicia el formulario tras generar la crónica
                routeName = "",
                destination = "",
                selectedDifficulty = null,
                budget = "",
                date = "",
                isSolo = false,
                notes = "",
                nameError = null,
                destinationError = null,
                budgetError = null
            )
        }

        // Persistencia en segundo plano (emmagatzematge intern: diari_viatges.json)
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveRoutes(_uiState.value.savedRoutes)
        }

        return entry.id
    }

    // Elimina una ruta concreta del historial
    fun deleteRoute(routeId: String) {

        _uiState.update { currentState ->

            currentState.copy(
                savedRoutes = currentState.savedRoutes.filterNot { route ->
                    route.id == routeId
                }
            )
        }

        // Guardamos de nuevo el historial actualizado
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveRoutes(_uiState.value.savedRoutes)
        }
    }


    // Borra todas las rutas del historial
    fun clearHistory() {

        _uiState.update { currentState ->

            currentState.copy(
                savedRoutes = emptyList()
            )
        }

        // Guardamos la lista vacía en el JSON
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveRoutes(emptyList())
        }
    }
}
