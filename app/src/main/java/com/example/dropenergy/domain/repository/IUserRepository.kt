package com.example.dropenergy.domain.repository

import com.example.dropenergy.domain.model.CheckDay
import com.example.dropenergy.domain.model.DiaryRecord
import com.example.dropenergy.domain.model.User
import java.util.SortedMap

interface IUserRepository {
    suspend fun writeUser(uid: String,user: User)

    suspend fun getUser(uid: String): User?


    suspend fun updateDiary(uid: String,diaryRecord: DiaryRecord)

    suspend fun updateWeek(uid: String,newDay: CheckDay)

    suspend fun updateSavedCans(uid: String,status: Boolean)

    suspend fun  updateSavedMoney(uid: String,status: Boolean)

    suspend fun getDiary(uid: String): GetDBState<SortedMap<String, DiaryRecord>>

    suspend fun getWeek(uid: String): GetDBState<MutableMap<String, Boolean>>

    suspend fun getSavedCans(uid: String): GetDBState<Int>

    suspend fun getSavedMoney(uid: String): GetDBState<Int>

    suspend fun getCurrency(uid: String): GetDBState<String>

    suspend fun getEverydayCans(uid: String): GetDBState<Int>
    suspend fun getEverydayMoney(uid: String): GetDBState<Int>


}