package com.example.pictureoftheday.api.marsrover

data class MarsServerResponseData (
    val latest_photos: List<MarsResponseDTO?>
)

data class MarsResponseDTO(
    val img_src: String?,
    val earth_date: String?
)