package com.example.dropenergy.EnterDialogScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

//@Preview(showBackground = true)
@Composable
fun RegScreen(navController: NavHostController, viewModel: DBViewModel){
    var loginInputText  by remember { mutableStateOf("") }
    var passwordInputText  by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginOK by remember { mutableStateOf(false) }
    var buttonColor by remember { mutableStateOf(LightYellow) }

    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val loginState = viewModel.loginFlow?.collectAsState()
    loginState?.value.let {state ->
        when(state){
            is GetDBState.Success -> {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("progress")

                }

            }
            else -> {null}
        }

    }


    val emailErr = stringResource(id = R.string.email_err)
    val passwordErr = stringResource(id = R.string.password_err)
    val shortPasswordErr = stringResource(id = R.string.password_short_err)

    Column {
        LinearProgressIndicator(
            progress = 1/3.toFloat(),
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
                            text = stringResource(id = R.string.registration),
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(60.dp))

                        OutlinedTextField(
                            value = loginInputText,
                            label = { Text(stringResource(id = R.string.email)) },
                            singleLine = true,
                            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Email),
                            onValueChange = {
                                loginInputText = it
                                loginOK = true
                            })
                        Spacer(modifier = Modifier.height(60.dp))

                        OutlinedTextField(
                            value = passwordInputText,
                            singleLine = true,
                            label = { Text(stringResource(id = R.string.password)) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Password),
                            onValueChange = {
                                passwordInputText = it
                                if (loginOK){
                                    buttonColor = LightGreen
                                }
                            },
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = {passwordVisible = !passwordVisible}){
                                    Icon(imageVector  = image, description)
                                }
                            })
                    }

                    Button(onClick = {
                        if( loginInputText.isEmpty() || !loginInputText.matches("^[A-Za-z0-9@.]+$".toRegex())
                            || loginInputText.matches("\\s".toRegex())){
                            errorMessage = emailErr
                            showToast = true
                        }
                        else if( passwordInputText.isEmpty() || !passwordInputText.matches("^[A-Za-z0-9]+$".toRegex())
                            || passwordInputText.matches("\\s".toRegex())){
                            errorMessage = passwordErr
                            showToast = true
                        }
                        else if (passwordInputText.length < 8){
                            errorMessage = shortPasswordErr
                            showToast = true
                        }
                        else {
                            //Загрузка в бд
                            viewModel.createUser(loginInputText,passwordInputText)
                            navController.navigate("dialog_cans")

                        }

                    },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text(text = stringResource(id = R.string.next))
                    }
                    Text(text = stringResource(id = R.string.already_have),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            navController.navigate("login")
                        })

                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}