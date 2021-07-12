package com.example.pictureoftheday.api.epic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureoftheday.BuildConfig
import com.example.pictureoftheday.utilities.RetrofitImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthFragmentViewModel(
    private val liveDataToObserve: MutableLiveData<EPICData> = MutableLiveData(),
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()
) : ViewModel() {
    fun getData(): LiveData<EPICData> {
        sendServerRequest()
        return liveDataToObserve
    }

    private fun sendServerRequest() {
        liveDataToObserve.value = EPICData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            EPICData.Error(Throwable("API Key is required"))
        } else {
            retrofitImpl.getRetrofitImplEPIC().getEPIC(apiKey).enqueue(object :
                Callback<EPICServerResponseJSONArray> {
                override fun onResponse(
                    call: Call<EPICServerResponseJSONArray>,
                    response: Response<EPICServerResponseJSONArray>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val res: Array<EPICServerResponseData> = gson.fromJson(response.body().toString(),Array<EPICServerResponseData>::class.java)

                        liveDataToObserve.value = EPICData.Success(res[0])
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataToObserve.value =
                                EPICData.Error(Throwable("Unknown error"))
                        } else {
                            liveDataToObserve.value =
                                EPICData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<EPICServerResponseJSONArray>, t: Throwable) {
                    liveDataToObserve.value = EPICData.Error(t)
                }
            })
        }
    }
}