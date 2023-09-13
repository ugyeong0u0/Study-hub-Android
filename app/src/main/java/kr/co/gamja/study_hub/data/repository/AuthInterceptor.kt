package kr.co.gamja.study_hub.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.datastore.App
import okhttp3.Interceptor
import okhttp3.Response

// 액세스 토큰으로 인가 요청시 사용
class AuthInterceptor:Interceptor {
    private lateinit var accessToken:String
    override fun intercept(chain: Interceptor.Chain): Response {
        CoroutineScope(Dispatchers.Main).launch {
            accessToken = App.getInstance().getDataStore().accessToken.first()
        }
        val newRequest=chain.request().newBuilder().addHeader("Authorization",accessToken).build()

        return chain.proceed(newRequest)
    }
}