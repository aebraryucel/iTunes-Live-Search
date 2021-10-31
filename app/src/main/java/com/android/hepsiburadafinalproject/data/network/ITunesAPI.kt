package com.android.hepsiburadafinalproject.data

import com.android.hepsiburadafinalproject.model.Content
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesAPI {

    @GET("/search")
    suspend fun searchRequest(@Query("term") term: String ,@Query("entity") entity: String,@Query("limit") limit: Int,@Query("offset") offset: Int):Response<Content>

}