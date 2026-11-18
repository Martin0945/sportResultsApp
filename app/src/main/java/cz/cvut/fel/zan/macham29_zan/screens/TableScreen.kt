package cz.cvut.fel.zan.macham29_zan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import cz.cvut.fel.zan.macham29_zan.veiwModels.TableViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableScreen(tableViewModel: TableViewModel = viewModel()){
    val standings by tableViewModel.standings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Premier League")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    val context = LocalContext.current
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(tableViewModel.leagueEmblemUrl.collectAsState().value)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "League Emblem",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(50.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    )
                }

            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column(modifier = Modifier.fillMaxSize()){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161A22))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.width(12.dp))

                    Text(text = "#", modifier = Modifier.width(24.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(36.dp))
                    Text(text = "Team", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text(text = "M", modifier = Modifier.width(32.dp), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "P", modifier = Modifier.width(32.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(standings){team ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            val zoneColor = when (team.position) {
                                in 1..4 -> Color.Blue
                                5 -> Color.Magenta
                                in 18 .. 20 -> Color.Red
                                else -> Color.Transparent
                            }

                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(24.dp)
                                    .background(zoneColor, RoundedCornerShape(2.dp))
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = team.position.toString(),
                                modifier = Modifier.width(24.dp),
                                fontWeight = FontWeight.Bold
                            )

                            val context = LocalContext.current
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(team.crest)
                                    .decoderFactory(SvgDecoder.Factory())
                                    .build(),
                                contentDescription = "Logo ${team.teamName}",
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = team.teamName,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )

                            Text(
                                text = team.numMatches.toString(),
                                modifier = Modifier.width(32.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = team.points.toString(),
                                modifier = Modifier.width(32.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }
}