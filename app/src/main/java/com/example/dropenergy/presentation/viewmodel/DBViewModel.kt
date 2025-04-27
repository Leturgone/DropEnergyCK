package com.example.dropenergy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dropenergy.domain.model.CheckDay
import com.example.dropenergy.domain.model.DiaryRecord
import com.example.dropenergy.domain.model.User
import com.example.dropenergy.domain.repository.IAuthRepository
import com.example.dropenergy.domain.repository.IUserRepository
import com.example.dropenergy.domain.repository.GetDBState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DBViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    val dayCheckMap = mutableMapOf<String,Boolean>(
        "MO" to false,
        "TU" to false,
        "WE" to false,
        "TH" to false,
        "FR" to false,
        "SA" to false,
        "SU" to false
    )



    private val processing_user = MutableLiveData<User>()

    private val _loginFlow = MutableStateFlow<GetDBState<FirebaseUser>?>(null)

    val loginFlow: StateFlow<GetDBState<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<GetDBState<FirebaseUser>?>(null)

    val signupFlow: StateFlow<GetDBState<FirebaseUser>?> = _signupFlow

    private val _diaryFlow = MutableStateFlow<GetDBState<MutableMap<String, DiaryRecord>>?>(null)

    val diaryFlow: StateFlow<GetDBState<MutableMap<String, DiaryRecord>>?> = _diaryFlow

    private val _weekFlow = MutableStateFlow<GetDBState<MutableMap<String, Boolean>>?>(null)

    val weekFlow: StateFlow<GetDBState<MutableMap<String, Boolean>>?> = _weekFlow


    private val _savedMoneyFlow = MutableStateFlow<GetDBState<Int>?>(null)

    val savedMoneyFlow: StateFlow<GetDBState<Int>?> = _savedMoneyFlow

    private val _everydayMoneyFlow = MutableStateFlow<GetDBState<Int>?>(null)

    val everydayMoneyFlow: StateFlow<GetDBState<Int>?> = _everydayMoneyFlow

    private val _currency= MutableStateFlow<GetDBState<String>?>(null)

    val currency: StateFlow<GetDBState<String>?> = _currency


    private val _savedCansFlow = MutableStateFlow<GetDBState<Int>?>(null)

    val savedCansFlow: StateFlow<GetDBState<Int>?> = _savedCansFlow

    private  val _everydayCansFlow = MutableStateFlow<GetDBState<Int>?>(null)

    val everyDayCansFlow: StateFlow<GetDBState<Int>?> = _everydayCansFlow


    private var currentUser = authRepository.getCurrentUser()





    init {
        Log.d("AuthViewModel", "AuthViewModel создана")
        //Проверка логина
        if (authRepository.getCurrentUser() != null){
            _loginFlow.value = GetDBState.Success(authRepository.getCurrentUser()!!)
        }
    }

    private fun get_uid(state: GetDBState<*>):String?{
        return when(state){
            is GetDBState.Success ->{
                try {
                    val user = state.result as FirebaseUser
                    user.uid
                }catch (e:java.lang.Exception){
                    null
                }
            }
            else ->null
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = GetDBState.Loading

        val result =withContext(Dispatchers.IO){
            authRepository.login(email, password)
        }
        updateUser()
        processing_user.value = get_uid(result)?.let {
            withContext(Dispatchers.IO){
                userRepository.getUser(it) }
        }
        _loginFlow.value = result
    }

    fun signup() = viewModelScope.launch {
        _signupFlow.value = GetDBState.Loading
        Log.i("Firebase","Начата регистрация")
        processing_user.value?.let {user ->
            val result =  withContext(Dispatchers.IO){
                authRepository.signup(user.login, user.password)
            }
            updateUser()
            get_uid(result)?.let { withContext(Dispatchers.IO){
                userRepository.writeUser(it,user) }
            }
            _signupFlow.value = result
        }
    }


    fun createUser(login: String, password: String){
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = dtf.format(LocalDateTime.now())
        processing_user.value = User(login,password,
            null,null,null,
            diary = mutableMapOf(
                date to DiaryRecord(
                    date = date, recordColor = "Я зарегистрировался в приложении",intensive = ""

                )
            ),
            week = dayCheckMap,0,0)
    }

    fun addMoneyInf(currency:String, money : Int){
        processing_user.value?.currency = currency
        processing_user.value?.everydayMoney = money
    }

    fun addCans(count: Int){
        processing_user.value?.everydayCans = count
    }

    fun updateDiary(diaryRecord: DiaryRecord)  = viewModelScope.launch(Dispatchers.IO){
        currentUser?.let { userRepository.updateDiary(it.uid,diaryRecord)}
    }

    fun updateWeek(newDay: CheckDay) = viewModelScope.launch(Dispatchers.IO) {
        currentUser?.let { userRepository.updateWeek(it.uid,newDay)}
    }

    fun updateSavedCans(status: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        currentUser?.let {  userRepository.updateSavedCans(it.uid,status)}

    }

    fun updateSavedMoney(status: Boolean)= viewModelScope.launch(Dispatchers.IO)  {
        currentUser?.let {  userRepository.updateSavedMoney(it.uid, status)}

    }


    //Методы для получения данных из репозитория для БД

    fun getDiary() = viewModelScope.launch {
        _diaryFlow.value = GetDBState.Loading
        val result = currentUser?.let {withContext(Dispatchers.IO){
            userRepository.getDiary(it.uid) }
        }
        _diaryFlow.value = result

    }

    fun getWeek()= viewModelScope.launch {
        Log.i("Firebase","Начато получение недели")
        _weekFlow.value = GetDBState.Loading
        Log.i("Firebase","Проставлено состояние")
        val result = currentUser?.let {
            withContext(Dispatchers.IO){
                userRepository.getWeek(it.uid)
            }
        }
        _weekFlow.value = result
    }

    
    fun getSavedMoney() = viewModelScope.launch {
        _savedMoneyFlow.value = GetDBState.Loading
        val result = currentUser?.let { withContext(Dispatchers.IO){
            userRepository.getSavedMoney(it.uid) }
        }
        _savedMoneyFlow.value = result
    }

    fun getEverydayMoney() = viewModelScope.launch {
        _everydayMoneyFlow.value = GetDBState.Loading
        val result = currentUser?.let { withContext(Dispatchers.IO){
            userRepository.getEverydayMoney(it.uid)
        } }
        _everydayMoneyFlow.value = result
    }


    fun getCurrency() = viewModelScope.launch {
        _currency.value = GetDBState.Loading
        val result = currentUser?.let { withContext(Dispatchers.IO){
            userRepository.getCurrency(it.uid)
        } }
        _currency.value = result
    }


    fun getSavedCans() = viewModelScope.launch {
        _savedCansFlow.value = GetDBState.Loading
        val result = currentUser?.let {
            withContext(Dispatchers.IO){
                userRepository.getSavedCans(it.uid)}
            }

        _savedCansFlow.value = result
    }



    fun getEverydayCans() = viewModelScope.launch {
        _everydayCansFlow.value = GetDBState.Loading
        val result = currentUser?.let {
            withContext(Dispatchers.IO) {
                userRepository.getSavedCans(it.uid)
            }
        }
        _everydayCansFlow.value = result
    }



    fun logout(){
        authRepository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }

    private fun updateUser(){
        currentUser = authRepository.getCurrentUser()
    }


}