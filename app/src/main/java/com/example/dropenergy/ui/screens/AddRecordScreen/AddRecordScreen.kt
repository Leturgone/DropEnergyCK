package com.example.dropenergy.AddRecordScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dropenergy.R
import com.example.dropenergy.ui.theme.LightGreen
import com.example.dropenergy.ui.theme.LightRed
import com.example.dropenergy.ui.theme.LightYellow

//Окно с большими разноцветными панелями для создания записи
@Composable
fun AddRecordScreen(navController: NavController){
    Column {
        Text(text = stringResource(id = R.string.create_record),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(
                    onClick = {
                        navController.navigate("yellow_rec")
                    })) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(LightYellow)
                        .padding(10.dp)
                        .fillMaxWidth(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.yellow_diary_record),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = {
                    navController.navigate("red_rec")
                })) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(LightRed)
                        .padding(10.dp)
                        .fillMaxWidth(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.red_diary_record),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = {
                    navController.navigate("green_rec")
                })) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(LightGreen)
                        .padding(10.dp)
                        .fillMaxWidth(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.green_diary_record),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}
