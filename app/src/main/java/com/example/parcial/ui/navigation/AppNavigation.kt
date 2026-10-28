package com.example.parcial.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.parcial.ui.screens.BuscarPorDniScreen
import com.example.parcial.ui.screens.DetalleDenunciaScreen
import com.example.parcial.ui.screens.HomeScreen
import com.example.parcial.ui.screens.ListaDenunciasScreen
import com.example.parcial.ui.screens.RegistrarDenunciaScreen
import com.example.parcial.viewmodel.DenunciaViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Lista : Screen("lista")
    object Registrar : Screen("registrar")
    object BuscarDni : Screen("buscar_dni")
    object Detalle : Screen("detalle/{denunciaId}") {
        fun createRoute(denunciaId: Int) = "detalle/$denunciaId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: DenunciaViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToLista = { navController.navigate(Screen.Lista.route) },
                onNavigateToRegistrar = { navController.navigate(Screen.Registrar.route) },
                onNavigateToBuscarDni = { navController.navigate(Screen.BuscarDni.route) },
                onNavigateToDetalleSearch = { navController.navigate(Screen.Detalle.createRoute(0)) }
            )
        }

        composable(Screen.Lista.route) {
            ListaDenunciasScreen(
                viewModel = viewModel,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detalle.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Registrar.route) {
            RegistrarDenunciaScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.BuscarDni.route) {
            BuscarPorDniScreen(
                viewModel = viewModel,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detalle.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detalle.route,
            arguments = listOf(
                navArgument("denunciaId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val denunciaId = backStackEntry.arguments?.getInt("denunciaId") ?: 0
            DetalleDenunciaScreen(
                denunciaId = denunciaId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
