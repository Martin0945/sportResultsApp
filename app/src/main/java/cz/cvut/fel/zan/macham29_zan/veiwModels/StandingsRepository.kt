package cz.cvut.fel.zan.macham29_zan.veiwModels

import cz.cvut.fel.zan.macham29_zan.api.RetrofitClient
import cz.cvut.fel.zan.macham29_zan.api.SettingsDataStore
import cz.cvut.fel.zan.macham29_zan.dataClasses.TeamStanding
import cz.cvut.fel.zan.macham29_zan.database.TeamStandingDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object StandingsRepository {
    private var dao: TeamStandingDao? = null
    private var dataStore: SettingsDataStore? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    var standings: Flow<List<TeamStanding>> = MutableStateFlow(emptyList())

    private val _leagueEmblemUrl = MutableStateFlow<String?>(null)
    val leagueEmblemUrl: StateFlow<String?> = _leagueEmblemUrl.asStateFlow()

    fun initialize(standingsDao: TeamStandingDao, settings: SettingsDataStore) {
        dao = standingsDao
        standings = standingsDao.getAllStandings()
        dataStore = settings

        scope.launch {
            syncTableWithApi()
        }
    }

    suspend fun syncTableWithApi(){
        try {
            val token = dataStore?.apiTokenFlow?.first() ?: return
            val response = RetrofitClient.api.getStandings(token)
            _leagueEmblemUrl.value = response.competition.emblem

            val totalStanding = response.standings.find { it.type == "TOTAL" }

            val entitiesToSave = totalStanding?.table?.map { record ->
                TeamStanding(
                    id = record.team.id,
                    position = record.position,
                    teamName = record.team.name,
                    numMatches = record.playedGames,
                    points = record.points,
                    crest = record.team.crest ?: ""
                )
            } ?: emptyList()
            if(entitiesToSave.isNotEmpty()){
                dao?.insertAll(entitiesToSave)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}