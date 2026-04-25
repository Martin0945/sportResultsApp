package cz.cvut.fel.zan.macham29_zan.veiwModels

import android.os.Build
import androidx.annotation.RequiresApi
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import cz.cvut.fel.zan.macham29_zan.database.MatchDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
object MatchRepository{
    private var matchDao: MatchDao? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    var allMatches: Flow<List<Match>> = MutableStateFlow(emptyList())

    fun initialize(dao: MatchDao){
        matchDao = dao
        allMatches = dao.getAllMatches()

        scope.launch {
            if(dao.getAllMatches().first().isEmpty()){
                loadInitialDataToDB()
            }
        }

    }

    fun toggleFavorite(match: Match){
        scope.launch {
            val updatedMatch = match.copy(isFavorite = !match.isFavorite)
            matchDao?.updateMatch(updatedMatch)
        }
    }

    fun updateNote(match: Match, note: String){
        scope.launch {
            val updatedMatch = match.copy(notes = note)
            matchDao?.updateMatch(updatedMatch)
        }
    }

    fun removeMultipleFromFavorites(matchIds: Set<Int>){
        scope.launch {
            val currentMatches = allMatches.first()
            val matchesToUpdate = currentMatches.filter { it.id in matchIds }.map{it.copy(isFavorite = false)}

            matchesToUpdate.forEach { matchDao?.updateMatch(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadInitialDataToDB(){
        val today = LocalDate.now()
        val mockData = listOf(
            Match(1, "Sparta Praha - Slavia Praha", today, "18:00"),
            Match(2, "Viktoria Plzeň - Baník Ostrava", today, "20:00"),
            Match(3, "Slovan Liberec - Sigma Olomouc", today.plusDays(1), "15:00"),
            Match(4, "Bohemians 1905 - FK Teplice", today.plusDays(1), "17:30"),
            Match(5, "nezobrazovat - FK Teplice", today.plusDays(8), "17:30"),
            Match(6, "Slovan Liberec - Sigma Olomouc", today.minusDays(3), null, "1:1"),
            Match(7, "Viktoria Plzeň - Sparta Praha", today.minusDays(3), null, "1:3"),
            Match(8, "Nezobrazovat  - Sigma Olomouc", today.minusDays(9), null, "1:1")
        )
        matchDao?.insertAll(mockData)
    }
}