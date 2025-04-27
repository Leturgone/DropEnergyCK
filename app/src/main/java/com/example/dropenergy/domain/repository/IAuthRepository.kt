package com.example.dropenergy.domain.repository

import com.google.firebase.auth.FirebaseUser

interface IAuthRepository {


    suspend fun login(email: String, password: String): GetDBState<FirebaseUser>

    suspend fun signup(email: String, password: String): GetDBState<FirebaseUser>

    fun logout()

    fun getCurrentUser(): FirebaseUser?

}