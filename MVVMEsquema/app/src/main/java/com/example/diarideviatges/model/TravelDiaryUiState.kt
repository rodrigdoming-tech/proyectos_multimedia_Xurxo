package com.example.diarideviatges.model

/**
 * Estado de la interfaz: campos actuales del formulario, errores de validación
 * en tiempo real y la lista de rutas guardadas en el histórico.
 */
data class TravelDiaryUiState(
    // ---- Campos del formulario ----
    val routeName: String = "",
    val destination: String = "",
    val selectedDifficulty: Difficulty? = null,
    val budget: String = "",
    val date: String = "",
    val isSolo: Boolean = false,
    val notes: String = "",
    // ---- Errores de validación ----
    val nameError: String? = null,
    val destinationError: String? = null,
    val budgetError: String? = null,
    // ---- Histórico de rutas guardadas ----
    val savedRoutes: List<RouteEntry> = emptyList()
) {
    /** true si el formulario es válido y se puede generar la postal / crónica. */
    val isFormValid: Boolean
        get() = routeName.isNotBlank() &&
                destination.isNotBlank() &&
                selectedDifficulty != null &&
                budget.isNotBlank() &&
                (budget.replace(',', '.').toDoubleOrNull() ?: -1.0) > 0.0 &&
                date.isNotBlank()
}
