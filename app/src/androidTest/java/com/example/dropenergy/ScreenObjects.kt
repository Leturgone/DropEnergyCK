package com.example.dropenergy

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText


//Объекты на экранах

object LogScreen{
    val LogTemplate = (hasText("Войти") or hasText("Sign in")) and hasNoClickAction()
    val EmailInput = (hasText("Электронная почта") or hasText("Email")) and hasSetTextAction()
    val PasswordInput = (hasText("Пароль") or hasText("Password")) and hasSetTextAction()
    val NextButton = (hasText("Дальше") or hasText("Next")) and hasClickAction()

    val EmailErrorToast = (hasText("Проверьте логин на ошибки")
            or hasText("Check email for errors"))and hasNoClickAction()
    val ShortPasswordErrorToast = (hasText("Пароль должен содержать минимум 8 символов")
            or hasText("Password must contain at least 8 characters"))and hasNoClickAction()
    val PasswordErrorToast = (hasText("Пароль не должен содержать пробелов")
            or hasText("Password must not contain spaces"))and hasNoClickAction()
    val LoginErrorToast = (hasText("Неверный логин или пароль")
            or hasText("Invalid email or password"))and hasNoClickAction()
}


object RegScreen{
    val RegTemplate = (hasText("Создать аккаунт") or hasText("Sign up")) and hasNoClickAction()
    val EmailInput = (hasText("Электронная почта") or hasText("Email")) and hasSetTextAction()
    val PasswordInput = (hasText("Пароль") or hasText("Password")) and hasSetTextAction()
    val NextButton = (hasText("Дальше") or hasText("Next")) and hasClickAction()
    val AlreadyTemplate = (hasText("Уже есть аккаунт? Войти") or
            hasText("Already have an account? Sign in")) and hasClickAction()

    val EmailErrorToast = (hasText("Проверьте логин на ошибки")
            or hasText("Check email for errors"))and hasNoClickAction()
    val ShortPasswordErrorToast = (hasText("Пароль должен содержать минимум 8 символов")
            or hasText("Password must contain at least 8 characters"))and hasNoClickAction()
    val PasswordErrorToast = (hasText("Пароль не должен содержать пробелов")
            or hasText("Password must not contain spaces"))and hasNoClickAction()
}


object DCansScreen{
    val regCansTemplate = (hasText("Сколько вы покупаете энергетиков в день?")
            or hasText("How many energy drinks do you buy per day?")) and hasNoClickAction()
    val countInput = (hasText("Количество") or hasText("Count")) and hasSetTextAction()
    val nextButton = (hasText("Дальше") or hasText("Next")) and hasClickAction()

    val NumberError = (hasText("Введите только число") or hasText("Enter only a number")) and hasNoClickAction()
}

object DMoneyScreen{
    val regMoneyTemplate = (hasText("Сколько в среднем стоит один энергетик?")
            or hasText("What is the average price of one energy drink?")) and hasNoClickAction()
    val priceInput = (hasText("Стоимость") or hasText("Price")) and hasSetTextAction()
    val currencyButton = hasText("₽") and hasClickAction()
    val nextButton = (hasText("Дальше") or hasText("Next")) and hasClickAction()

    val numberError = (hasText("Введите только число") or hasText("Enter only a number")) and hasNoClickAction()
    val signupError = (hasText("Не удалось выполнить регистрацию") or hasText("Failed to sign up")) and hasNoClickAction()
}


object BottomBar{
    val bottomBarStatButton = hasContentDescription("progress") and hasClickAction()
    val bottomBarDiaryButton = hasContentDescription("diary") and hasClickAction()
    val bottomBarAddButton = hasContentDescription("add_record") and hasClickAction()
    val bottomBarOptButton = hasContentDescription("options") and hasClickAction()
}

object StatScreen{
    val ScreenDayCecTemplate = (hasText("Ежедневная отметка") or hasText("Daily Check")) and hasNoClickAction()
    val DaysSection = (hasText("Эта неделя") or hasText("This week")) and  hasClickAction()
    val ScreenProgressTemplate = (hasText("Прогресс") or hasText("Progress")) and hasNoClickAction()
    val MoneySec = hasContentDescription("moneyScreen") and hasClickAction()
    val CanSec = hasContentDescription("canScreen") and  hasClickAction()

    val WeekLoadError = (hasText("Ошибка загрузки недели") or hasText("Error loading week")) and hasNoClickAction()

}

object DiaryScreen{
    val ScreenTemplate = hasText("Дневник") or hasText("Diary") and hasNoClickAction()
    val DiaryList = hasScrollAction()

    val DiaryLoadError = (hasText("Не удалось загрузить дневник") or hasText("Failed to load diary")) and hasNoClickAction()

}

object AddNewRecordScreen{
    val ScreenTemplate = (hasText("Создать запись") or hasText("Create Record")) and hasNoClickAction()
    val NewBuyRecBtn = (hasText("Я купил энергетик") or hasText("I bought an energy drink")) and hasClickAction()
    val NewWantRecBtn = (hasText("Я хочу энергетик") or hasText("I want an energy drink")) and hasClickAction()
    val NewGoodRecBtn = (hasText("Я справился с соблазном") or hasText("I overcame the temptation")) and hasClickAction()
}

object MoneyScreen{
    val ScreenTemplate = (hasText("Сэкономлено") or hasText("Saved")) and hasNoClickAction()
    val PrognozTemplate = (hasText("Прогноз") or hasText("Prediction")) and hasNoClickAction()


    val LoadingEveryDayMoneyError = (hasText("Не удалось загрузить ежедневные деньги") or
            hasText("Failed to load daily money")) and hasNoClickAction()
    val LoadingSavedMoneyError = (hasText("Не удалось загрузить сохраненные деньги") or
            hasText("Failed to load saved money")) and hasNoClickAction()
    val LoadingCurrencyError = (hasText("Не удалось загрузить валюту") or
            hasText("Failed to load currency")) and hasNoClickAction()
}

object CanScreen{
    val ScreenTemplate = (hasText("Не выпито") or hasText("Not drunk")) and hasNoClickAction()
    val PrognozTemplate = (hasText("Прогноз") or hasText("Prediction")) and hasNoClickAction()


    val LoadingEveryDayCansError = (hasText("Не удалось загрузить ежедневные банки") or
            hasText("Failed to load daily cans")) and hasNoClickAction()
    val LoadingSavedCansError = (hasText("Не удалось загрузить не выпитые банки") or
            hasText("Failed to load not drunk cans")) and hasNoClickAction()
}

object NewRecordScreen{
    val ScreenTemplateBuy = (hasText("Я купил энергетик") or hasText("I bought an energy drink")) and hasNoClickAction()
    val ScreenTemplateWant = (hasText("Я хочу энергетик") or hasText("I want an energy drink")) and hasNoClickAction()
    val ScreenTemplateGood= (hasText("Я справился с соблазном") or hasText("I overcame the temptation")) and hasNoClickAction()
    val RateTemplate = (hasText("Оцените Интенсивность") or hasText("Rate Intensity")) and hasNoClickAction()
    val RecordDateTemplate = (hasText("Дата записи") or hasText("Record Date")) and hasNoClickAction()
    val CalendarBtn = hasContentDescription("Calendar") and hasClickAction()
    val Slider = hasText("8")
    val SaveButton = (hasText("Сохранить") or hasText("Save")) and hasClickAction()
}

object OptionsScreen{
    val OptTemplate = (hasText("Настройки") or hasText("Options")) and hasNoClickAction()
    val LogoutButton = (hasText("Выйти из аккаунта") or hasText("Log out")) and hasClickAction()
}