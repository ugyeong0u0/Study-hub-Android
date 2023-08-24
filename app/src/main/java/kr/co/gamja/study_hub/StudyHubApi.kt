package kr.co.gamja.study_hub

import kr.co.gamja.study_hub.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StudyHubApi {

    //이메일 인증코드 전송
    @POST("/api/email")
    fun email(@Body email: ApiRequest): Call<ApiResponse>

    // 이메일 인증코드 검증
    @POST("/api/email/valid")
    fun emailValid(@Body emailValidRequest: EmailValidRequest): Call<EmailValidResponse>

    // 로그인
    @POST("/api/users/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // 회원가입
    @POST("/api/users/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

}