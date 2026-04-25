package cz.cvut.fel.zan.macham29_zan

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.cvut.fel.zan.macham29_zan.screens.FavoritesScreen
import cz.cvut.fel.zan.macham29_zan.screens.ProgramScreen
import cz.cvut.fel.zan.macham29_zan.screens.ResultsScreen
import cz.cvut.fel.zan.macham29_zan.screens.SettingsScreen
import cz.cvut.fel.zan.macham29_zan.screens.TableScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                AppScreen.entries.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            composable(AppScreen.Program.route) { ProgramScreen() }
            composable(AppScreen.Results.route) { ResultsScreen() }
            composable(AppScreen.Favorites.route) { FavoritesScreen() }
            composable(AppScreen.Settings.route) { SettingsScreen() }
        }
    }
}