package com.example.diarideviatges.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diarideviatges.ui.screens.FormScreen
import com.example.diarideviatges.ui.screens.PostalScreen
import com.example.diarideviatges.viewmodel.TravelDiaryViewModel

object Routes {

    const val FORM = "form"
    const val POSTAL = "postal/{routeId}"

    fun postal(routeId: String): String {
        return "postal/$routeId"
    }
}


@Composable
fun TravelDiaryNavGraph(
    viewModel: TravelDiaryViewModel,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.FORM
    ) {

        composable(Routes.FORM) {

            FormScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable(
            route = Routes.POSTAL,
            arguments = listOf(
                navArgument("routeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            PostalScreen(
                routeId = backStackEntry.arguments?.getString("routeId"),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}