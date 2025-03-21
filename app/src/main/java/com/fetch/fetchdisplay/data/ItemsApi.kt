package com.fetch.fetchdisplay.data

import retrofit2.http.GET


interface ItemsApi {
    @GET("/hiring.json")
    suspend fun getAllItems(): ItemsDTO
}