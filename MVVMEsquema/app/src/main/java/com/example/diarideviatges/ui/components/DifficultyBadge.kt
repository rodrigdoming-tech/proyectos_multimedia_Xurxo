package com.example.diarideviatges.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diarideviatges.model.Difficulty
import com.example.diarideviatges.model.label

/** Insignia visual coloreada según el nivel de dificultad de la ruta. */
@Composable
fun DifficultyBadge(difficulty: Difficulty, modifier: Modifier = Modifier) {
    val colors = when (difficulty) {
        Difficulty.FACIL -> Color(0xFFDCEDC8) to Color(0xFF33691E)
        Difficulty.MODERADA -> Color(0xFFFFE0B2) to Color(0xFF8D6E00)
        Difficulty.EXIGENT -> Color(0xFFFFCDD2) to Color(0xFFB71C1C)
    }
    Surface(
        modifier = modifier,
        color = colors.first,
        contentColor = colors.second,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = difficulty.label(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
