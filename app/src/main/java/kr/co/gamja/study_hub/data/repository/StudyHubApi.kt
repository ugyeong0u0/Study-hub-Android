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
    suspend fun email(@Body email: EmailRequest): Response<Unit>

    // 이메일 인증코드 검증
    @Headers("Accept: application/json","Content-type: application/json")
    @POST("/api/email/valid")
    suspend fun emailValid(@Body emailValidRequest: EmailValidRequest): Response<EmailValidResponse>

    // 로그인
    @Headers("Accept: application/json","Content-type: application/json")
    @POST("/api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // 회원가입
    @Headers("Accept: application/json","Content-type: application/json")
    @POST("/api/users/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<Unit>

}