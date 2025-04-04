package com.example.dropenergy.ProgressScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dropenergy.ui.screens.CustomToastMessage
import com.example.dropenergy.R
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.viewmodel.DBViewModel

@Composable
fun CanScreen(viewModel: DBViewModel){
    var ekonomCan by remember { mutableIntStateOf(0) }
    var inDayCan by remember { mutableIntStateOf(0) }
    var inWeekCan by remember { mutableIntStateOf(0) }
    var inMonthCan by remember { mutableIntStateOf(0) }
    var inYearCan by remember { mutableIntStateOf(0) }


    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.getSavedCans()
        viewModel.getEverydayCans()
    }

    Box(modifier = Modifier.fillMaxWidth()){
        CustomToastMessage(
            message = errorMessage,
            isVisible = showToast,
            onDismiss = { showToast = false },
        )
        Column {
            Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = stringResource(id = R.string.saved_cans),
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    viewModel.savedCansFlow.collectAsState().value.let {state ->
                        when(state){
                            is GetDBState.Success -> {
                                ekonomCan = state.result
                                SavedCans(ekonomCan = ekonomCan)
                            }
                            is GetDBState.Loading ->  CircularProgressIndicator()
                            is GetDBState.Failure -> {
                                errorMessage = stringResource(id = R.string.loading_saved_cans_err)
                                showToast = true
                            }
                            else -> {null}
                        }
                    }

                }

            }

            Text(text = stringResource(id = R.string.predict),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp))

            Column(Modifier.padding(start = 16.dp)) {
                viewModel.everyDayCansFlow.collectAsState().value.let {state ->
                    when(state){
                        is GetDBState.Success -> {
                            inDayCan = state.result
                            inWeekCan = inDayCan * 7
                            inMonthCan = inDayCan * 30
                            inYearCan = inDayCan * 365

                            FutureCans(
                                inDayCan = inDayCan,
                                inWeekCan = inWeekCan,
                                inMonthCan = inMonthCan,
                                inYearCan =inYearCan
                            )
                        }
                        is GetDBState.Loading -> CircularProgressIndicator(Modifier.padding(16.dp))
                        is GetDBState.Failure -> {
                            errorMessage = stringResource(id = R.string.loading_everyday_cans_err)
                            showToast = true
                        }
                        else -> {null}
                    }
                }


            }

        }
    }

}


@Composable
fun SavedCans(ekonomCan: Int){
    Text(
        text = "$ekonomCan ${stringResource(id = R.string._cans)}",
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}


@Composable
fun FutureCans(inDayCan: Int, inWeekCan: Int, inMonthCan:Int, inYearCan:Int ){
    Text(text = "$inDayCan ${stringResource(id = R.string._cans)} ${stringResource(id = R.string.in_day)}",
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp))

    Text(text = "$inWeekCan ${stringResource(id = R.string._cans)} ${stringResource(id = R.string.in_week)} ",
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp))

    Text(text = "$inMonthCan ${stringResource(id = R.string._cans)} ${stringResource(id = R.string.in_month)} ",
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp))

    Text(text = "$inYearCan ${stringResource(id = R.string._cans)} ${stringResource(id = R.string.in_year)} ",
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp))
}

