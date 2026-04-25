package cz.cvut.fel.zan.macham29_zan.veiwModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.zan.macham29_zan.dataClasses.TeamStanding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class TableViewModel: ViewModel(){
    val standings: StateFlow<List<TeamStanding>> = StandingsRepository.standings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _leagueName = MutableStateFlow("Chance liga")
    val leagueName: StateFlow<String> = _leagueName.asStateFlow()

}