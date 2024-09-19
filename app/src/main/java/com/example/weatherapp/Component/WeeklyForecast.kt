package com.example.weatherapp.Component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.ColorGradient1
import com.example.weatherapp.ui.theme.ColorGradient2
import com.example.weatherapp.ui.theme.ColorGradient3
import com.example.weatherapp.ui.theme.ColorTextPrimary
import com.example.weatherapp.ui.theme.ColorTextPrimaryVariant
import com.example.weatherapp.ui.theme.ColorTextSecondary
import com.example.weatherapp.ui.theme.ColorTextSecondaryVariant
import com.example.weatherapp.Model.ForecastItem

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun extractDayName(dateString: String): List<String> {
    val dateObject = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val dayName = dateObject.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
    val monthName = dateObject.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
    val dayNameAbb = dayName.substring(0,3)
    val monthNameAbb = monthName.substring(0,3)
    val list = listOf<String>(dayNameAbb,monthNameAbb)
    return list
}
@Composable
fun WeeklyForecast(
    data: List<ForecastItem>,
    modifier: Modifier = Modifier,

    ){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
       WeatherForecastHeader()
        data.forEach { item ->

            Log.d("Day",item.date)
        }
        LazyRow (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ){

            itemsIndexed(data){ index,item ->
                 Forecast(item = item)
            }
        }
    }
}
@Composable
private fun WeatherForecastHeader(
    modifier: Modifier= Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
         Text(text = "Weekly forecast",
              style =  MaterialTheme.typography.titleLarge,
             color = Color.LightGray,
             fontWeight = FontWeight.Bold,
             fontSize = 20.sp
             )
          ActionText()
    }
}

@Composable
private fun ActionText(
    modifier: Modifier  = Modifier
){
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ){
        Text(text ="Next Month",
            style = MaterialTheme.typography.titleSmall,
            color = Color.LightGray,
            fontWeight = FontWeight.Medium
        )
        Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "right icon",
            tint = Color.LightGray,
            modifier = Modifier.size(20.dp)

        )

    }
}
@Composable
private fun Forecast(
    modifier: Modifier = Modifier,
    item: ForecastItem
){
    val currentDate = remember { LocalDate.now() }
    val isSelected = remember {
        item.date.toString().split(" ")[0] == currentDate.toString().split("-")[2]
    }
    val updatedModifier = remember(isSelected){
        if(isSelected){
            modifier.background(
                shape = RoundedCornerShape(5.dp),
                brush = Brush.linearGradient(listOf(ColorGradient1, ColorGradient2, ColorGradient3))
            )
        }else{
            modifier
        }
    }
    val primaryTextColor = remember(isSelected) {
        if(isSelected) ColorTextSecondary else ColorTextPrimary
    }
    val secondaryTextColor = remember(isSelected) {
        if(isSelected) ColorTextSecondaryVariant else ColorTextPrimaryVariant
    }
    val temperatureTextStyle = remember(isSelected) {


        if (isSelected) {
            TextStyle(
                brush = Brush.verticalGradient(listOf(Color.White, Color.White.copy(alpha = 0.3f))),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        } else {
            TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black

            )
        }
    }

    Column (
         modifier = updatedModifier
             .width(70.dp)
             .padding(horizontal = 10.dp, vertical = 16.dp)
    ){
        Text(text = item.dayOfWeek,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
            )
        Text(text = item.date,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier =  Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherImage(item)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text ="${item.temperature}°",
            style = temperatureTextStyle
            )
        //AirQualityIndicator(value = item.airQuality, color = item.airQualityIndicatorColorHex)
    }
}

@Composable
private fun WeatherImage(
    item: ForecastItem,
    modifier: Modifier = Modifier,

    ){
           Box(
               modifier = modifier
                   .fillMaxWidth()
                   .height(60.dp),
               contentAlignment = Alignment.Center
           ){
               AsyncImage(
                   model = "https:${item.image}".replace("64x64","128x128"),
                   contentDescription = "weather image",
                     contentScale = ContentScale.FillWidth,
                     modifier = Modifier.fillMaxWidth()

                   )
           }
}
//@Composable
//private fun AirQualityIndicator(
//    modifier : Modifier = Modifier,
//    value: String,
//    color: String
//){
//     Surface(
//         modifier = modifier,
//         color =Color.fromHex(color),
//         contentColor = ColorTextSecondary,
//         shape = RoundedCornerShape(8.dp)
//
//     ) {
//          Box(
//              modifier = Modifier
//                  .width(35.dp)
//                  .padding(vertical = 2.dp),
//              contentAlignment = Alignment.Center
//          ) {
//               Text(text = value
//               )
//          }
//
//     }
//}

