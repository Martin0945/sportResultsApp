package cz.cvut.fel.zan.macham29_zan.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey
    val id: Int,
    val teams: String,
    val date: LocalDate,
    val time: String? = null,
    val score: String? = null,
    val isFavorite: Boolean = false,
    val notes: String = ""
)