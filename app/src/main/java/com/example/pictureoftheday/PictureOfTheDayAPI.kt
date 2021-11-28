package com.example.pictureoftheday

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String):
            Call<PODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayByDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): Call<PODServerResponseData>
}