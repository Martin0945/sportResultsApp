package cz.cvut.fel.zan.macham29_zan.veiwModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.zan.macham29_zan.api.MatchDetailResponse
import cz.cvut.fel.zan.macham29_zan.api.RetrofitClient
import cz.cvut.fel.zan.macham29_zan.api.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MatchDetailViewModel(private val dataStore : SettingsDataStore): ViewModel() {
    private val _matchDetail = MutableStateFlow<MatchDetailResponse?>(null)
    val MatchDetail: StateFlow<MatchDetailResponse?> = _matchDetail.asStateFlow()

    fun loadMatchDetail (matchId: Int){
        viewModelScope.launch {
            try{
                val token = dataStore.apiTokenFlow.first() ?: return@launch
                val response = RetrofitClient.api.getAfterMatchDetail(matchId, token)

                _matchDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}