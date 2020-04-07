package no.kristiania.pgr208_exam.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName

data class FeaturesWrapperDto(
    val features: List<Feature>
)

@Fts4
@Entity(tableName = "features_table")
data class Feature(
    @Embedded
    val properties: Properties,
    @Embedded
    val geometry: Geometry?
)

data class Properties(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Long,
    val name: String,
    @SerializedName("icon")
    val iconId: String
)

@Entity
data class Geometry(
    val coordinates: List<Double>
)