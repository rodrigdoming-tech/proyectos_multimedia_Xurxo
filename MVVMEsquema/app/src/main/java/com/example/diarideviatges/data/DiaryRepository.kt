package com.example.diarideviatges.data

import android.content.Context
import com.example.diarideviatges.model.RouteEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Repositorio de persistencia interna: guarda y lee el histórico de rutas
 * en un archivo JSON privado dentro de context.filesDir (diari_viatges.json).
 */
class DiaryRepository(context: Context) {

    private val file = File(context.filesDir, "diari_viatges.json")

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /** Sobrescribe el JSON interno con la lista completa de rutas. */
    fun saveRoutes(routes: List<RouteEntry>) {
        try {
            file.writeText(json.encodeToString(routes))
        } catch (_: Exception) {
            // Si falla la escritura el histórico en memoria se mantiene intacto
        }
    }

    /** Lee el histórico del almacenamiento interno; devuelve lista vacía si no existe. */
    fun loadRoutes(): List<RouteEntry> = try {
        if (file.exists()) json.decodeFromString<List<RouteEntry>>(file.readText()) else emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}
