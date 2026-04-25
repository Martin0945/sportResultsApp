package cz.cvut.fel.zan.macham29_zan.veiwModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.zan.macham29_zan.veiwModels.MatchRepository
import kotlinx.coroutines.flow.StateFlow
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    val favoriteMatches: StateFlow<List<Match>> = MatchRepository.allMatches.map {
        list -> list.filter {it.isFavorite}
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMatchIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedMatchIds: StateFlow<Set<Int>> = _selectedMatchIds.asStateFlow()

    fun toggleSelection(matchId: Int){
        val currentSelection = _selectedMatchIds.value
        if(currentSelection.contains(matchId)){
            _selectedMatchIds.value = currentSelection - matchId
        }else{
            _selectedMatchIds.value = currentSelection + matchId
        }
    }

    fun clearSection(){
        _selectedMatchIds.value = emptySet()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteSelected(){
        MatchRepository.removeMultipleFromFavorites(_selectedMatchIds.value)
        clearSection()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNotes(id: Int, notes: String) {
        val matchToUpdate = favoriteMatches.value.find { it.id == id }
        if (matchToUpdate != null) {
            MatchRepository.updateNote(matchToUpdate, notes)
        }
    }

}