package com.example.dropenergy

import com.example.dropenergy.domain.model.CheckDay
import com.example.dropenergy.domain.model.DiaryRecord
import com.example.dropenergy.domain.model.User
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.data.repository.UserRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class UserRepositoryUnitTests {
    private lateinit var database: DatabaseReference
    private lateinit var mockFirebaseUser : FirebaseUser
    private lateinit var mockAuthResult : AuthResult
    private lateinit var userRepository: UserRepository
    private lateinit var user: User
    private lateinit var unFormattedUser: HashMap<*,*>
    private lateinit var errorUnFormattedUser: HashMap<*,*>
    private lateinit var childDatabaseReference: DatabaseReference
    private val date = LocalDate.now()

    @Before
    fun setup() {
        database = mockk<DatabaseReference>()
        childDatabaseReference = mockk<DatabaseReference>()
        mockFirebaseUser = mockk<FirebaseUser>()
        mockAuthResult = mockk<AuthResult>()
        userRepository = UserRepository(database)
        unFormattedUser = hashMapOf(
            "diary" to mutableMapOf(
                "2025-01-11 18:37" to hashMapOf(
                    "date" to "2025-01-11 18:37",
                    "recordColor" to "green",
                    "intensive" to "8"
                ),

                "2025-01-10 16:41" to hashMapOf(
                    "date" to "2025-01-10 16:41",
                    "recordColor" to "green",
                    "intensive" to "8"
                ),
                "2025-01-26 23:50" to hashMapOf(
                    "date" to "2025-01-26 23:50",
                    "recordColor" to "yellow",
                    "intensive" to "8"
                )
            ),
            "password" to "12345678",
            "savedCans" to "2",
            "week" to mutableMapOf(
                "TU" to true, "MO" to false, "SU" to false,
                "TH" to true, "FR" to false, "SA" to true,
                "WE" to false
            ),
            "everydayCans" to "2",
            "everydayMoney" to "60",
            "savedMoney" to "120",
            "currency" to "₽",
            "login" to "user@pochta.com"
        )

        errorUnFormattedUser = hashMapOf(
            "diar" to mutableMapOf(
                "2025-01-11 18:37" to hashMapOf(
                    "date" to "2025-01-11 18:37",
                    "recordColor" to "green",
                    "intensive" to "8"
                ),

                "2025-01-10 16:41" to hashMapOf(
                    "date" to "2025-01-10 16:41",
                    "recordColor" to "green",
                    "intensive" to "8"
                ),
                "2025-01-26 23:50" to hashMapOf(
                    "date" to "2025-01-26 23:50",
                    "recordColor" to "yellow",
                    "intensive" to "8"
                )
            ),
            "pass" to "12345678",
            "savs" to "2",
            "we" to mutableMapOf(
                "TU" to true, "MO" to false, "SU" to false,
                "TH" to true, "FR" to false, "SA" to true,
                "WE" to false
            ),
            "e" to "2",
            "everydayMney" to "60",
            "savMoney" to "120",
            "cuency" to "₽",
            "lon" to "user@pochta.com"
        )
        user = User(
            login = "user@pochta.com",
            password = "12345678",
            everydayCans = 2,
            everydayMoney = 60,
            currency = "₽",
            diary = mutableMapOf(
                "2025-01-11 18:37" to DiaryRecord(date = "2025-01-11 18:37", recordColor = "green", intensive = "8"),
                "2025-01-10 16:41" to DiaryRecord(date = "2025-01-10 16:41", recordColor = "green", intensive = "8"),
                "2025-01-26 23:50" to DiaryRecord(date = "2025-01-26 23:50", recordColor = "yellow", intensive = "8")
            ),
            week = mutableMapOf("MO" to false, "TU" to true, "WE" to false, "TH" to true, "FR" to false, "SA" to true, "SU" to false),
            savedMoney = 120,
            savedCans = 2
        )

    }

    @Test
    fun successWriteUserTest() = runTest {
        every { database.child("users").child(any()).setValue(user) } returns Tasks.forResult(null)

        userRepository.writeUser("uid", user)

        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains("Пользователь загружен в БД")
        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorWriteUserTest() = runTest {
        every { database.child("users").child(any()).setValue(user) } returns Tasks.forException(
            Exception()
        )

        userRepository.writeUser("uid", user)

        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains("Не удалось загрузить в БД")
        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetUserTest() = runTest {
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any()).get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val gotUser = userRepository.getUser("uid")

        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Данные пользователя получены из БД ")

        assertEquals(user,gotUser)
        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorGetUserTest() = runTest {
        every { database.child("users").child(any()).get() } returns Tasks.forException(Exception())

        userRepository.getUser("uid")

        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить из БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorFormatGetUserTest() = runTest {
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any()).get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val gotUser = userRepository.getUser("uid")

        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить из БД")

        assertNotEquals(user,gotUser)
        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorMapFormatGetUserTest() = runTest {
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any()).get() } returns Tasks.forResult(task)
        every { task.value } returns user

        val gotUser = userRepository.getUser("uid")
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить из БД ")

        assertNotEquals(user,gotUser)
        assertEquals(true, logMessageGood)
    }

    @Test
    fun successUpdateDiaryTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("diary").setValue(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<Void>>()
            listener.onSuccess(null) // Вызываем слушатель саксеса
            mockTask
        }
        every { mockTask.addOnFailureListener(any()) } returns mockTask

        userRepository.updateDiary("uid", DiaryRecord(date = date.toString(), recordColor = "green", intensive = "8"))
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Дневник загружен в БД")

        assertEquals(true, logMessageGood)
    }




    @Test
    fun errorUpdateDiaryTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("diary").setValue(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(Exception()) // Вызываем слушатель саксеса
            mockTask
        }

        userRepository.updateDiary("uid", DiaryRecord(date = date.toString(), recordColor = "green", intensive = "8"))
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить Дневник в БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successUpdateWeekTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("week").setValue(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<Void>>()
            listener.onSuccess(null) // Вызываем слушатель саксеса
            mockTask
        }
        every { mockTask.addOnFailureListener(any()) } returns mockTask

        userRepository.updateWeek("uid", CheckDay("FR",true) )
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Неделя загружена в БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorUpdateWeekTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("week").setValue(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(Exception())
            mockTask
        }

        userRepository.updateWeek("uid", CheckDay("FR",true))
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить неделю в БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successUpdateSavedCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("savedCans").setValue(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<Void>>()
            listener.onSuccess(null)
            mockTask
        }
        every { mockTask.addOnFailureListener(any()) } returns mockTask

        userRepository.updateSavedCans("uid", true)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Сохр банки загружены в БД")

        assertEquals(true, logMessageGood)

    }

    @Test
    fun errorUpdateSavedCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("savedCans").setValue(any()) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(Exception())
            mockTask
        }
        every { mockTask.addOnSuccessListener(any()) } returns mockTask

        userRepository.updateSavedCans("uid", true)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить сохр банки в БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorSumSavedCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser


        every { childDatabaseReference.child("savedCans").setValue(any()) } returns mockTask

        userRepository.updateSavedCans("uid", true)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в сложении банок")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successUpdateSavedMoneyTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("savedMoney").setValue(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<Void>>()
            listener.onSuccess(null)
            mockTask
        }
        every { mockTask.addOnFailureListener(any()) } returns mockTask

        userRepository.updateSavedMoney("uid", true)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Сохр деньги загружены в БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorUpdateSavedMoneyTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser


        every { childDatabaseReference.child("savedMoney").setValue(any()) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(Exception())
            mockTask
        }
        every { mockTask.addOnSuccessListener(any()) } returns mockTask

        userRepository.updateSavedMoney("uid", true)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Не удалось загрузить сохр деньги в БД")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorSumSavedMoneyTest() = runTest{
        val task = mockk<DataSnapshot>()
        val mockTask = mockk<Task<Void>>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser


        every { childDatabaseReference.child("savedMoney").setValue(any()) } returns mockTask

        userRepository.updateSavedMoney("uid", true)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в сложении денег")

        assertEquals(true, logMessageGood)
    }


    @Test
    fun successGetDiaryTest() = runTest{

        val diary = mutableMapOf(
            "2025-01-26 23:50" to DiaryRecord(date = "2025-01-26 23:50", recordColor = "yellow", intensive = "8"),
            "2025-01-11 18:37" to DiaryRecord(date = "2025-01-11 18:37", recordColor = "green", intensive = "8"),
            "2025-01-10 16:41" to DiaryRecord(date = "2025-01-10 16:41", recordColor = "green", intensive = "8")
        )

        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getDiary("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,diary)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Полученный дневник")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorGetDiaryTest() = runTest{

        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getDiary("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в получении дневника")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetWeekTest() = runTest{

        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getWeek("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,user.week)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Полученная неделя")

        assertEquals(true, logMessageGood)

    }

    @Test
    fun errorGetWeekTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getWeek("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в получении недели")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetSavedCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getSavedCans("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,user.savedCans)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Полученное количество невыпитых банок ")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorGetSavedCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getSavedCans("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в получении количества невыпитых банок")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetSavedMoneyTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getSavedMoney("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,user.savedMoney)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Получено сэкономленных денег ")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorGetSavedMoneyTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getSavedMoney("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка с получением сэкономленных денег ")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetCurrencyTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getCurrency("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,user.currency)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Полученная валюта ")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorGetCurrencyTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getCurrency("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в получении валюты")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetEverydayCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getEverydayCans("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,user.everydayCans)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Полученное количество ежед банок ")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun errorGetEverydayCansTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getEverydayCans("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в получении количества ежед банок")

        assertEquals(true, logMessageGood)
    }

    @Test
    fun successGetEverydayMoneyTest() = runTest{
        val money = user.everydayMoney!! * user.everydayCans!!
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns unFormattedUser

        val diaryState = userRepository.getEverydayMoney("uid")

        assertTrue(diaryState is GetDBState.Success)
        assertEquals(diaryState.result,money)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Полученные ежед деньги ")

        assertEquals(true, logMessageGood)

    }

    @Test
    fun errorGetEverydayMoneyTest() = runTest{
        val task = mockk<DataSnapshot>()
        every { database.child("users").child(any())} returns childDatabaseReference
        every { childDatabaseReference.get() } returns Tasks.forResult(task)
        every { task.value } returns errorUnFormattedUser

        val diaryState = userRepository.getEverydayMoney("uid")

        assertTrue(diaryState is GetDBState.Failure)
        val logMessageGood = (ShadowLog.getLogsForTag("Firebase").lastOrNull())?.msg?.contains(
            "Ошибка в получении количества денег")

        assertEquals(true, logMessageGood)
    }



}