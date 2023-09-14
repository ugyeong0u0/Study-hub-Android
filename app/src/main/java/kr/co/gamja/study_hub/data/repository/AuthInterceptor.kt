package kr.co.gamja.study_hub.data.repository

import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.data.model.AccessTokenRequest
import okhttp3.Interceptor
import okhttp3.Response

// 액세스 토큰으로 인가 요청시 사용
class AuthInterceptor() : Interceptor {
    private val tag = this.javaClass.simpleName
    private lateinit var accessToken: String
    private lateinit var refreshToken: String


    override fun intercept(chain: Interceptor.Chain): Response {

        runBlocking {
            accessToken = App.getInstance().getDataStore().accessToken.first()
            refreshToken = App.getInstance().getDataStore().refreshToken.first()
            Log.d(tag, "데이터스토어에서 불러온 액세스토큰 " + accessToken+"//////"+refreshToken)
        }
        synchronized(this) {
            val request =
                chain.request().newBuilder().addHeader("Authorization", accessToken).build()
            val response = chain.proceed(request)
            Log.d(tag, "회원조회 인터셉트")
            return response.also {
                if (response.code != 200) {
                    val getNewToken = runBlocking {
                        Log.d(tag, "회원조회 기존액세스토큰유효x "+response.code)
                        RetrofitManager.api.accessTokenIssued(AccessTokenRequest(refreshToken))
                    }
                    if (getNewToken.code() == 200) {
                        Log.d(tag, "회원조회 인터셉트 뉴토큰코드 "+getNewToken.code())
                        runBlocking {
                            val dataStoreInstance = App.getInstance().getDataStore()
                            dataStoreInstance.clearDataStore() // 초기화
                            dataStoreInstance.setAccessToken(getNewToken.body()?.accessToken.toString())
                            dataStoreInstance.setAccessToken(getNewToken.body()?.refreshToken.toString())
                            Log.d(tag, "회원조회 갱신 뉴토큰 " + accessToken+"//////"+refreshToken)
                        }
                        val newAuthRequest =
                            chain.request().newBuilder()
                                .addHeader(
                                    "Authorization",
                                    getNewToken.body()?.accessToken.toString()
                                )
                                .build()
                        Log.d(tag, "회원조회 인터셉트 뉴토큰으로 ")
                        return chain.proceed(newAuthRequest)
                    }
                    Log.d(tag, "회원조회 인터셉트 리프레쉬유효x "+getNewToken.code())
                }
                Log.d(tag, "회원조회 인터셉트 기존액세스토큰 유효 "+response.code)
            }

        }

    }
}
