package cz.cvut.fel.zan.macham29_zan

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.fel.zan.macham29_zan.api.SettingsDataStore
import cz.cvut.fel.zan.macham29_zan.screens.FavoritesScreen
import cz.cvut.fel.zan.macham29_zan.screens.MatchDetailScreen
import cz.cvut.fel.zan.macham29_zan.screens.ProgramScreen
import cz.cvut.fel.zan.macham29_zan.screens.ResultsScreen
import cz.cvut.fel.zan.macham29_zan.screens.TableScreen
import cz.cvut.fel.zan.macham29_zan.veiwModels.MatchDetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){}

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar (containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary){
                AppScreen.entries.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Table.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Table.route) { TableScreen() }
            composable(AppScreen.Program.route) { ProgramScreen(onMatchClick = { matchId -> navController.navigate("detail/$matchId") })}
            composable(AppScreen.Results.route) { ResultsScreen(onMatchClick = { matchId -> navController.navigate("detail/$matchId") })}
            composable(AppScreen.Favorites.route) { FavoritesScreen(onMatchClick = { matchId -> navController.navigate("detail/$matchId")})}

            composable(
                route = "detail/{matchId}",
                arguments = listOf(navArgument("matchId") { type = NavType.IntType })
            ) { backStackEntry ->
                val matchId = backStackEntry.arguments?.getInt("matchId") ?: -1

                val context = LocalContext.current
                val dataStore = remember { SettingsDataStore(context) }

                val factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MatchDetailViewModel(dataStore) as T
                    }
                }
                val viewModel: MatchDetailViewModel = viewModel(factory = factory)

                MatchDetailScreen(
                    matchId = matchId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
