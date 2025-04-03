package com.example.dropenergy.EnterDialogScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dropenergy.ui.screens.CustomToastMessage
import com.example.dropenergy.R
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.viewmodel.DBViewModel
import com.example.dropenergy.ui.theme.LightGreen
import com.example.dropenergy.ui.theme.LightYellow

@Composable
fun CurrencyListItem(currency: String, isSelected: Boolean, onClick: () -> Unit){
    Row {
        Text(text = currency, modifier = Modifier.weight(1f))
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF1DB954),
                unselectedColor = Color.LightGray
            ),
            modifier = Modifier.size(24.dp)
        )
    }

}


@Composable
fun AskMoneyScreen(navController: NavHostController,viewModel: DBViewModel?){
    var inputText  by remember { mutableStateOf("") }
    var currencyText by remember { mutableStateOf("₽") }
    var showDialog by remember { mutableStateOf(false) }
    var buttonColor by remember { mutableStateOf(LightYellow) }
    val currencyList = listOf<String>("₽", "$", "Fr", "¥", "€", "£", "kr", "zł", "₺", "R")
    val error = stringResource(id = R.string.number_err)
    val signupState = viewModel?.signupFlow?.collectAsState()

    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    Column {
        LinearProgressIndicator(
            progress = 3/3.toFloat(),
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
                            text = stringResource(id = R.string.dialog_money_question),
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(60.dp))
                        Row(verticalAlignment = Alignment.CenterVertically){
                            OutlinedTextField(
                                value = inputText,
                                label = { Text(text = stringResource(id = R.string.price))},
                                singleLine = true,
                                keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Number),
                                onValueChange = {
                                    inputText = it
                                    buttonColor = LightGreen
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedButton(onClick = {
                                showDialog = true
                            }, modifier = Modifier.height(50.dp)) {
                                Text(text = currencyText, fontSize = 20.sp)
                            }

                        }

                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(stringResource(id = R.string.dialog_money_cur_question)) },
                            text = {
                                LazyColumn{
                                    items(items = currencyList){ currency ->
                                        CurrencyListItem(currency, currencyText == currency)
                                        {
                                            currencyText = currency
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    showDialog = false }) {
                                    Text(stringResource(id = R.string.ok))
                                }
                            }
                        )
                    }
                    Button(onClick = {
                        try {
                            viewModel?.addMoneyInf(currency = currencyText, money = inputText.toInt())
                            viewModel?.signup()
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
                signupState?.value.let {state ->
                    when(state){
                        is GetDBState.Success -> {
                            LaunchedEffect(Unit) {
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.navigate("progress")
                            }

                        }
                        is GetDBState.Loading -> CircularProgressIndicator()
                        is GetDBState.Failure ->{
                            errorMessage = stringResource(id = R.string.reg_error)
                            showToast = true
                        }
                        else -> {null}
                    }

                }
            }
        }

    }
}

