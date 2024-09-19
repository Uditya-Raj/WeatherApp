package com.example.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.Model.constant
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel(){
     private val weatherApi  = RetrofitInstance.weatherApi
      val weatherResult  = MutableLiveData<WeatherModel?>()
       val forecastResult = MutableLiveData<WeatherModel?>()
     val  isLoading = MutableLiveData<Boolean>(false)


     fun getData(city:String){
          viewModelScope.launch {
                   isLoading.postValue(true)
                    val response = weatherApi.getWeather(constant.apiKey,city,constant.aqi)
                    val forecast = weatherApi.getForecast(constant.apiKey,city)
                    if(response.isSuccessful){
                         response.body()?.let {
                              weatherResult.postValue(response.body())
                         }
                         isLoading.postValue(true)
                    }
              if(forecast.isSuccessful){
                  forecast.body()?.let {
                      forecastResult.postValue(forecast.body())
                  }
                  isLoading.postValue(true)
              }

          }

     }


    fun resetWeatherResult() {
        weatherResult.value = null
    }

    fun refreshData(city:String) {
        weatherResult.value = null
        getData(city)
    }
}