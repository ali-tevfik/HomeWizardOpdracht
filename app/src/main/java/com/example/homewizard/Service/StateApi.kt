package com.example.homewizard.Service

import com.example.homewizard.Model.ModelState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

//Api Con
interface StateApi {
    @GET("api/v1/state")
    fun getState(): Call<ModelState>

    @PUT("api/v1/state/")
    fun setState(@Body data : ModelState): Call<ModelState>
}