package no.kristiania.pgr208_exam.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceWrapper(
    val place: Place
)

@Fts4
@Entity(tableName = "places_table")
data class Place (
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Long,
    val type: String?,
    @SerializedName("mapboxIcon")
    val iconId: String?,
    val name: String,
    val comments: String?,
    val countryCode: String?,
    val lat: Double,
    val lon: Double,
    val stars: Int?,
    val banner: String?,
    val externalLink1: String?,
    val externalLinkDescription1: String?
)