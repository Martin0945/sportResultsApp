package cz.cvut.fel.zan.macham29_zan

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppScreen(val route: String, val title: String, val icon: ImageVector){
    Program("program", "Program", Icons.Default.DateRange),
    Table("table", "Table", Icons.AutoMirrored.Filled.List),
    Results("results", "Results", Icons.Default.Done),
    Favorites("favorites", "Favorites", Icons.Default.Favorite),
}