package kr.co.gamja.study_hub.model.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitManager {
    init {
        Log.d("회원가입 레트로핏 매니저 호출","")
    }

    private const val BASE_URL = "https://study-hub.site"
    val gson =GsonBuilder().setLenient().create()

    // 레트로핏 생성
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api = retrofit.create(StudyHubApi::class.java)

}


