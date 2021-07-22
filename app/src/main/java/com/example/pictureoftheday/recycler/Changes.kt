package com.example.pictureoftheday.recycler

data class Changes<out T> (
    val oldData: T,
    val newData: T
)

fun <T> createCombinedPayload(payloads: List<Changes<T>>) : Changes<T> {
    assert(payloads.isNotEmpty())
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Changes(firstChange.oldData, lastChange.newData)
}