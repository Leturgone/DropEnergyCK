package com.example.dropenergy.ProgressScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dropenergy.ui.screens.CustomToastMessage
import com.example.dropenergy.R
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.viewmodel.DBViewModel


@Composable
fun DailyCheckSection(viewModel: DBViewModel) {
    var week by remember { mutableStateOf(listOf<Pair<String, Boolean>>()) }

    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getWeek()
    }
    Box(modifier = Modifier.fillMaxWidth()){
        CustomToastMessage(
            message = errorMessage,
            isVisible = showToast,
            onDismiss = { showToast = false },
        )
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.daily_check),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp))

            //Облако с днями недели
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)){

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {}
                        .padding(10.dp)
                ) {

                    Text(text = stringResource(id = R.string.this_week),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp))
                    viewModel.weekFlow.collectAsState().value.let {state ->
                        when(state){
                            is GetDBState.Success -> {
                                week = state.result.toList()
                                WeekSection(week = week)
                            }
                            is GetDBState.Loading -> CircularProgressIndicator()
                            is GetDBState.Failure -> {
                                week =viewModel.dayCheckMap.toList()
                                WeekSection(week = week)
                                errorMessage = stringResource(id = R.string.loading_week_err)
                                showToast = true
                            }
                            else -> {null}
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun WeekSection(week: List<Pair<String, Boolean>>){
    //Список с днями и чеками входа
    LazyRow(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround){
        items(week.size){
            val record = week[it]
            val day = stringArrayResource(id = R.array.days_of_week)[it]
            val check = record.second

            Column(verticalArrangement = Arrangement.Center) {
                var tint = MaterialTheme.colorScheme.secondaryContainer
                if (check){
                    tint = Color.Green
                }
                Icon(imageVector = Icons.Rounded.CheckCircleOutline,
                    tint = tint, contentDescription = "Yes")
                Text(text = day,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(4.dp))
            }

        }
    }
}


