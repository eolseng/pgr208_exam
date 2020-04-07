package no.kristiania.pgr208_exam.services

import no.kristiania.pgr208_exam.entities.FeaturesWrapperDto
import no.kristiania.pgr208_exam.entities.Place
import no.kristiania.pgr208_exam.entities.PlaceWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoForeignLandService {

    @GET("places")
    suspend fun getAll(): FeaturesWrapperDto

    @GET("place")
    suspend fun getById(@Query("id") id: Long): PlaceWrapper

}