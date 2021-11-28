package com.example.pictureoftheday.api.marsrover

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRoverPhotosAPI {
    @GET("mars-photos/api/v1/rovers/curiosity/latest_photos")
    fun GetMarsPhotosCuriosity(@Query("api_key") apikey: String):
            Call<MarsServerResponseData>
}