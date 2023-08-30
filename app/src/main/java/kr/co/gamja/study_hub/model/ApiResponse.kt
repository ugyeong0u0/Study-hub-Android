package kr.co.gamja.study_hub.model

import com.google.gson.annotations.SerializedName

// 이메일 인증코드 전송
data class ApiResponse(
    @SerializedName("body")
    val body: String?,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusCodeValue")
    val statusCodeValue: Int
)

//이메일 인증코드 검증
data class EmailValidResponse(
    @SerializedName("validResult")
    val validResult: Boolean
)


// 로그인
data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

// 회원가입
data class SignupResponse(
    @SerializedName("body")
    val body: String?,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusCodeValue")
    val statusCodeValue: Int
)

