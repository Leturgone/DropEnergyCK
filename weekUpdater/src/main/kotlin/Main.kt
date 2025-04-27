import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import java.io.FileInputStream


suspend fun resetWeeks(){
    val firebase = FirebaseDatabase.getInstance()
    val database = firebase.reference.child("users")
    val uidList = mutableListOf<String>()
    var page = FirebaseAuth.getInstance().listUsers(null)
    while (page != null) {
        for (user in page.values) {
            uidList.add(user.uid.toString())
        }
        page = page.getNextPage()
    }
    println(uidList)
    val week: MutableMap<String,Boolean> = mutableMapOf(
        "TU" to false, "MO" to false, "SU" to false,
        "TH" to false, "FR" to false, "SA" to false,
        "WE" to false
    )
    coroutineScope {
        uidList.forEach() { uid ->
            async(Dispatchers.IO) {
                database.child(uid).child("week").setValueAsync(week).get()
                println("Обновлена неделя")
            }
        }
    }
    println("Выполнен сброс недели")
}


fun main()  = runBlocking{
    val serviceAccount = FileInputStream("youradminsdk.json")
    val options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("yourdatabaseurl")
        .build()
    FirebaseApp.initializeApp(options)
    println("Программа запущена.")
    try {
        resetWeeks()
    }catch (e:Exception){
        println("Ошибка сети")
    }

}




