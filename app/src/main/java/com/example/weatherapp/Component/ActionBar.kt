package com.example.weatherapp.Component


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.SearchScreen
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.ui.theme.ColorGradient1
import com.example.weatherapp.ui.theme.ColorGradient2
import com.example.weatherapp.ui.theme.ColorGradient3
import com.example.weatherapp.ui.theme.ColorImageShadow
import com.example.weatherapp.ui.theme.ColorSurface
import com.example.weatherapp.ui.theme.ColorTextPrimaryVariant

@Composable
fun SearchButton(viewModel: WeatherViewModel,navController: NavController){
    Box(modifier = Modifier
        .size(48.dp),
        contentAlignment = Alignment.Center
    ){
        Card(
          shape = CircleShape,
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(5.dp)

        ) {
            IconButton(onClick = {
                Log.d("SearchButton", "Search button clicked")
                navController.navigate(SearchScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search",modifier = Modifier.size(30.dp))
        }

        }

    }
}




@Composable
fun ProfileButton(
    modifier: Modifier = Modifier
){
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
fun LocationInfo(
    modifier: Modifier = Modifier,
    data: WeatherModel?,



    ){
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


val GradientColor = listOf(
     ColorGradient1,
     ColorGradient2,
     ColorGradient3
)
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