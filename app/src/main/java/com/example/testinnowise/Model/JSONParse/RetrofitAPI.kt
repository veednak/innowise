package com.example.testinnowise.Model.JSONParse

import com.example.xxxx.JSONParse.CurrentWeatherDataJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming


interface RetrofitAPI {

    @GET("weather?&units=metric&appid=51292973022fda6b08d9db471b34a11c")
    @Streaming
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<CurrentWeatherDataJson?>?
}