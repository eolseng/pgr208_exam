package no.kristiania.pgr208_exam.db

import android.content.Context
import androidx.room.*
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.entities.Place

val DATABASE_NAME = "sailaway_database"

@Database(
    entities = [Feature::class, Place::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SailAwayDatabase : RoomDatabase() {

    abstract fun featureDao(): FeatureDao
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: SailAwayDatabase? = null

        fun getDatabase(
            context: Context
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

class Converters {
    @TypeConverter
    fun listToString(list: List<Double>) : String {
        return list.joinToString(" ")
    }
    @TypeConverter
    fun stringToList(string: String): List<Double> {
        return string.split(" ").map { it.toDouble() }
    }
}