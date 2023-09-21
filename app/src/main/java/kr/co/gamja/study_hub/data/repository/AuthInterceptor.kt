package kr.co.gamja.study_hub.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.data.model.AccessTokenRequest
import okhttp3.Interceptor
import okhttp3.Response

// 액세스 토큰으로 인가 요청시 사용
class AuthInterceptor() : Interceptor {
    private val tag = this.javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val accessTokenDeferred = async(Dispatchers.IO) {
            App.getInstance().getDataStore().accessToken.first()
        }
        val refreshTokenDeferred = async(Dispatchers.IO) {
            App.getInstance().getDataStore().refreshToken.first()
        }
        val accessToken = accessTokenDeferred.await()
        val refreshToken = refreshTokenDeferred.await()

        Log.d(tag, "데이터스토어에서 불러온 액세스토큰 " + accessToken + "//////" + refreshToken)

        val request =
            chain.request().newBuilder().addHeader("Authorization", accessToken).build()
        val response = chain.proceed(request)

        Log.d(tag, "회원조회 인터셉트 시작")

        if (response.code != 200) {
            Log.d(tag, "회원조회 인터셉트 기존토큰 유효x " + response.code)
            val getNewToken = async { RetrofitManager.api.accessTokenIssued(AccessTokenRequest(refreshToken)) }.await()

            if (getNewToken.code() == 200) {
                Log.d(tag, "회원조회 인터셉트 뉴토큰코드 " + getNewToken.code())
                val dataStoreInstance = App.getInstance().getDataStore()

               dataStoreInstance.clearDataStore()   // 초기화
                 dataStoreInstance.setAccessToken(getNewToken.body()?.accessToken.toString())
                 dataStoreInstance.setAccessToken(getNewToken.body()?.refreshToken.toString())
                Log.d(tag, "회원조회 갱신 뉴토큰 " + accessToken + "//////" + refreshToken)

                val newAuthRequest =
                    chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            getNewToken.body()?.accessToken.toString()
                        )
                        .build()
                Log.d(tag, "회원조회 인터셉트 뉴토큰으로 ")
                response.close()
                return@runBlocking chain.proceed(newAuthRequest)
            } else{
                Log.d(tag, "회원조회 인터셉트 리프레쉬유효x " + getNewToken.code())
                return@runBlocking response
            }
        } else {
            Log.d(tag, "회원조회 인터셉트 기존액세스토큰 유효 " + response.code)
            return@runBlocking response
        }
    }
}
