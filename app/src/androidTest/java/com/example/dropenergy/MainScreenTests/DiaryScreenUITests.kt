package com.example.dropenergy.MainScreenTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.dropenergy.BottomBar
import com.example.dropenergy.DiaryScreen
import com.example.dropenergy.MainScreen
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


class DiaryScreenUITests {
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

    }

    @Test
    fun navToDiaryTest(){
        coEvery { userRepository.getDiary("uid")} returns GetDBState.Success(user.diary.toSortedMap(reverseOrder()))
        viewModel = DBViewModel(authRepository,userRepository)

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarDiaryButton).assertExists()
            onNode(BottomBar.bottomBarDiaryButton).performClick()
            onNode(DiaryScreen.ScreenTemplate).assertExists()
        }
    }

    @Test
    fun loadingDiaryTest(){
        coEvery { userRepository.getDiary("uid")} returns GetDBState.Success(user.diary.toSortedMap(reverseOrder()))
        viewModel = DBViewModel(authRepository,userRepository)

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarDiaryButton).assertExists()
            onNode(BottomBar.bottomBarDiaryButton).performClick()
            onRoot().printToLog("LOGG")
            onNode(DiaryScreen.ScreenTemplate).assertExists()
            onNode(DiaryScreen.DiaryList).assertExists()
            onNode(DiaryScreen.DiaryLoadError).assertDoesNotExist()
        }
    }

    @Test
    fun errorLoadingTest(){
        coEvery { userRepository.getDiary("uid")} returns GetDBState.Failure(Exception())
        viewModel = DBViewModel(authRepository,userRepository)

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarDiaryButton).assertExists()
            onNode(BottomBar.bottomBarDiaryButton).performClick()
            onRoot().printToLog("LOGG")
            onNode(DiaryScreen.ScreenTemplate).assertExists()
            onNode(DiaryScreen.DiaryList).assertDoesNotExist()
            onNode(DiaryScreen.DiaryLoadError).assertExists()
        }
    }

}