package kr.co.gamja.study_hub.model

// 이메일 인증 코드 "전송"요청
data class ApiRequest(
    val email :String
)
// 이메일 인증 코드 "검증"요청
data class EmailValidRequest(
    val authCode:String,
    val email: String
)
// 로그인 요청
data class LoginRequest(
    val email: String,
    val password:String
)
// 회원가입
data class SignupRequest(
    val email:String,
    val password: String,
    val gender:String,
    val grade:String,
    val nickname:String

)


