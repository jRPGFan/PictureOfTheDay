package com.example.pictureoftheday.api.epic

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EPICAPI {
    @GET("EPIC/api/natural/images")
    fun getEPIC(@Query("api_key") apiKey: String):
            Call<EPICServerResponseJSONArray>
}