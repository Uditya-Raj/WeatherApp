package com.example.weatherapp

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.AsyncImage


import com.example.weatherapp.Component.CardBackground

import com.example.weatherapp.Component.GradientColor

import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.ui.theme.ColorAirQualityIconTitle
import com.example.weatherapp.ui.theme.ColorGradient1
import com.example.weatherapp.ui.theme.ColorGradient2
import com.example.weatherapp.ui.theme.ColorGradient3
import com.example.weatherapp.ui.theme.ColorImageShadow
import com.example.weatherapp.ui.theme.ColorSurface
import com.example.weatherapp.ui.theme.ColorTextPrimary
import com.example.weatherapp.ui.theme.ColorTextPrimaryVariant
import com.example.weatherapp.ui.theme.ColorTextSecondary
import com.example.weatherapp.ui.theme.ColorTextSecondaryVariant
import com.example.weatherapp.ui.theme.ColorWindForecast
import com.example.weatherapp.Model.ForecastItem
import java.time.LocalDate

@Composable
fun SecondWeatherScreen(
    navController: NavController,
    data: WeatherModel?,
    forecastItemList: MutableList<ForecastItem>,
    viewModel: WeatherViewModel
) {

    Scaffold {

        Column(modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff548FD1)
            )
            .padding(paddingValues = it)
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState())
        ) {
            //ActionBar()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    tonalElevation = 5.dp

                ) {
                    IconButton(onClick = {
                       viewModel.resetWeatherResult()
                        navController.navigate(WeatherScreen.route){
                            popUpTo(WeatherScreen.route){
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back arrow",
                            modifier = Modifier.size(40.dp), tint = Color.LightGray
                        )
                }

                }
                SecondScreenLocationInfo(
                    modifier = Modifier.padding(top = 10.dp),
                    data)
                SecondScreenProfileButton()

            }

            Spacer(modifier = Modifier.height(12.dp))
            SecondScreenDailyForecast(data)
            Spacer(modifier = Modifier.height(12.dp))
            SecondScreenAirQuality(data)
            Spacer(modifier = Modifier.height(12.dp))
           SecondScreenWeeklyForecast(forecastItemList)

        }
    }
}

@Composable
fun SecondScreenLocationInfo(modifier: Modifier = Modifier,
                             data: WeatherModel?){
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(imageVector = Icons.Default.LocationOn , contentDescription = "location" )
            Text(text = data?.location?.name.toString(), style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
        ProgressBar()


    }
}

@Composable
private fun ProgressBar(
){
    Box(modifier = Modifier
        .background(
            brush = Brush.linearGradient(colors = GradientColor),
            shape = RoundedCornerShape(8.dp)
        )
        .padding(horizontal = 10.dp, vertical = 2.dp)


    ){
        Text(text = "Updating .", style = MaterialTheme.typography.labelSmall, color = ColorTextPrimaryVariant)
    }
}
@Composable
fun SecondScreenProfileButton(modifier: Modifier = Modifier){
    Box(modifier = modifier
        .size(48.dp)
        .border(
            width = 1.5.dp,
            color = ColorSurface,
            shape = CircleShape
        )
        .shadow(
            elevation = 5.dp,
            shape = CircleShape,
            ambientColor = ColorImageShadow,
        )

    ){
        Image(painter = painterResource(id = R.drawable.img_profile) , contentDescription = null)
    }
}

@Composable
fun SecondScreenDailyForecast(
    data: WeatherModel?
){
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (forecastImage, forecastValue, windImage, title, description, background) = createRefs()

        CardBackground(
            modifier = Modifier.constrainAs(background){
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    top = parent.top,
                    bottom = description.bottom,
                    topMargin =  40.dp
                )
                height = Dimension.fillToConstraints
            }
        )

        AsyncImage(

            model = "https:${data?.current?.condition?.icon}".replace("64x64","128x128")
            ,contentDescription = "Condition Icon",
            modifier = Modifier
                .size(160.dp)
                .height(175.dp)
                .constrainAs(forecastImage) {
                    start.linkTo(parent.start, margin = 4.dp)
                    top.linkTo(parent.top)
                }
        )

        Text(text = data?.current?.condition?.text.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = ColorTextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 20.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start, margin = 40.dp)
                    top.linkTo(forecastImage.bottom)
                }
        )
        Text(text = data?.location?.localtime.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = ColorTextSecondaryVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .constrainAs(description) {
                    start.linkTo(title.start)
                    top.linkTo(title.bottom, margin = 20.dp)
                }
                .padding(bottom = 24.dp)
        )

        ForestValue(
            modifier = Modifier.constrainAs(forecastValue){
                end.linkTo(parent.end, margin = 24.dp)
                top.linkTo(forecastImage.top)
                bottom.linkTo(forecastImage.bottom)
            },
            data
        )
        WindForecastImage(
            modifier = Modifier.constrainAs(windImage){
                linkTo(
                    top = title.top,
                    bottom = title.bottom
                )
                end.linkTo(parent.end, margin = 24.dp)
            }
        )

    }
}

@Composable
private fun ForestValue(
    modifier:Modifier = Modifier,
    data : WeatherModel?
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = data?.current?.temp_c.toString(),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        listOf(Color.White, Color.White.copy(0.3f))
                    )
                ),
                fontSize = 80.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(end = 16.dp)
            )

            Text(
                text = "°",
                style = TextStyle(
                    brush = Brush.linearGradient(
                        listOf(Color.White, Color.White.copy(0.3f))
                    )
                ),
                fontSize = 70.sp,
                fontWeight = FontWeight.Light
            )
        }

        Text(text = "Feels like ${data?.current?.feelslike_c.toString()}",
            style = MaterialTheme.typography.bodyMedium,
            color = ColorTextSecondaryVariant
        )
    }

}

@Composable
private fun WindForecastImage(
    modifier:Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = R.drawable.wind), contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = ColorWindForecast
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SecondScreenAirQuality(
  data: WeatherModel?,
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
                AirQualityInfo(
                    R.drawable.feel,
                    "Real Feel",
                    "${data?.current?.windchill_c.toString()}°",
                    modifier = Modifier.weight(1f)
                )
                AirQualityInfo(
                    R.drawable.img_cloudy,
                    "Cloud",
                    "${data?.current?.cloud.toString()} %",
                    modifier = Modifier.weight(1f)
                )
                AirQualityInfo(
                    R.drawable.wind,
                    "Wind",
                    "${data?.current?.wind_mph.toString()} mph",
                    modifier = Modifier.weight(1f)
                )
               AirQualityInfo(
                    R.drawable.uv,
                    "UV Index",
                    data?.current?.uv.toString(),
                    modifier = Modifier.weight(1f)
                )
               AirQualityInfo(
                    R.drawable.humidity,
                    "Humidity",
                    "${data?.current?.humidity.toString()}%",
                    modifier = Modifier.weight(1f)
                )
                AirQualityInfo(
                    R.drawable.wind_pressure,
                    "Pressure",
                    "${data?.current?.pressure_mb.toString()}mb",
                    modifier = Modifier.weight(1f)
                )
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

@Composable
fun SecondScreenWeeklyForecast(
    data: List<ForecastItem>,
    modifier: Modifier = Modifier

){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        WeatherForecastHeader()
        LazyRow (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ){
            items(data){ item ->
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
     val currentData = remember { LocalDate.now()}
     val isSelected = remember {
         item.date.toString().split(" ")[0] ==currentData.toString().split("-")[2]
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
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherImage(item)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "${item.temperature}°",
            style = temperatureTextStyle
        )

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
