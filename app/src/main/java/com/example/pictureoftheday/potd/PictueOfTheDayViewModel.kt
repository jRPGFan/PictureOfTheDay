package com.example.pictureoftheday.potd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureoftheday.BuildConfig
import com.example.pictureoftheday.utilities.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
) : ViewModel() {
    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest("")
        return liveDataForViewToObserve
    }

    fun getData(date: String): LiveData<PictureOfTheDayData> {
        sendServerRequest(date)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("API Key is required"))
        } else {
            if (date.isEmpty()) {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(object :
                    Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable("Unknown error"))
                            } else {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                    }
                })
            } else {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDayByDate(date, apiKey)
                    .enqueue(object :
                        Callback<PODServerResponseData> {
                        override fun onResponse(
                            call: Call<PODServerResponseData>,
                            response: Response<PODServerResponseData>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Success(response.body()!!)
                            } else {
                                val message = response.message()
                                if (message.isNullOrEmpty()) {
                                    liveDataForViewToObserve.value =
                                        PictureOfTheDayData.Error(Throwable("Unknown error"))
                                } else {
                                    liveDataForViewToObserve.value =
                                        PictureOfTheDayData.Error(Throwable(message))
                                }
                            }
                        }

                        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                        }
                    })
            }
        }
    }
}