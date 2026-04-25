package cz.cvut.fel.zan.macham29_zan.veiwModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.zan.macham29_zan.veiwModels.MatchRepository
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class ResultsViewModel : ViewModel(){
    val matches: StateFlow<List<Match>> = MatchRepository.allMatches
        .map { list -> list.filter { it.score != null } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    fun updateSearchText(newText: String) { _searchText.value = newText }


}