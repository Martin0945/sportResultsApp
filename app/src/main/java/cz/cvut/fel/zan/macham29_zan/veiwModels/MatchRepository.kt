package cz.cvut.fel.zan.macham29_zan.veiwModels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import cz.cvut.fel.zan.macham29_zan.api.RetrofitClient
import cz.cvut.fel.zan.macham29_zan.api.SettingsDataStore
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import cz.cvut.fel.zan.macham29_zan.database.MatchDao
import cz.cvut.fel.zan.macham29_zan.notifications.MatchAlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
object MatchRepository{
    private var matchDao: MatchDao? = null
    private var dataStore: SettingsDataStore? = null
    private val scope = CoroutineScope(Dispatchers.IO)


    var allMatches: Flow<List<Match>> = MutableStateFlow(emptyList())

    fun initialize(dao: MatchDao, settings: SettingsDataStore){
        matchDao = dao
        dataStore = settings
        allMatches = dao.getAllMatches()

        scope.launch {
            syncMatchesWithApi()
        }

    }

    private suspend fun syncMatchesWithApi() {
        try {
            val token = dataStore?.apiTokenFlow?.first() ?: return
            val response = RetrofitClient.api.getMatches(token)

            val existingMatches = matchDao?.getAllMatches()?.first()?.associateBy { it.id } ?: emptyMap()


            val entitiesToSave = response.matches.map { dto ->
                val existing = existingMatches[dto.id]

                val instant = java.time.Instant.parse(dto.utcDate)
                val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                val matchDate = localDateTime.toLocalDate()
                val matchTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                val scoreString = if(dto.score.fullTime.home != null){
                    "${dto.score.fullTime.home}:${dto.score.fullTime.away}"
                }else{
                    null
                }

                Match(
                    id = dto.id,
                    teams = "${dto.homeTeam.name} - ${dto.awayTeam.name}",
                    date = matchDate,
                    time = matchTime,
                    score = scoreString,
                    isFavorite = existing?.isFavorite ?: false,
                    notes = existing?.notes ?: ""
                )
            }
            matchDao?.insertAll(entitiesToSave)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleFavorite(context : Context ,match: Match){
        scope.launch {
            val updatedMatch = match.copy(isFavorite = !match.isFavorite)
            matchDao?.updateMatch(updatedMatch)

            if (updatedMatch.isFavorite) {
                scheduleNotification(context, updatedMatch)
            } else {
                cancelNotification(context, updatedMatch)
            }
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

    private fun scheduleNotification(context : Context, match: Match){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intentReal = Intent(context, MatchAlarmReceiver::class.java).apply {
            putExtra("EXTRA_MATCH_TITLE", match.teams)
            putExtra("EXTRA_MATCH_ID", match.id)
            putExtra("IS_DEMO", false)
        }

        val pendingIntentReal = PendingIntent.getBroadcast(
            context,
            match.id,
            intentReal,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val matchDateTime = LocalDateTime.of(match.date, LocalTime.parse(match.time))
        val notificationTime = matchDateTime.minusHours(1)
        val triggerTimeReal = notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (triggerTimeReal > System.currentTimeMillis()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeReal,
                pendingIntentReal
            )
        }

        val formatter = DateTimeFormatter.ofPattern("dd.MM.")
        val formattedDate = match.date.format(formatter)

        val intentDemo = Intent(context, MatchAlarmReceiver::class.java).apply {
            putExtra("EXTRA_MATCH_TITLE", match.teams)
            putExtra("EXTRA_MATCH_ID", match.id)
            putExtra("EXTRA_MATCH_DATE", formattedDate)
            putExtra("IS_DEMO", true)
        }

        val pendingIntentDemo = PendingIntent.getBroadcast(
            context,
            match.id + 100000,
            intentDemo,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTimeDemo = System.currentTimeMillis() + 10_000

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeDemo,
            pendingIntentDemo
        )
    }

    private fun cancelNotification(context: Context, match: Match) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intentReal = Intent(context, MatchAlarmReceiver::class.java)
        val pendingIntentReal = PendingIntent.getBroadcast(
            context, match.id, intentReal, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntentReal)

        val pendingIntentDemo = PendingIntent.getBroadcast(
            context, match.id + 100000, intentReal, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntentDemo)
    }

}