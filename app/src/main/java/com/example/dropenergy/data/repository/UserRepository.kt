package com.example.dropenergy.data.repository

import android.util.Log
import com.example.dropenergy.domain.model.CheckDay
import com.example.dropenergy.domain.model.DiaryRecord
import com.example.dropenergy.domain.model.User
import com.example.dropenergy.domain.repository.GetDBState
import com.example.dropenergy.domain.repository.IUserRepository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.SortedMap

class UserRepository(
    private val database: DatabaseReference
) : IUserRepository {

    override suspend fun writeUser(uid: String,user: User){
        try {
            database.child("users").child(uid).setValue(user).await()
            Log.i("Firebase","Пользователь загружен в БД $uid")
        }catch (e: Exception){
            Log.e("Firebase","Не удалось загрузить в БД")
        }
    }

    private fun orderWeek(nonOrderWeek : MutableMap<String, Boolean>): MutableMap<String, Boolean> {
        val weekDaysOrder = listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
        val orderedWeekMap = mutableMapOf<String, Boolean>().apply {
            weekDaysOrder.forEach{day ->
                this[day] = nonOrderWeek[day]?:false
            }
        }
        return  orderedWeekMap
    }

    override suspend fun getUser(uid:String): User? {
        return try {
            val task = database.child("users").child(uid).get().await()
            val user = task.value
            Log.i("USER",user.toString())
            when (user) {
                is HashMap<*, *> -> {
                    val diaryMap = user["diary"] as? MutableMap<String, HashMap<String, String>>
                        ?: mutableMapOf()
                    val diaryRecords = diaryMap.mapValues { (_, innerMap) ->
                        DiaryRecord(
                            date = innerMap["date"] ?: "",
                            recordColor = innerMap["recordColor"] ?: "",
                            intensive = innerMap["intensive"] ?: ""
                        )
                    }.toMutableMap()

                    val weekMap = user["week"] as? MutableMap<String, Boolean> ?: mutableMapOf()

                    val orderedWeek = orderWeek(weekMap)
                    val result = User(
                        login = user["login"].toString(),
                        password = user["password"].toString(),
                        everydayCans = user["everydayCans"].toString().toInt(),
                        everydayMoney = user["everydayMoney"].toString().toInt(),
                        currency = user["currency"].toString(),
                        diary = diaryRecords,
                        week = orderedWeek,
                        savedMoney = user["savedMoney"].toString().toInt(),
                        savedCans = user["savedCans"].toString().toInt()

                    )
                    Log.i("Firebase", "Данные пользователя получены из БД $user")
                    result
                }

                else -> {
                    Log.e("Firebase","Не удалось загрузить из БД $user")
                    null}
            }
        }
        catch (e: Exception){
            Log.e("Firebase","Не удалось загрузить из БД")
            null
        }
    }

    override suspend fun updateDiary(uid: String, diaryRecord: DiaryRecord) {
        val dtf = DateTimeFormatter.ofPattern("HH:mm")
        val tim = dtf.format(LocalDateTime.now()).toString()
        val userDiary = getUser(uid)?.diary
        diaryRecord.date = diaryRecord.date +" " + tim
        userDiary?.put(diaryRecord.date, diaryRecord)
        database.child("users").child(uid).child("diary").setValue(userDiary).addOnSuccessListener {
            Log.i("Firebase","Дневник загружен в БД")
        }.addOnFailureListener {
            Log.e("Firebase","Не удалось загрузить Дневник в БД")
        }

    }


    override suspend fun updateWeek(uid: String, newDay: CheckDay) {
        val userWeek = getUser(uid)?.week
        userWeek?.set(newDay.day, newDay.check)
        database.child("users").child(uid).child("week").setValue(userWeek).addOnSuccessListener {
            Log.i("Firebase","Неделя загружена в БД")
        }.addOnFailureListener {
            Log.e("Firebase","Не удалось загрузить неделю в БД")
        }
    }

    override suspend fun updateSavedCans(uid: String, status: Boolean) {
        val newCans: Int
        try {
            newCans = when(status) {
                true -> getUser(uid)?.savedCans?.plus(getUser(uid)?.everydayCans!!)!!
                false -> 0
            }
            database.child("users").child(uid).child("savedCans").setValue(newCans)
                .addOnSuccessListener {
                    Log.i("Firebase", "Сохр банки загружены в БД")

                }.addOnFailureListener {
                Log.e("Firebase", "Не удалось загрузить сохр банки в БД")
            }
        }catch (e:Exception){
            Log.e("Firebase", "Ошибка в сложении банок")
        }
    }

    override suspend fun updateSavedMoney(uid: String, status: Boolean) {
        val newMoney : Int
        try {
            newMoney = when (status) {
                true -> getUser(uid)?.savedMoney?.plus(getUser(uid)?.everydayMoney!!)!!
                false -> 0
            }
            database.child("users").child(uid).child("savedMoney").setValue(newMoney)
                .addOnSuccessListener {
                Log.i("Firebase", "Сохр деньги загружены в БД")

            }.addOnFailureListener {
                Log.e("Firebase", "Не удалось загрузить сохр деньги в БД")
            }
        }catch (e:Exception){
                Log.e("Firebase", "Ошибка в сложении денег")
            }
    }

    override suspend fun getDiary(uid: String): GetDBState<SortedMap<String, DiaryRecord>> {
        return try {
            Log.i("Firebase","Начато получение дневника $uid")
            val diary = getUser(uid)?.diary?.toSortedMap(reverseOrder())
            Log.i("Firebase","Полученный дневник $diary $uid")
            GetDBState.Success(diary!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка в получении дневника")
            GetDBState.Failure(e)
        }
    }

    override suspend fun getWeek(uid: String): GetDBState<MutableMap<String, Boolean>> {
        return try{
            Log.i("Firebase","Начато получение недели $uid")
            val week = getUser(uid)?.week
            Log.i("Firebase","Полученная неделя $week")
            GetDBState.Success(week!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка в получении недели")
            GetDBState.Failure(e)
        }
    }

    override suspend fun getSavedCans(uid: String): GetDBState<Int> {
        return try{
            val savedCans = getUser(uid)?.savedCans
            Log.i("Firebase","Полученное количество невыпитых банок $savedCans")
            GetDBState.Success(savedCans!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка в получении количества невыпитых банок")
            GetDBState.Failure(e)
        }
    }

    override suspend fun getSavedMoney(uid: String): GetDBState<Int> {
        return try{
            val savedMoney = getUser(uid)?.savedMoney
            Log.i("Firebase","Получено сэкономленных денег $savedMoney")
            GetDBState.Success(savedMoney!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка с получением сэкономленных денег ")
            GetDBState.Failure(e)
        }

    }

    override suspend fun getCurrency(uid: String): GetDBState<String> {
        return try{
            val currency = getUser(uid)?.currency
            Log.i("Firebase","Полученная валюта $currency")
            GetDBState.Success(currency!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка в получении валюты")
            GetDBState.Failure(e)
        }
    }

    override suspend fun getEverydayCans(uid: String): GetDBState<Int> {
        return try{
            val energyCount = getUser(uid)?.everydayCans
            Log.i("Firebase","Полученное количество ежед банок $energyCount")
            GetDBState.Success(energyCount!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка в получении количества ежед банок")
            GetDBState.Failure(e)
        }
    }

    override suspend fun getEverydayMoney(uid: String): GetDBState<Int> {
        return try{
            val energyMoney = getUser(uid)?.everydayMoney
            val cansCount = getUser(uid)?.everydayCans
            Log.i("Firebase","Полученные ежед деньги $energyMoney")
            GetDBState.Success(energyMoney!! * cansCount!!)
        }catch (e:Exception){
            Log.e("Firebase","Ошибка в получении количества денег")
            GetDBState.Failure(e)
        }
    }


}