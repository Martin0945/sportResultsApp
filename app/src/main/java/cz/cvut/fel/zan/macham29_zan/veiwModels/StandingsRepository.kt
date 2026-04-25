package cz.cvut.fel.zan.macham29_zan.veiwModels

import cz.cvut.fel.zan.macham29_zan.dataClasses.TeamStanding
import cz.cvut.fel.zan.macham29_zan.database.TeamStandingDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object StandingsRepository {
    private var dao: TeamStandingDao? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    var standings: Flow<List<TeamStanding>> = MutableStateFlow(emptyList())

    fun initialize(standingsDao: TeamStandingDao) {
        dao = standingsDao
        standings = standingsDao.getAllStandings()

        scope.launch {
            if (standingsDao.getAllStandings().first().isEmpty()) {
                loadMockDataToDb()
            }
        }
    }

    private suspend fun loadMockDataToDb() {
        val fakeData = listOf(
            TeamStanding(1, 1, "Sparta Praha", 30, 76), // Přidal jsem na začátek každého řádku ID
            TeamStanding(2, 2, "Slavia Praha", 30, 72),
            TeamStanding(3, 3, "Viktoria Plzeň", 30, 62),
            TeamStanding(4, 4, "Baník Ostrava", 30, 45),
            TeamStanding(5, 5, "Mladá Boleslav", 30, 44),
            TeamStanding(6, 6, "Slovácko", 29, 41)
        )
        dao?.insertAll(fakeData)
    }
}