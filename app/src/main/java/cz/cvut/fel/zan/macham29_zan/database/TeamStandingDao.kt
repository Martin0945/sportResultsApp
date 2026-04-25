package cz.cvut.fel.zan.macham29_zan.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.cvut.fel.zan.macham29_zan.dataClasses.TeamStanding
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamStandingDao {
    @Query("SELECT * FROM standings ORDER BY position ASC")
    fun getAllStandings(): Flow<List<TeamStanding>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(standings: List<TeamStanding>)
}
