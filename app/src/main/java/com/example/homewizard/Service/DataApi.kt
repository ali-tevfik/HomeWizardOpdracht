package com.example.homewizard.Service

import com.example.homewizard.Model.ModelData
import com.example.homewizard.Model.ModelState
import retrofit2.Call
import retrofit2.http.GET

interface DataApi {
    @GET("api/v1/data")
    fun getData(): Call<ModelData>

}