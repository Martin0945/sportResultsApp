package cz.cvut.fel.zan.macham29_zan.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao{
    
    @Query("SELECT * FROM matches")
    fun getAllMatches(): Flow<List<Match>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<Match>)

    @Update
    suspend fun updateMatch(match: Match)

}