package cz.cvut.fel.zan.macham29_zan.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "standings")

data class TeamStanding(
    @PrimaryKey
    val id: Int,
    val position: Int,
    val teamName: String,
    val numMatches: Int,
    val points: Int
)