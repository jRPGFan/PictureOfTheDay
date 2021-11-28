package com.example.pictureoftheday.api.epic

import org.json.JSONArray

data class EPICServerResponseJSONArray (
    val jsonArray: JSONArray
//    val image: String?,
//    val date: String?,
//    val caption: String?
)

data class EPICServerResponseData(
    val image: String?,
    val date: String?,
    val caption: String?
)
//
//data class EpicResponseDTO(
//    val image: String?,
//    val date: String?,
//    val caption: String?
//)