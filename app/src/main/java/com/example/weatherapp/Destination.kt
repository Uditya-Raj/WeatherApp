package com.example.weatherapp

interface Destination{
    var route : String
}
object WeatherScreen : Destination{
    override var route: String = "WeatherScreen"
}
object SearchScreen : Destination{
    override var route: String ="SearchScreen"
}

object SecondWeatherScreen : Destination{
    override var route : String ="SecondWeatherScreen"
}