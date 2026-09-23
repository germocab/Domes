package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.ColorDetailScreen
import com.example.ui.screens.DomeBoardScreen
import com.example.ui.screens.DomeDetailScreen
import com.example.ui.screens.NewDomeScreen
import com.example.ui.theme.DomesTheme
import com.example.ui.viewmodel.DomesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DomesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    DomesApp()
                }
            }
        }
    }
}

@Composable
fun DomesApp(
    viewModel: DomesViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "board",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("board") {
            DomeBoardScreen(
                viewModel = viewModel,
                onNavigateToNewDome = { navController.navigate("new_dome") },
                onNavigateToDome = { domeId -> navController.navigate("dome_detail/$domeId") }
            )
        }

        composable("new_dome") {
            NewDomeScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "dome_detail/{domeId}",
            arguments = listOf(navArgument("domeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val domeId = backStackEntry.arguments?.getLong("domeId") ?: 0L
            DomeDetailScreen(
                domeId = domeId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToColor = { colorId -> navController.navigate("color_detail/$colorId") }
            )
        }

        composable(
            route = "color_detail/{colorId}",
            arguments = listOf(navArgument("colorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val colorId = backStackEntry.arguments?.getLong("colorId") ?: 0L
            ColorDetailScreen(
                colorId = colorId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
