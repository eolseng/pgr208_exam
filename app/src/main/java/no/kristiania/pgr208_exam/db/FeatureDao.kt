package no.kristiania.pgr208_exam.db

import androidx.lifecycle.LiveData
import androidx.room.*
import no.kristiania.pgr208_exam.entities.Feature

@Dao
interface FeatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(feature: Feature)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(features: List<Feature>)

    @Delete
    suspend fun delete(feature: Feature)

    @Query("DELETE FROM features_table")
    suspend fun deleteAll()

    @Transaction
    suspend fun updateFeatures(features: List<Feature>) {
        deleteAll()
        insertAll(features)
    }

    @Query("SELECT *, rowid FROM features_table")
    fun getAll(): LiveData<List<Feature>>

    @Query("SELECT *, rowid FROM features_table WHERE name MATCH :query")
    fun searchFeatures(query: String): LiveData<List<Feature>>

}
