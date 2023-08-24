package kr.co.gamja.study_hub

import android.util.Log
import com.google.gson.GsonBuilder
import kr.co.gamja.study_hub.data.ApiRequest
import kr.co.gamja.study_hub.data.EmailValidRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects

object RetrofitManager {


    private const val BASE_URL =
        "http://studyhub-beanstalk-env.eba-gmc8emac.ap-northeast-2.elasticbeanstalk.com:80"

    val gson= GsonBuilder().setLenient().create()
    // 레트로핏 생성
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api = retrofit.create(StudyHubApi::class.java)

//    private fun <T> enqueueCall(callApi: Call<T>, callbackOperation: ResponseCallBack) {
//        val api = retrofit.create(StudyHubApi::class.java)
//
//        callApi.enqueue(object : Callback<T> {
//            override fun onResponse(call: Call<T>, response: Response<T>) {
//                Log.d("RetrofitManager", "success : ${response.raw()}")
//                callbackOperation.responseCallBack(true)
//            }
//
//            override fun onFailure(call: Call<T>, t: Throwable) {
//                Log.d("RetrofitManager", "fail : $t")
//            }
//        })
//    }
//
//    fun checkEmail(email: ApiRequest) {
//        enqueueCall(api.email(email), object : ResponseCallBack {
//            override fun responseCallBack(result: Boolean): Boolean {
//                return result
//            }
//        })
//    }
}

//interface ResponseCallBack {
//    fun responseCallBack(result: Boolean): Boolean
//}
