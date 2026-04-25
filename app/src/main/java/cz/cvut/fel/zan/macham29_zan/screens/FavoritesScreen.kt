package cz.cvut.fel.zan.macham29_zan.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import cz.cvut.fel.zan.macham29_zan.veiwModels.FavoritesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(favoritesViewModel: FavoritesViewModel = viewModel()) {

    val favoriteMatches by favoritesViewModel.favoriteMatches.collectAsState()

    val selectedIds by favoritesViewModel.selectedMatchIds.collectAsState()

    val isSelectionMode = selectedIds.isNotEmpty()

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var editingMatchId by rememberSaveable { mutableStateOf<Int?>(null) }
    var noteText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            if(isSelectionMode){
                TopAppBar(
                    title = {Text("Selected: ${selectedIds.size}")},
                    navigationIcon = {
                        IconButton(onClick = {favoritesViewModel.clearSection()}) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel selection")
                        }
                    },
                    actions = {
                        IconButton(onClick = {favoritesViewModel.deleteSelected()}) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete selected")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }else{
                TopAppBar(title = { Text("Favorite Matches") })
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (favoriteMatches.isEmpty()) {
                Text(
                    text = "No favorite matches yet",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(favoriteMatches) { match ->
                        val isSelected = selectedIds.contains(match.id)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .combinedClickable(
                                    onClick = {
                                        if(isSelectionMode){
                                            favoritesViewModel.toggleSelection(match.id)
                                        }else{
                                            editingMatchId = match.id
                                            noteText = match.notes
                                            showDialog = true
                                        }
                                    },
                                    onLongClick = {
                                        favoritesViewModel.toggleSelection(match.id)
                                    }
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = match.teams,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if(!isSelectionMode){
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit note",
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }

                                if (match.notes.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Note: ${match.notes}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("Edit note") },
                text = {
                    TextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Enter your note") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        editingMatchId?.let { id ->
                            favoritesViewModel.updateNotes(id, noteText)
                        }
                        showDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}