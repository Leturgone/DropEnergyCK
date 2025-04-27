package com.example.dropenergy.RegLogScreenTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dropenergy.DCansScreen
import com.example.dropenergy.DMoneyScreen
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
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegScreenUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private lateinit var viewModel: DBViewModel

    @Before
    fun setUP(){
        coEvery {authRepository.signup("giovanni18@pochta.com",
            "12345678")
        } returns GetDBState.Success(mockk())
        coEvery {authRepository.signup("1 1 1", "12345678")
        } returns GetDBState.Failure(mockk())
        coEvery {authRepository.signup("giovanni18@pochta.com", "1")
        } returns GetDBState.Failure(mockk())
        coEvery {authRepository.signup("giovanni18@pochta.com", "1         ")
        } returns GetDBState.Failure(mockk())
        every { authRepository.getCurrentUser() } returns null
        coEvery {authRepository.signup("logged@pochta.com", "12345678")
        } returns GetDBState.Failure(mockk())
        viewModel = DBViewModel(authRepository, userRepository)
    }

    @Test
    fun testRegScreensGood(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }

            //RegisterScreen
            onNode(RegScreen.RegTemplate).assertExists()
            onNode(RegScreen.AlreadyTemplate).assertExists()
            onNode(RegScreen.EmailInput).assertExists()
            onNode(RegScreen.NextButton).assertExists()
            onNode(RegScreen.PasswordInput).assertExists()
            onNode(RegScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(RegScreen.PasswordInput).performTextInput("12345678")
            onNode(RegScreen.NextButton).performClick()

            //DialogCansScreen
            onNode(DCansScreen.regCansTemplate).assertExists()
            onNode(DCansScreen.countInput).assertExists()
            onNode(DCansScreen.nextButton).assertExists()
            onNode(DCansScreen.countInput).performTextInput("2")
            onNode(DCansScreen.nextButton).performClick()

            //DialogMoneyScreen
            onNode(DMoneyScreen.regMoneyTemplate).assertExists()
            onNode(DMoneyScreen.priceInput).assertExists()
            onNode(DMoneyScreen.currencyButton).assertExists()
            onNode(DMoneyScreen.nextButton).assertExists()
            onNode(DMoneyScreen.priceInput).performTextInput("60")
            onNode(DMoneyScreen.nextButton).performClick()
            onNode(StatScreen.ScreenDayCecTemplate).assertExists()
        }


    }

    @Test
    fun testRegScreenBadEmail(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onNode(RegScreen.EmailInput).performTextInput("1 1 1")
            onNode(RegScreen.PasswordInput).performTextInput("12345678")
            onNode(RegScreen.NextButton).performClick()
            onNode(RegScreen.EmailErrorToast).assertExists()
            onNode(DCansScreen.regCansTemplate).assertDoesNotExist()
        }


    }

    @Test
    fun testRegScreenBadPassword() {

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onNode(RegScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(RegScreen.PasswordInput).performTextInput("1         ")
            onNode(RegScreen.NextButton).performClick()
            onNode(RegScreen.PasswordErrorToast).assertExists()
            onNode(DCansScreen.regCansTemplate).assertDoesNotExist()
        }

    }
    @Test
    fun testRegScreenShortPassword() {
        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onNode(RegScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(RegScreen.PasswordInput).performTextInput("1")
            onNode(RegScreen.NextButton).performClick()
            onNode(RegScreen.ShortPasswordErrorToast).assertExists()
            onNode(DCansScreen.regCansTemplate).assertDoesNotExist()
        }

    }

    @Test
    fun testRegScreenBadCans(){
        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("MY TAG")
            onNode(RegScreen.EmailInput).performTextInput("giovanni18@pochta.com")
            onNode(RegScreen.PasswordInput).performTextInput("12345678")
            onNode(RegScreen.NextButton).performClick()

            //DialogCansScreen
            onNode(DCansScreen.regCansTemplate).assertExists()
            onNode(DCansScreen.countInput).assertExists()
            onNode(DCansScreen.nextButton).assertExists()
            onNode(DCansScreen.countInput).performTextInput("2   ")
            onNode(DCansScreen.nextButton).performClick()
            onNode(DCansScreen.NumberError).assertExists()
            onNode(DMoneyScreen.regMoneyTemplate).assertDoesNotExist()
        }

    }

    @Test
    fun testRegScreenBadMoney(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("MY TAG")

            //RegisterScreen
            onNode(RegScreen.EmailInput).performTextInput("logged@pochta.com",)
            onNode(RegScreen.PasswordInput).performTextInput("12345678")
            onNode(RegScreen.NextButton).performClick()

            //DialogCansScreen
            onNode(DCansScreen.countInput).performTextInput("2")
            onNode(DCansScreen.nextButton).performClick()

            //DialogMoneyScreen
            onNode(DMoneyScreen.priceInput).performTextInput("...  ")
            onNode(DMoneyScreen.nextButton).performClick()
            onNode(DMoneyScreen.numberError).assertExists()
            onNode(StatScreen.ScreenDayCecTemplate).assertDoesNotExist()
        }

    }

    @Test
    fun testRegScreenAlreadyUser(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("MY TAG")

            //RegisterScreen
            onNode(RegScreen.EmailInput).performTextInput("logged@pochta.com",)
            onNode(RegScreen.PasswordInput).performTextInput("12345678")
            onNode(RegScreen.NextButton).performClick()

            //DialogCansScreen
            onNode(DCansScreen.countInput).performTextInput("2")
            onNode(DCansScreen.nextButton).performClick()

            //DialogMoneyScreen
            onNode(DMoneyScreen.priceInput).performTextInput("60")
            onNode(DMoneyScreen.nextButton).performClick()
            onNode(DMoneyScreen.signupError).assertExists()
            onNode(StatScreen.ScreenDayCecTemplate).assertDoesNotExist()
        }


    }


}