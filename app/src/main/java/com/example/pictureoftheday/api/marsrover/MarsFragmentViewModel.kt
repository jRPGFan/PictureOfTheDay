package com.example.pictureoftheday.api.marsrover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureoftheday.BuildConfig
import com.example.pictureoftheday.api.epic.EPICData
import com.example.pictureoftheday.api.epic.EPICServerResponseData
import com.example.pictureoftheday.utilities.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsFragmentViewModel(
    private val liveDataToObserve: MutableLiveData<MarsData> = MutableLiveData(),
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
) : ViewModel() {
    fun getData(): LiveData<MarsData> {
        sendServerRequest()
        return liveDataToObserve
    }

    private fun sendServerRequest() {
        liveDataToObserve.value = MarsData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            MarsData.Error(Throwable("API Key is required"))
        } else {
            retrofitImpl.getRetrofitImplMars().GetMarsPhotosCuriosity(apiKey).enqueue(object :
                Callback<MarsServerResponseData> {
                override fun onResponse(
                    call: Call<MarsServerResponseData>,
                    response: Response<MarsServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataToObserve.value =
                            MarsData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataToObserve.value =
                                MarsData.Error(Throwable("Unknown error"))
                        } else {
                            liveDataToObserve.value =
                                MarsData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<MarsServerResponseData>, t: Throwable) {
                    liveDataToObserve.value = MarsData.Error(t)
                }
            })
        }
    }
}