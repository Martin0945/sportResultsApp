    package cz.cvut.fel.zan.macham29_zan.screens

    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Close
    import androidx.compose.material.icons.filled.Favorite
    import androidx.compose.material.icons.filled.FavoriteBorder
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.focus.FocusRequester
    import androidx.compose.ui.focus.focusRequester
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import cz.cvut.fel.zan.macham29_zan.veiwModels.ResultsViewModel
    import java.sql.Date
    import java.time.LocalDate
    import java.time.format.DateTimeFormatter

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ResultsScreen(resultsViewModel: ResultsViewModel = viewModel()){
        val matchList by resultsViewModel.matches.collectAsState()
        val searchText by resultsViewModel.searchText.collectAsState()

        var isSearchActive by rememberSaveable { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(isSearchActive) {
            if (isSearchActive) {
                focusRequester.requestFocus()
            }
        }

        val limitDays = LocalDate.now().minusDays(7)

        val filteredMatches = matchList.filter { match ->
            val containsText = match.teams.contains(searchText, ignoreCase = true)
            val isNotTooOld = match.date >= limitDays

            containsText && isNotTooOld
        }

        val groupedMatches = filteredMatches.groupBy { it.date }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearchActive) {
                            TextField(
                                value = searchText,
                                onValueChange = { resultsViewModel.updateSearchText(it) },
                                placeholder = { Text("Search for team...") },
                                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                )
                            )
                        } else {
                            Text("League Results")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearchActive = !isSearchActive
                            if (!isSearchActive) {
                                resultsViewModel.updateSearchText("")
                            }
                        }) {
                            Icon(
                                imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    groupedMatches.toSortedMap(reverseOrder()).forEach { (date, matchesForDate) ->
                        item {
                            Text(
                                text = formatHeaderDateRes(date),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )
                        }

                        items(matchesForDate) {match ->
                            Card(
                                modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ){

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = match.teams,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (match.score != null) {
                                            Text(
                                                text = match.score,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        } else if (match.time != null) {
                                            Text(
                                                text = match.time,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
private fun formatHeaderDateRes(date: LocalDate): String{
    val today = LocalDate.now()
    return when(date){
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("d. M.")
            date.format(formatter)
        }
    }
}
