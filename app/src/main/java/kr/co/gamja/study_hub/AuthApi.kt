package kr.co.gamja.study_hub


import kr.co.gamja.study_hub.data.Auth
import kr.co.gamja.study_hub.data.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// 토큰생성, refresh token
interface AuthApi {
    @POST(" ")
    fun login(
        @Body auth:Auth
    ): Call<LoginResponse>
    @GET(" ")
    fun refreshToken(
        @Header("Authorization")
        token:String
    ):Call<LoginResponse>

    companion object{
        private const val BASE_URL =""

        fun create() : AuthApi{
            val retrofit =Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi::class.java)
            return retrofit
        }
   }
}
