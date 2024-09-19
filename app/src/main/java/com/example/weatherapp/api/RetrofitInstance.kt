package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

// here creating instance of retrofit.
object RetrofitInstance {
    private const val baseUrl = "https://api.weatherapi.com"
    private fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

     // creating instance of weather api. and intrigrating with retrofit.
    val weatherApi = getInstance().create(WeatherApi::class.java)
}