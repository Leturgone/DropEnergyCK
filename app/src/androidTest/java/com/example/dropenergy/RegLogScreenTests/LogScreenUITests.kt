package com.example.dropenergy.RegLogScreenTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.dropenergy.LogScreen
import com.example.dropenergy.MainScreen
import com.example.dropenergy.RegScreen
import com.example.dropenergy.StatScreen
import com.example.dropenergy.data.repository.AuthRepository
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.data.repository.UserRepository
import com.example.dropenergy.presentation.viewmodel.DBViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LogScreenUITests{
    @get:Rule
    val composeTestRule = createComposeRule()
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private lateinit var viewModel: DBViewModel

    @Before
    fun setUP(){
        coEvery {authRepository.login("giovanni18@pochta.com", "12345678")
        } returns GetDBState.Success(mockk())
        coEvery {authRepository.login("1 1 1", "12345678")
        } returns GetDBState.Failure(mockk())
        coEvery {authRepository.login("giovanni18@pochta.com", "1")
        } returns GetDBState.Failure(mockk())
        coEvery {authRepository.login("giovanni18@pochta.com", "1         ")
        } returns GetDBState.Failure(mockk())
        every { authRepository.getCurrentUser() } returns null
        coEvery {authRepository.login("notlogged@pochta.com", "12345678")
        } returns GetDBState.Failure(mockk())
        viewModel = DBViewModel(authRepository, userRepository)
    }

    @Test
    fun testLogScreensGood(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }

            //RegisterScreen
            onNode(RegScreen.RegTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).performClick()
            //LoginScreen
            onNode(LogScreen.LogTemplate).assertExists()
            onNode(LogScreen.EmailInput).assertExists()
            onNode(LogScreen.PasswordInput).assertExists()
            onNode(LogScreen.NextButton).assertExists()
            onNode(LogScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(LogScreen.PasswordInput).performTextInput("12345678")
            onNode(LogScreen.NextButton).performClick()
            onNode(StatScreen.ScreenDayCecTemplate).assertExists()

        }

    }

    @Test
    fun testLogScreensBadEmail(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }

            onNode(RegScreen.RegTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).performClick()
            //LoginScreen
            onNode(LogScreen.LogTemplate).assertExists()
            onNode(LogScreen.EmailInput).assertExists()
            onNode(LogScreen.PasswordInput).assertExists()
            onNode(LogScreen.NextButton).assertExists()
            onNode(LogScreen.EmailInput).performTextInput("1 1 1")
            onNode(LogScreen.PasswordInput).performTextInput("12345678")
            onNode(LogScreen.NextButton).performClick()
            onNode(LogScreen.EmailErrorToast).assertExists()
            onNode(StatScreen.ScreenDayCecTemplate).assertDoesNotExist()
        }

    }

    @Test
    fun testLogScreensShortPassword() {

        composeTestRule.run {
            setContent { MainScreen(viewModel) }

            onNode(RegScreen.RegTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).performClick()
            //LoginScreen
            onNode(LogScreen.LogTemplate).assertExists()
            onNode(LogScreen.EmailInput).assertExists()
            onNode(LogScreen.PasswordInput).assertExists()
            onNode(LogScreen.NextButton).assertExists()
            onNode(LogScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(LogScreen.PasswordInput).performTextInput("1")
            onNode(LogScreen.NextButton).performClick()
            onNode(LogScreen.ShortPasswordErrorToast).assertExists()
            onNode(StatScreen.ScreenDayCecTemplate).assertDoesNotExist()
        }

    }

    @Test
    fun testLogScreensBadPassword() {

        composeTestRule.run {
            setContent { MainScreen(viewModel) }

            onNode(RegScreen.RegTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).performClick()
            //LoginScreen
            onNode(LogScreen.LogTemplate).assertExists()
            onNode(LogScreen.EmailInput).assertExists()
            onNode(LogScreen.PasswordInput).assertExists()
            onNode(LogScreen.NextButton).assertExists()
            onNode(LogScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(LogScreen.PasswordInput).performTextInput("1         ")
            onNode(LogScreen.NextButton).performClick()
            onNode(LogScreen.PasswordErrorToast).assertExists()
            onNode(StatScreen.ScreenDayCecTemplate).assertDoesNotExist()
        }

    }

    @Test
    fun testLogScreensBadUser(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }

            onNode(RegScreen.RegTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).performClick()
            //LoginScreen
            onNode(LogScreen.LogTemplate).assertExists()
            onNode(LogScreen.EmailInput).assertExists()
            onNode(LogScreen.PasswordInput).assertExists()
            onNode(LogScreen.NextButton).assertExists()
            onNode(LogScreen.EmailInput).performTextInput("notlogged@pochta.com")
            onNode(LogScreen.PasswordInput).performTextInput("12345678")
            onNode(LogScreen.NextButton).performClick()
            onNode(LogScreen.LoginErrorToast).assertExists()
            onNode(StatScreen.ScreenDayCecTemplate).assertDoesNotExist()
        }

    }

}