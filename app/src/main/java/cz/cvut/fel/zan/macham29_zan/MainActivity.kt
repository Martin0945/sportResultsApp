package cz.cvut.fel.zan.macham29_zan

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import cz.cvut.fel.zan.macham29_zan.database.AppDatabase
import cz.cvut.fel.zan.macham29_zan.ui.theme.Macham29ZANTheme
import cz.cvut.fel.zan.macham29_zan.veiwModels.MatchRepository
import cz.cvut.fel.zan.macham29_zan.veiwModels.StandingsRepository

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        MatchRepository.initialize(database.matchDao())
        StandingsRepository.initialize(database.teamStandingDao())

        enableEdgeToEdge()
        setContent {
            Macham29ZANTheme {
                MainApp()
            }
        }
    }
}