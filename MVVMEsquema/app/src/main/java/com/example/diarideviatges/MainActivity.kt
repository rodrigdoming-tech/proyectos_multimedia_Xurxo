package com.example.diarideviatges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.diarideviatges.navigation.TravelDiaryNavGraph
import com.example.diarideviatges.ui.theme.DiariDeViatgesTheme
import com.example.diarideviatges.viewmodel.TravelDiaryViewModel

class MainActivity : ComponentActivity() {

    // Creamos un único ViewModel para toda la aplicación
    private val viewModel: TravelDiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DiariDeViatgesTheme {

                TravelDiaryNavGraph(
                    viewModel = viewModel
                )
            }
        }
    }
}