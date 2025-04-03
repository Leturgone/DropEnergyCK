package com.example.dropenergy.DiaryScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dropenergy.ui.screens.CustomToastMessage
import com.example.dropenergy.R
import com.example.dropenergy.domain.model.DiaryRecord
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.viewmodel.DBViewModel


@Composable
fun DiaryScreen(viewModel: DBViewModel){
    var diary by remember { mutableStateOf(listOf<Pair<String, DiaryRecord>>()) }

    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getDiary()
    }
    Box(modifier = Modifier.fillMaxWidth()){
        CustomToastMessage(
            message = errorMessage,
            isVisible = showToast,
            onDismiss = { showToast = false },
        )
        Column {
            Text(text = stringResource(id = R.string.diary),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                viewModel.diaryFlow.collectAsState().value.let {state ->
                    when(state){
                        is GetDBState.Success -> {
                            diary = state.result.toList()
                            DiaryList(diary = diary)
                        }
                        is GetDBState.Loading -> CircularProgressIndicator()
                        is GetDBState.Failure -> {
                            errorMessage = stringResource(id = R.string.loading_diary_err)
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
fun DiaryList(diary: List<Pair<String, DiaryRecord>>){
    LazyColumn(modifier = Modifier.padding(6.dp)) {
        items(diary.size) {
            val record = diary[it]
            Column{
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Circle,
                        modifier = Modifier
                            .size(15.dp)
                            .padding(start = 6.dp),
                        tint = Color.LightGray,
                        contentDescription = "Icon"
                    )
                    Row {
                        Text(
                            text = record.second.date,
                            Modifier.padding(start = 8.dp)
                        )
                        Text(
                            text =
                            when(record.second.recordColor){
                                "green" -> stringResource(id = R.string.green_diary_record)
                                "red" -> stringResource(id = R.string.red_diary_record)
                                "yellow" -> stringResource(id = R.string.yellow_diary_record)
                                else -> { stringResource(id = R.string.first_diary_record)}
                            },
                            Modifier.padding(start = 6.dp)
                        )

                    }
                }
                if(record.second.intensive!="") {
                    Text(
                        text = "${stringResource(id = R.string.intensity)}: ${record.second.intensive}",
                        Modifier.padding(start = 23.dp, top = 6.dp)
                    )
                }
            }
        }
    }
}

























