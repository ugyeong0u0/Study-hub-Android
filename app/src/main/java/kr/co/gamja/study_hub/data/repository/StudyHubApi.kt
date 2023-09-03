package kr.co.gamja.study_hub.data.repository

import kr.co.gamja.study_hub.data.*
import kr.co.gamja.study_hub.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface StudyHubApi {

    //이메일 인증코드 전송
    @Headers("Accept: application/json","Content-type: application/json")
    @POST("/api/email")
    fun email(@Body email: EmailRequest): Call<Unit>

    //이메일 인증코드 전송
    @Headers("Accept: application/json","Content-type: application/json")
    @POST("/api/email")
    suspend fun email2(@Body email: EmailRequest): Response<Unit>

    // 이메일 인증코드 검증
    @Headers("Accept: application/json","Content-type: application/json")
    @POST("/api/email/valid")
    fun emailValid(@Body emailValidRequest: EmailValidRequest): Call<EmailValidResponse>

    // 로그인
    @POST("/api/users/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // 회원가입
    @POST("/api/users/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

}