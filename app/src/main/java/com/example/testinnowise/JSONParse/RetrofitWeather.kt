package com.example.xxxx.JSONParse

import com.example.innowise.JSON.Weather5DaysDataJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming


interface RetrofitAPI2 {
    @GET("forecast?&units=metric&appid=51292973022fda6b08d9db471b34a11c")
    @Streaming
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<Weather5DaysDataJSON?>?
}