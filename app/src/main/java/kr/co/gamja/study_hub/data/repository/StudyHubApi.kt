package kr.co.gamja.study_hub.data.repository

import kr.co.gamja.study_hub.data.*
import kr.co.gamja.study_hub.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface StudyHubApi {

    //이메일 인증코드 전송
    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("/api/email")
    suspend fun email(@Body email: EmailRequest): Response<Unit>

    // 이메일 인증코드 검증
    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("/api/email/valid")
    suspend fun emailValid(@Body emailValidRequest: EmailValidRequest): Response<EmailValidResponse>

    // 로그인
    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("/api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // 회원가입
    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("/api/users/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<Unit>

    // 회원가입 -닉네임 중복 확인 api
    @GET("/api/users/duplication-nickname")
    suspend fun checkNicknameDuplication(@Query("nickname") nickname: String): Response<Unit>

    // 회원가입 - 이메일 중복 확인 api
    @POST("/api/email/duplication")
    suspend fun checkEmailDuplication(@Body emailRequest: EmailRequest): Response<Unit>

    // 액세스 토큰 만료시, 리프레쉬 토큰으로 액세스, 리프레시 재발급
    @POST("/api/jwt/accessToken")
    suspend fun accessTokenIssued(@Body accessTokenRequest: AccessTokenRequest): Response<AccessTokenResponse>

    // 회원 단건조회 : myPageInfo.kt
    @GET("/api/users")
    suspend fun getUserInfo(): Response<UsersResponse>

    // new - 내가 북마크한 스터디 조회
    @GET("/api/study-posts/bookmarked")
    suspend fun getBookmark(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetBookmarkResponse>

    //new - 북마크 저장, 삭제
    @POST("/api/study-posts/{postId}/bookmark")
    suspend fun saveDeleteBookmark(@Path("postId") postId: Int?): Response<BookmarkSaveDeleteResponse>

    // 스더디 생성
    @POST("/api/study-posts")
    suspend fun setCreateStudy(@Body createStudyRequest: CreateStudyRequest): Response<Unit>

    // 스터디 게시글 전체 조회
    @GET("/api/study-posts/find/all")
    suspend fun getStudyPostAll(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<FindStudyResponse>

    // 닉네임 중복 조회 - 인가x
    @GET("/api/users/duplication-nickname")
    suspend fun getIsDuplicationNickname(@Query("nickname") nickname: String): Response<Unit>

    // 닉네임 수정 - 인가0
    @PUT("/api/users/nickname")
    suspend fun putNewNickname(@Body changeNicknameRequest: ChangeNicknameRequest): Response<Unit>

    // 학과 수정 - 인가 0
    @PUT("/api/users/major")
    suspend fun putNewMajor(@Body changeMajorRequest: ChangeMajorRequest): Response<Unit>

    // 현재 비번 검사- 인가0
    @POST("/api/users/password/verify")
    suspend fun postCurrentPassword(@Body currentPasswordRequest: CurrentPasswordRequest): Response<Unit>

    // 비번 수정 - 인가0
    @PUT("/api/users/password")
    suspend fun putNewPassword(@Body newPasswordRequest: NewPasswordRequest): Response<Unit>

    // 게시글 인기순 조회
    @GET("/api/study-posts/find/hot")
    suspend fun getHotStudyPostAll(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<HotStudyPostResponse>

    // 회원 탈퇴
    @DELETE("/api/users")
    suspend fun deleteUser():Response<Unit>
    // 스터디 컨텐츠 조회
    @GET("/api/study-posts/find/{postId}")
    suspend fun getStudyContent(@Path("postId") postId: Int):Response<StudyContentResponse>
}
