package com.example.dropenergy.EnterDialogScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dropenergy.ui.screens.CustomToastMessage
import com.example.dropenergy.R
import com.example.dropenergy.viewmodel.DBViewModel
import com.example.dropenergy.ui.theme.LightGreen
import com.example.dropenergy.ui.theme.LightYellow


@Composable
fun AskCansScreen(navController: NavHostController,viewModel: DBViewModel){
    var inputText  by remember {mutableStateOf("")}
    var buttonColor by remember { mutableStateOf(LightYellow) }
    val error = stringResource(id = R.string.number_err)

    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column {
        LinearProgressIndicator(
            progress = 2/3.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Box(modifier = Modifier.fillMaxWidth()){
            CustomToastMessage(
                message = errorMessage,
                isVisible = showToast,
                onDismiss = { showToast = false },
            )
            Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
                Column( modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(60.dp))
                        Text(
                            text = stringResource(id = R.string.dialog_cans_question),
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(60.dp))
                        OutlinedTextField(
                            value = inputText,
                            label = { Text(text = stringResource(id = R.string.count))},
                            singleLine = true,
                            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                inputText = it
                                buttonColor = LightGreen
                            })
                    }
                    Button(
                        onClick = {
                            try{
                                viewModel.addCans(inputText.toInt())
                                navController.navigate("dialog_money")
                            }catch (e:java.lang.NumberFormatException){
                                errorMessage = error
                                showToast = true
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text(text = stringResource(id = R.string.next))

                    }
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }

    }
}