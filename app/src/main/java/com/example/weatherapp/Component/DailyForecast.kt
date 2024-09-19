package com.example.weatherapp.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.ui.theme.ColorGradient2
import com.example.weatherapp.ui.theme.ColorGradient3
import com.example.weatherapp.ui.theme.ColorTextSecondary
import com.example.weatherapp.ui.theme.ColorTextSecondaryVariant
import com.example.weatherapp.ui.theme.ColorWindForecast

@Composable
fun DailyForecast(
    data: WeatherModel?,
    ){
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (forecastImage, forecastValue, windImage, title,time, description, background) = createRefs()

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
            modifier = Modifier.size(160.dp)
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
              modifier = Modifier.padding(start = 20.dp).constrainAs(title){
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
fun CardBackground(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(ColorGradient2, ColorGradient2, ColorGradient3)
                ),
                shape = RoundedCornerShape(5.dp)
            )
    ) {

    }
}

@Composable
private fun ForestValue(
    modifier:Modifier = Modifier,
    data: WeatherModel?
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

         Text(text ="Feels like ${data?.current?.feelslike_c.toString()}",
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








