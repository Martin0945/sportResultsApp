package cz.cvut.fel.zan.macham29_zan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.cvut.fel.zan.macham29_zan.database.DateConverter
import cz.cvut.fel.zan.macham29_zan.dataClasses.Match
import cz.cvut.fel.zan.macham29_zan.dataClasses.TeamStanding


@Database(entities = [Match::class, TeamStanding::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun matchDao(): MatchDao
    abstract fun teamStandingDao() : TeamStandingDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "league_database"
                ).fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}