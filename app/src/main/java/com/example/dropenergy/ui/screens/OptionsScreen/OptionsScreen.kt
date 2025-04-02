package com.example.dropenergy.OptionsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dropenergy.ui.components.OptionsFunction
import com.example.dropenergy.viewmodel.DBViewModel
import com.example.dropenergy.R



@Composable
fun OptionsScreen(navController: NavHostController, viewModel: DBViewModel){
    val optList = listOf(
        OptionsFunction(
            title = stringResource(id = R.string.logout)
        ) { viewModel.logout()
            navController.popBackStack()
            navController.popBackStack()
            navController.popBackStack()
            navController.navigate("reg")
        }

    )

    Column {
        Text(text = stringResource(id = R.string.options),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(14.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 6.dp),
        ) {
            LazyColumn {
                items(optList.size) {
                    val option = optList[it]
                    Column(
                        Modifier
                            .padding(start = 13.dp)
                            .clickable { option.function() }
                            .fillMaxWidth()){
                        Text(text = option.title,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }

}