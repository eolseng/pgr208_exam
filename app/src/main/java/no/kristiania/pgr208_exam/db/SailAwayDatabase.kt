package no.kristiania.pgr208_exam.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.entities.Place

val DATABASE_NAME = "sailaway_database"

@Database(
    entities = [Feature::class, Place::class],
    version = 1,
    exportSchema = false
)
abstract class SailAwayDatabase : RoomDatabase() {

    abstract fun featureDao(): FeatureDao
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: SailAwayDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): SailAwayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SailAwayDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}