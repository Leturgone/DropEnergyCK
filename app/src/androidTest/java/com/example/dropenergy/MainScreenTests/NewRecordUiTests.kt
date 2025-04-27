package com.example.dropenergy.MainScreenTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.dropenergy.AddNewRecordScreen
import com.example.dropenergy.BottomBar
import com.example.dropenergy.MainScreen
import com.example.dropenergy.NewRecordScreen
import com.example.dropenergy.StatScreen
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

class NewRecordUiTests {
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
        viewModel = DBViewModel(authRepository,userRepository)

    }


    @Test
    fun navToNewRecordTest(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarAddButton).assertExists()
            onNode(BottomBar.bottomBarAddButton).performClick()

            //AddNewRecordScreen
            onNode(AddNewRecordScreen.ScreenTemplate).assertExists()
            onNode(AddNewRecordScreen.NewGoodRecBtn).assertExists()
            onNode(AddNewRecordScreen.NewBuyRecBtn).assertExists()
            onNode(AddNewRecordScreen.NewWantRecBtn).assertExists()

            onNode(BottomBar.bottomBarStatButton).assertExists()
            onNode(BottomBar.bottomBarStatButton).performClick()
            onNode(StatScreen.ScreenDayCecTemplate).assertExists()
        }

    }

    @Test
    fun createGoodRecordTest(){

        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarAddButton).assertExists()
            onNode(BottomBar.bottomBarAddButton).performClick()
            onNode(AddNewRecordScreen.NewGoodRecBtn).performClick()
            //NewRecordScreen
            onNode(NewRecordScreen.ScreenTemplateGood).assertExists()
            onNode(NewRecordScreen.RecordDateTemplate).assertExists()
            onNode(NewRecordScreen.Slider).assertExists()
            onNode(NewRecordScreen.RateTemplate).assertExists()
            onNode(NewRecordScreen.CalendarBtn).assertExists()
            onNode(NewRecordScreen.SaveButton).assertExists()
        }
    }


    @Test
    fun createWantRecordTest(){
        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarAddButton).assertExists()
            onNode(BottomBar.bottomBarAddButton).performClick()
            onNode(AddNewRecordScreen.NewWantRecBtn).performClick()
            //NewRecordScreen
            onNode(NewRecordScreen.ScreenTemplateWant).assertExists()
            onNode(NewRecordScreen.RecordDateTemplate).assertExists()
            onNode(NewRecordScreen.Slider).assertExists()
            onNode(NewRecordScreen.RateTemplate).assertExists()
            onNode(NewRecordScreen.CalendarBtn).assertExists()
            onNode(NewRecordScreen.SaveButton).assertExists()
        }

    }

    @Test
    fun createBadRecordTest(){
        composeTestRule.run {
            setContent { MainScreen(viewModel) }
            onRoot().printToLog("LOGG")
            onNode(BottomBar.bottomBarAddButton).assertExists()
            onNode(BottomBar.bottomBarAddButton).performClick()
            onNode(AddNewRecordScreen.NewBuyRecBtn).performClick()
            //NewRecordScreen
            onNode(NewRecordScreen.ScreenTemplateBuy).assertExists()
            onNode(NewRecordScreen.RecordDateTemplate).assertExists()
            onNode(NewRecordScreen.Slider).assertExists()
            onNode(NewRecordScreen.RateTemplate).assertExists()
            onNode(NewRecordScreen.CalendarBtn).assertExists()
            onNode(NewRecordScreen.SaveButton).assertExists()
        }

    }

}