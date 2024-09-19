package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.Model.WeatherModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    weatherResult: State<WeatherModel?>
){

    val keyboardController = LocalSoftwareKeyboardController.current
  var city by remember {
    mutableStateOf("")
  }
    var hasSearched by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.resetWeatherResult()   //reset weatherResult so that we can open searchScreen otherwise it will open SecondScreen Directly.
        hasSearched = true
    }

    // Handle navigation based on search result
    LaunchedEffect(weatherResult.value, hasSearched) {
        if (hasSearched && weatherResult.value != null) {
            navController.navigate(SecondWeatherScreen.route)
            hasSearched = false  // Reset the flag after navigation
        }
    }

 Column(modifier = Modifier
     .fillMaxSize()
     .background(Color.White),

 ) {
  Column(modifier = Modifier
      .fillMaxWidth()
      .padding(start = 20.dp, top = 50.dp)
  ) {
   IconButton(onClick = {
       navController.navigate(WeatherScreen.route)
   }) {
    Icon(painter = painterResource(id = R.drawable.icons8_left_arrow_32), contentDescription = null, modifier = Modifier.size(30.dp))
   }
   Spacer(modifier = Modifier.height(12.dp))
      Text(text = "Search Cities", style = MaterialTheme.typography.displaySmall.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Light)
      )
      Spacer(modifier = Modifier.height(12.dp))
       TextField(modifier = Modifier
           .padding(end = 20.dp)
           .fillMaxWidth(), value = city, onValueChange = {city = it},
           placeholder = { Text(text = "Enter location", color = Color.Gray)
           },
           leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Gray)},
           trailingIcon = {
               if(city.isNotEmpty()){
                   IconButton(onClick = { city="" }) {
                       Icon(imageVector =Icons.Filled.Clear , contentDescription =null, modifier= Modifier
                           .size(20.dp)
                           .background(shape = CircleShape, color = Color.Black.copy(.2f)) )
                   }
               }
           },
           colors = TextFieldDefaults.textFieldColors(unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, containerColor = Color.LightGray.copy(.3f)),
           shape = RoundedCornerShape(25.dp),
           keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
           keyboardActions = KeyboardActions(onSearch ={
               if (city.isNotEmpty()) {
                   viewModel.getData(city)  // Assume this method exists in your ViewModel
                   keyboardController?.hide()
               }
               keyboardController?.hide()}
           )
       )
  }

 }
}