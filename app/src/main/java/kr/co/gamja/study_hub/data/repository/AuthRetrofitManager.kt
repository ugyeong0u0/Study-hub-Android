package kr.co.gamja.study_hub.data.repository

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 인가 액세스토큰 보낼 때사용
object AuthRetrofitManager {

    private const val BASE_URL = "https://study-hub.site"
    val gson = GsonBuilder().setLenient().create()

    // 레트로핏 생성
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api = retrofit.create(StudyHubApi::class.java)
}