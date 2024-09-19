package com.example.weatherapp.api

import com.example.weatherapp.Model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// creating method for sending request.
interface WeatherApi {
    @GET("v1/current.json")
    suspend fun getWeather(
        @Query("key") apikey :String,
        @Query("q") city : String,
        @Query("aqi") aqi: String ="yes"
    ):Response<WeatherModel>


    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apikey :String,
        @Query("q") city : String,
        @Query("days") days: Int = 7 // default value is 3 days
    ):Response<WeatherModel>


}