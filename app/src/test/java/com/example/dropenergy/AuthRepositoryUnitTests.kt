package com.example.dropenergy

import android.util.Log
import com.example.dropenergy.data.repository.AuthRepository
import com.example.dropenergy.domain.repository.GetDBState
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue


@RunWith(RobolectricTestRunner::class)
class AuthRepositoryUnitTests {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepository
    private lateinit var mockFirebaseUser : FirebaseUser
    private lateinit var mockAuthResult : AuthResult

    @Before
    fun setup() {
        mockkStatic(Log::class)
        firebaseAuth = mockk<FirebaseAuth>()
        mockFirebaseUser = mockk<FirebaseUser>()
        mockAuthResult = mockk<AuthResult>()
        authRepository = AuthRepository(firebaseAuth)



    }

    @Test
    fun successLogTest() = runTest{
        every { firebaseAuth.signInWithEmailAndPassword(any(),any()) }returns Tasks.forResult(mockAuthResult)
        every { mockAuthResult.user } returns mockFirebaseUser

        val res =  authRepository.login("good@pochta.com","12345678")

        assertTrue(res is GetDBState.Success)
    }

    @Test
    fun errorLogTest() = runTest{
        every { firebaseAuth.signInWithEmailAndPassword(any(),any()) }returns Tasks.forException(
            Exception()
        )

        val res =  authRepository.login("bad@pochta.com","12345678")

        assertTrue(res is GetDBState.Failure)
    }

    @Test
    fun successRegTest() = runTest{
        every { firebaseAuth.currentUser} returns null
        every { firebaseAuth.createUserWithEmailAndPassword(any(),any())} returns Tasks.forResult(mockAuthResult)
        every { mockAuthResult.user } returns mockFirebaseUser
        every { mockFirebaseUser.updateProfile(any()) } returns Tasks.forResult(null)

        val res =  authRepository.signup("good@pochta.com","12345678")

        assertTrue(res is GetDBState.Success)


    }

    @Test
    fun errorRegTest() = runTest{
        every { firebaseAuth.currentUser} returns null
        every { firebaseAuth.createUserWithEmailAndPassword(any(),any()) }returns Tasks.forException(
            Exception()
        )

        val res =  authRepository.signup("good@pochta.com","12345678")

        assertTrue(res is GetDBState.Failure)
    }
}