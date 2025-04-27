package com.example.dropenergy.MainScreenTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.example.dropenergy.BottomBar
import com.example.dropenergy.MainScreen
import com.example.dropenergy.OptionsScreen
import com.example.dropenergy.RegScreen
import com.example.dropenergy.domain.model.DiaryRecord
import com.example.dropenergy.domain.model.User
import com.example.dropenergy.data.repository.AuthRepository
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.data.repository.UserRepository
import com.example.dropenergy.presentation.viewmodel.DBViewModel
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OptionsUITests {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private lateinit var viewModel: DBViewModel
    private lateinit var user: User

    @Before
    fun setUp(){
        user = User(
            login = "user@pochta.com",
            password = "12345678",
            everydayCans = 2,
            everydayMoney = 60,
            currency = "₽",
            diary = mutableMapOf(
                "2025-01-11 18:37" to DiaryRecord(date = "2025-01-11 18:37", recordColor = "green", intensive = "8"),
                "2025-01-10 16:41" to DiaryRecord(date = "2025-01-10 16:41", recordColor = "green", intensive = "8"),
                "2025-01-10 19:43" to DiaryRecord(date = "2025-01-10 19:43", recordColor = "green", intensive = "8"),
                "2025-01-11 18:02" to DiaryRecord(date = "2025-01-11 18:02", recordColor = "red", intensive = "8"),
                "2025-01-11 18:13" to DiaryRecord(date = "2025-01-11 18:13", recordColor = "green", intensive = "8"),
                "2025-01-16 18:41" to DiaryRecord(date = "2025-01-16 18:41", recordColor = "green", intensive = "8"),
                "2025-01-11 18:11" to DiaryRecord(date = "2025-01-11 18:11", recordColor = "red", intensive = "8"),
                "2025-01-27 17:32" to DiaryRecord(date = "2025-01-27 17:32", recordColor = "green", intensive = "8"),
                "2025-01-15 20:41" to DiaryRecord(date = "2025-01-15 20:41", recordColor = "green", intensive = "8"),
                "2025-01-10 17:49" to DiaryRecord(date = "2025-01-10 17:49", recordColor = "green", intensive = "8"),
                "2025-01-10 16:39" to DiaryRecord(date = "2025-01-10 16:39", recordColor = "green", intensive = "8"),
                "2025-01-26 23:50" to DiaryRecord(date = "2025-01-26 23:50", recordColor = "yellow", intensive = "8")
            ),
            week = mutableMapOf("MO" to false, "TU" to true, "WE" to false, "TH" to true, "FR" to false, "SA" to true, "SU" to false),
            savedMoney = 120,
            savedCans = 2
        )
        val mockFirebaseUser = mockk<FirebaseUser>()
        every { authRepository.getCurrentUser() } returns mockFirebaseUser
        every { mockFirebaseUser .uid } returns "uid"
        coEvery { userRepository.getUser("uid")} returns user
        coEvery { userRepository.getWeek("uid")} returns GetDBState.Success(user.week)
        coEvery { authRepository.logout() } returns mockk()

        viewModel = DBViewModel(authRepository, userRepository)

    }

    @Test
    fun navToOptionsTest(){
        composeTestRule.run {
            setContent { MainScreen(viewModel = viewModel) }

            onNode(BottomBar.bottomBarOptButton).assertExists()
            onNode(BottomBar.bottomBarOptButton).performClick()
            onNode(OptionsScreen.OptTemplate).assertExists()
            onNode(OptionsScreen.LogoutButton).assertExists()
        }

    }

    @Test
    fun logoutTest(){
        composeTestRule.run {
            setContent { MainScreen(viewModel = viewModel) }

            onNode(BottomBar.bottomBarOptButton).assertExists()
            onNode(BottomBar.bottomBarOptButton).performClick()
            onNode(OptionsScreen.LogoutButton).performClick()
            onNode(RegScreen.RegTemplate).assertExists()
        }

    }
}