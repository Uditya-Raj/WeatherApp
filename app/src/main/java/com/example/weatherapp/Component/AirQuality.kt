package com.example.weatherapp.Component


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.ColorAirQualityIconTitle
import com.example.weatherapp.ui.theme.ColorSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AirQuality(
    data :WeatherModel?,
    modifier: Modifier = Modifier,

){
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        color = Color(0xff234390)


        ) {
       Column(
           modifier = Modifier.padding(
               vertical = 40.dp,
               horizontal = 40.dp
           ),
           verticalArrangement = Arrangement.spacedBy(14.dp)
       ){
           AirQualityHeader()
           FlowRow(
               modifier = Modifier.fillMaxWidth(),
               maxItemsInEachRow = 3,
               horizontalArrangement = Arrangement.spacedBy(30.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp)
           ) {
               AirQualityInfo(R.drawable.feel,"Real Feel","${data?.current?.windchill_c.toString()}°",modifier = Modifier.weight(1f))
               AirQualityInfo(R.drawable.img_cloudy,"Cloud","${data?.current?.cloud.toString()} %",modifier = Modifier.weight(1f))
               AirQualityInfo(R.drawable.wind,"Wind","${data?.current?.wind_mph.toString()} mph",modifier = Modifier.weight(1f))
               AirQualityInfo(R.drawable.uv,"UV Index",data?.current?.uv.toString(),modifier = Modifier.weight(1f))
               AirQualityInfo(R.drawable.humidity,"Humidity","${data?.current?.humidity.toString()}%",modifier = Modifier.weight(1f))
               AirQualityInfo(R.drawable.wind_pressure,"Pressure","${data?.current?.pressure_mb.toString()}mb",modifier = Modifier.weight(1f))
               AirQualityConsInfo(R.drawable.concentration,"Concentration",data)




           }
       }

    }

}


@Composable
private fun AirQualityHeader(
    modifier: Modifier = Modifier
){
   Row(
       modifier = modifier.fillMaxWidth(),
       horizontalArrangement = Arrangement.SpaceBetween,
       verticalAlignment = Alignment.CenterVertically
   ){

       Row(
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.spacedBy(8.dp)
       ) {
           Icon(painter = painterResource(id = R.drawable.cloud), contentDescription = null,
               tint = ColorAirQualityIconTitle,
               modifier = Modifier.size(32.dp)

               )
           Text(text = "Air Quality",
                 style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
               color = Color.White
               )
       }
       RefreshButton()

   }
}

@Composable
private fun RefreshButton(
    modifier: Modifier = Modifier
){
     Surface(
         color = ColorSurface,
         shape = CircleShape,
         modifier = Modifier
             .size(32.dp)
             .shadow(5.dp, shape = CircleShape)
     ){
         Box(
             modifier = modifier.fillMaxSize(),
             contentAlignment = Alignment.Center
         ){
              Icon(painter = painterResource(id = R.drawable.refresh), contentDescription = null,
                  modifier.size(18.dp)
              )

         }
     }
}


@Composable
private fun AirQualityInfo(
    @DrawableRes icon:Int,
    title : String,
    data :String,
    modifier: Modifier = Modifier,

){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
         Image(painter = painterResource(icon), contentDescription = null,
             modifier = Modifier.size(24.dp)
             )
        Column(
            horizontalAlignment = Alignment.Start
        ){
            Text(text = title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(text = data,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun AirQualityConsInfo(
    @DrawableRes icon:Int,
    title : String,
    data :WeatherModel?,
    modifier: Modifier = Modifier,

    ){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(painter = painterResource(icon), contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start
        ){
            Text(text = title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(text = "CO- ${data?.current?.air_quality?.co.toString()} ppm",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(text = "NO2 -${data?.current?.air_quality?.no2.toString()} ppb",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(text ="SO2 -${data?.current?.air_quality?.so2.toString()} ppb" ,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

        }
    }
}

















