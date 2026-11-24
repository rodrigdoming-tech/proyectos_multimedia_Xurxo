package com.example.diarideviatges.util

import com.example.diarideviatges.model.RouteEntry
import com.example.diarideviatges.model.label
import java.util.Locale

/** Genera el texto de la crónica formateado en Markdown para la exportación. */
object ChronicleFormatter {

    fun buildChronicleMarkdown(route: RouteEntry): String = buildString {
        appendLine("# 🏞️ ${route.routeName}")
        appendLine()
        appendLine("**Destino:** ${route.destination}")
        appendLine("**Fecha:** ${route.date}")
        appendLine("**Dificultad:** ${route.difficulty.label()}")
        appendLine("**Presupuesto estimado:** " + String.format(Locale.US, "%.2f €", route.estimatedBudget))
        appendLine("**Ruta hecha en solitario:** " + if (route.isSolo) "Sí" else "No")
        appendLine()
        appendLine("## Crónica de la jornada")
        appendLine()
        if (route.notes.isBlank()) {
            appendLine("(Sin notas de la experiencia)")
        } else {
            appendLine(route.notes)
        }
        appendLine()
        appendLine("---")
        appendLine("_Generada con Diari de Viatges_")
    }
}
