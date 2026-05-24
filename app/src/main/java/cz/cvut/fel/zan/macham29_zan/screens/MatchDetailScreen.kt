package cz.cvut.fel.zan.macham29_zan.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import cz.cvut.fel.zan.macham29_zan.veiwModels.MatchDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: Int,
    viewModel : MatchDetailViewModel,
    onNavigateBack: () -> Unit
    ){
    val detail by viewModel.MatchDetail.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.loadMatchDetail(matchId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
       paddingValues ->
        if(detail == null){
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }else{
            val match = detail!!

            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TeamBadge(name = match.homeTeam.name,
                        logoUrl = match.homeTeam.crest,
                        modifier = Modifier.weight(1f))

                    Text(
                        text = "${match.score.fullTime.home ?: "-"} : ${match.score.fullTime.away ?: "-"}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    TeamBadge(name = match.awayTeam.name,
                        logoUrl = match.awayTeam.crest,
                        modifier = Modifier.weight(1f))

                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    Text(
                        text = "Match Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)

                    val currStatus: String = when (match.status) {
                        "TIMED" -> "Upcoming"
                        "IN_PLAY" -> "Live"
                        "PAUSED" -> "Half-time"
                        "FINISHED" -> "Full Time"
                        "POSTPONED" -> "Postponed"
                        "CANCELLED" -> "Cancelled"
                        else -> match.status
                    }

                    InfoRow(label = "Status", value = currStatus)
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)

                    if (match.matchday != null) {
                        InfoRow(label = "Round", value = "${match.matchday}. round")
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
                    }

                    if (match.venue != null) {
                        InfoRow(label = "Venue", value = match.venue)
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
                    }


                    val mainReferee = match.referees?.find { it.type == "REFEREE" }?.name
                    if (mainReferee != null) {
                        InfoRow(label = "Referee", value = mainReferee)
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
                    }

                    if (match.score.halfTime?.home != null) {
                        InfoRow(
                            label = "Half-time Score",
                            value = "${match.score.halfTime.home} : ${match.score.halfTime.away}"
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun TeamBadge(name: String, logoUrl: String, modifier: Modifier = Modifier){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(text = name, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, maxLines = 2)
        Spacer(modifier = Modifier.height(8.dp))

        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context).
                data(logoUrl).
                decoderFactory(SvgDecoder.Factory()).
                build(),
            contentDescription = "Logo $name",
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}