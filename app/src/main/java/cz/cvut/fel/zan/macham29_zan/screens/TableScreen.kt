package cz.cvut.fel.zan.macham29_zan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.cvut.fel.zan.macham29_zan.veiwModels.TableViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableScreen(tableViewModel: TableViewModel = viewModel()){
    val standings by tableViewModel.standings.collectAsState()
    val leagueName by tableViewModel.leagueName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = leagueName)}
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()){
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "#", modifier = Modifier.weight(0.1f), fontWeight = FontWeight.Bold)
                    Text(text = "Team", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                    Text(text = "M", modifier = Modifier.weight(0.2f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(text = "P", modifier = Modifier.weight(0.2f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(standings){team ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = team.position.toString(), modifier = Modifier.weight(0.1f))
                            Text(text = team.teamName, modifier = Modifier.weight(0.5f), fontWeight = FontWeight.SemiBold)
                            Text(text = team.numMatches.toString(), modifier = Modifier.weight(0.2f), textAlign = TextAlign.Center)
                            Text(text = team.points.toString(), modifier = Modifier.weight(0.2f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    }
            }
        }
        }
    }
}