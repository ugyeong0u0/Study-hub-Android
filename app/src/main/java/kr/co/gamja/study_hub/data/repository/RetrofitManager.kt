package kr.co.gamja.study_hub.data.repository

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitManager {

    private const val BASE_URL = "https://study-hub.site"
    val gson =GsonBuilder().setLenient().create()

    // 레트로핏 생성
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api = retrofit.create(StudyHubApi::class.java)

}


