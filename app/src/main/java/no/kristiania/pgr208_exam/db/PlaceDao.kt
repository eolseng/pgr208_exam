package no.kristiania.pgr208_exam.db

import androidx.lifecycle.LiveData
import androidx.room.*
import no.kristiania.pgr208_exam.entities.Place

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Query("SELECT *, rowid FROM places_table WHERE rowid = :id")
    fun getById(id: Long): LiveData<Place>
}