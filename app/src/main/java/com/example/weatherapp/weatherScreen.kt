package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.weatherapp.Component.AirQuality
import com.example.weatherapp.Component.DailyForecast
import com.example.weatherapp.Component.LocationInfo
import com.example.weatherapp.Component.ProfileButton
import com.example.weatherapp.Component.SearchButton
import com.example.weatherapp.Component.WeeklyForecast
import com.example.weatherapp.Component.extractDayName
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.Model.ForecastItem
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: WeatherViewModel,cityName:String) {
    val navController = rememberNavController()
    val weatherResult = viewModel.weatherResult.observeAsState()
    val forecastResult = viewModel.forecastResult.observeAsState()
    var data: WeatherModel?
    var forecastData: WeatherModel?
    weatherResult.value.let { weatherData ->
        data = weatherData
    }
    forecastResult.value.let { forecast ->
        forecastData = forecast

    }


    val forecastItemList = mutableListOf<ForecastItem>()
    forecastData?.let { weatherData ->
        weatherData.forecast.forecastday.forEach { forecastDay ->
            val (dayOfWeek, monthName) = extractDayName(forecastDay.date)
            val date = forecastDay.date.split("-")[2]
            val forecastItem = ForecastItem(
                image = forecastDay.day.condition.icon,
                dayOfWeek = dayOfWeek,
                date = "$date $monthName",
                temperature = forecastDay.day.maxtemp_c,
            )
            forecastItemList.add(forecastItem)
        }
    }

    NavHost(navController = navController, startDestination = WeatherScreen.route) {
        composable(WeatherScreen.route) {
            WeatherScreen(viewModel, navController, forecastItemList,cityName,data)
        }
        composable(SearchScreen.route) {

            SearchScreen(navController, viewModel, weatherResult)

        }
        composable(SecondWeatherScreen.route) {
            SecondWeatherScreen(navController,data,forecastItemList,viewModel)
        }



    }
}

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    navController: NavController,
    forecastItemList: List<ForecastItem>,
    cityName: String,
    data: WeatherModel?
){


    LaunchedEffect(cityName) {
        viewModel.viewModelScope.launch {
            viewModel.getData(cityName)
        }
    }


    Scaffold {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff548FD1))
            .padding(paddingValues = it)
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState())
        ) {
            //ActionBar()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileButton()
                LocationInfo(
                    modifier = Modifier.padding(top = 10.dp),
                    data
                    )
                SearchButton(viewModel,navController)

            }
          
            Spacer(modifier = Modifier.height(12.dp))
            DailyForecast(data)
            Spacer(modifier = Modifier.height(12.dp))
            AirQuality(data)
            Spacer(modifier = Modifier.height(12.dp))
            WeeklyForecast(forecastItemList)

        }
    }
}



@Composable
fun WeatherDetails(data: WeatherModel){
     Column(
         modifier = Modifier
             .fillMaxWidth()
             .padding(vertical = 8.dp),
         horizontalAlignment = Alignment.CenterHorizontally
     ){
         Row(
             modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.Start,
             verticalAlignment = Alignment.Bottom
         ) {
             Icon(imageVector = Icons.Default.LocationOn, contentDescription ="Location", modifier = Modifier.size(40.dp) )
             Text(text =data.location.name , fontSize = 30.sp )
             Spacer(modifier = Modifier.width(8.dp))
             Text(text =data.location.country , fontSize = 18.sp, color = Color.Gray )
         }
         Spacer(modifier = Modifier.height(16.dp))
         Text(text ="${data.current.temp_c} °C", fontSize =60.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

         AsyncImage(
             modifier = Modifier.size(160.dp),
             model = "https:${data.current.condition.icon}".replace("64x64","128x128")
             , contentDescription = "Condition Icon",
             )
         Text(text =data.current.condition.text, fontSize =20.sp, color = Color.Gray, textAlign = TextAlign.Center)
         Spacer(modifier = Modifier.height(16.dp))
         Card {
             Column(modifier = Modifier.fillMaxWidth()) {
                 Row(modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceAround) {
                      WeatherKeyValue("Humidity",data.current.humidity)
                     WeatherKeyValue("Wind Speed",data.current.wind_kph+"kmph")
                 }
                 Row(modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceAround) {
                     WeatherKeyValue("UV",data.current.uv)
                     WeatherKeyValue("Cloud",data.current.cloud)
                 }
                 Row(modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceAround) {
                     WeatherKeyValue("Local time",data.location.localtime.split(" ")[1])
                     WeatherKeyValue("Date",data.location.localtime.split(" ")[0])
                 }
             }
         }
     }
}

@Composable
fun WeatherKeyValue(key:String,value:String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = key, fontWeight = FontWeight.SemiBold  )
        Text(text = value,fontSize =20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}


