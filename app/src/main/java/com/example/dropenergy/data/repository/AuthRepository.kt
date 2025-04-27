package com.example.dropenergy.data.repository

import android.util.Log
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.lang.Exception


class AuthRepository(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {

    init {
        Log.i("AuthRepository", "AuthRepository создан")
    }


    override suspend fun login(email: String, password: String): GetDBState<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Log.i("Firebase","Логин выполнен")
            GetDBState.Success(result.user!!)
        } catch (e : Exception){
            Log.e("Firebase","Произошла ошибка при логине")
            GetDBState.Failure(e)
        }
    }

    override suspend fun signup(email: String, password: String): GetDBState<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().build())?.await()
            Log.i("Firebase","Регистрация выполнена ${firebaseAuth.currentUser?.uid}")
            GetDBState.Success(result.user!!)
        } catch (e : Exception){
            Log.e("Firebase","Произошла ошибка при регистрации")
            GetDBState.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        Log.i("Firebase","Выполнен выход из аккаунта")
    }

    override fun getCurrentUser(): FirebaseUser? {
        Log.i("Firebase","Начато получение юзера")
        val currentUser = firebaseAuth.currentUser
        Log.i("Firebase","Полученный юзер $currentUser")
        return currentUser
    }

}