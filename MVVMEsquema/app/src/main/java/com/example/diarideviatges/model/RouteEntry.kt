package com.example.diarideviatges.model

import kotlinx.serialization.Serializable

/** Nivel de dificultad de una ruta. */
enum class Difficulty {
    FACIL, MODERADA, EXIGENT
}

/** Etiqueta legible para cada nivel de dificultad. */
fun Difficulty.label(): String = when (this) {
    Difficulty.FACIL -> "Fácil"
    Difficulty.MODERADA -> "Moderada"
    Difficulty.EXIGENT -> "Exigente"
}

/**
 * Modelo inmutable que representa una ruta o escapada registrada en el diario.
 * Es serializable para poder guardarlo en el JSON del almacenamiento interno.
 */
@Serializable
data class RouteEntry(
    val id: String,
    val routeName: String,
    val destination: String,
    val difficulty: Difficulty,
    val estimatedBudget: Double,
    val date: String,
    val isSolo: Boolean,
    val notes: String
)
