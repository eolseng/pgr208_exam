package no.kristiania.pgr208_exam.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

data class PlaceWrapper(
    val place: Place
)

@Fts4
@Entity(tableName = "places_table")
data class Place (
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Long,
    val name: String,
    var comments: String?,
    val banner: String?,
    val lat: Double,
    val lon: Double
)