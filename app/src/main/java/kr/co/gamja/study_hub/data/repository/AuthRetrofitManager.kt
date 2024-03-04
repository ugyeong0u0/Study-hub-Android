package kr.co.gamja.study_hub.data.repository

import com.google.gson.GsonBuilder
import kr.co.gamja.study_hub.global.NullOnEmptyConverterFactory
import okhttp3.Dispatcher
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
        .client(createClientAuth())
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api = retrofit.create(StudyHubApi::class.java)

    // 동시 처리 1개로
    private fun createClientAuth(): OkHttpClient {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.dispatcher(dispatcher)
        okHttpClientBuilder.addInterceptor(AuthInterceptor())
        return okHttpClientBuilder.build()
    }

}
