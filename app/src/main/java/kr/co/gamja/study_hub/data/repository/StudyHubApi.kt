package kr.co.gamja.study_hub.data.repository

import kr.co.gamja.study_hub.data.*
import kr.co.gamja.study_hub.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface StudyHubApi {

    //이메일 인증코드 전송
    @POST("/api/v1/email")
    suspend fun email(@Body email: EmailRequest): Response<Unit>

    // 이메일 인증코드 검증
    @POST("/api/v1/email/verify")
    suspend fun emailValid(@Body emailValidRequest: EmailValidRequest): Response<EmailValidResponse>

    // 로그인
    @POST("/api/v1/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // 회원가입
    @POST("/api/v1/users/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<Unit>

    // 회원가입 -닉네임 중복 확인 api
    @GET("/api/v1/users/duplication-nickname")
    suspend fun checkNicknameDuplication(@Query("nickname") nickname: String): Response<Unit>

    // 회원가입 - 이메일 중복 확인 api
    @POST("/api/v1/email/duplication")
    suspend fun checkEmailDuplication(@Body emailRequest: EmailRequest): Response<Unit>

    // 액세스 토큰 만료시, 리프레쉬 토큰으로 액세스, 리프레시 재발급
    @POST("/api/jwt/v1/accessToken")
    suspend fun accessTokenIssued(@Body accessTokenRequest: AccessTokenRequest): Response<AccessTokenResponse>

    // 회원 단건조회 : myPageInfo.kt
    @GET("/api/v1/users")
    suspend fun getUserInfo(): Response<UsersResponse>

    // new - 내가 북마크한 스터디 조회
    @GET("/api/v1/study-posts/bookmarked")
    suspend fun getBookmark(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetBookmarkResponse>

    //new - 북마크 저장, 삭제
    @POST("/api/v1/bookmark/{postId}")
    suspend fun saveDeleteBookmark(@Path("postId") postId: Int?): Response<BookmarkSaveDeleteResponse>


    // 닉네임 중복 조회 - 인가x
    @GET("/api/v1/users/duplication-nickname")
    suspend fun getIsDuplicationNickname(@Query("nickname") nickname: String): Response<Unit>

    // 닉네임 수정 - 인가0
    @PUT("/api/v1/users/nickname")
    suspend fun putNewNickname(@Body changeNicknameRequest: ChangeNicknameRequest): Response<Unit>

    // 학과 수정 - 인가 0
    @PUT("/api/v1/users/major")
    suspend fun putNewMajor(@Body changeMajorRequest: ChangeMajorRequest): Response<Unit>

    // 현재 비번 검사- 인가0
    @POST("/api/v1/users/password/verify")
    suspend fun postCurrentPassword(@Body currentPasswordRequest: CurrentPasswordRequest): Response<Unit>

    // 비번 수정 - 인가0
    @PUT("/api/v1/users/password")
    suspend fun putNewPassword(@Body newPasswordRequest: NewPasswordRequest): Response<Unit>

    // 회원 탈퇴
    @DELETE("/api/v1/users")
    suspend fun deleteUser(): Response<Unit>

    /*
    스터디
     */

    // 스터디 컨텐츠 조회- 스터디 단건 조회 api
    @GET("/api/v1/study-posts/{postId}")
    suspend fun getStudyContent(@Path("postId") postId: Int): Response<StudyContentResponseM>

    // 스터디 삭제
    @DELETE("/api/v1/study-posts/{postId}")
    suspend fun deleteMyStudy(@Path("postId") postId: Int): Response<Unit>

    // 스터디 생성
    @POST("/api/v1/study-posts")
    suspend fun setCreateStudy(@Body createStudyRequest: CreateStudyRequest): Response<Int>

    // 스터디 게시글 전체 조회 todo("매개변수 변경됨")
    @GET("/api/v2/study-posts")
    suspend fun getStudyPostAll(
        @Query("hot") hot: Boolean,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("inquiryText") inquiryText: String?,
        @Query("titleAndMajor") titleAndMajor: Boolean
    ): Response<FindStudyResponseM>

    // 스터디 수정
    @PUT("/api/v1/study-posts")
    suspend fun correctMyStudy(@Body correctStudyRequest: CorrectStudyRequest): Response<Int>

    // 유저 사진 수정
    @Multipart
    @PUT("/api/v1/users/image")
    suspend fun setUserImg(@Part photo: MultipartBody.Part): Response<Unit>

    // 유저 사진 삭제
    @DELETE("/api/v1/users/image")
    suspend fun deleteUserImg(): Response<Unit>

    // 내가 쓴 스터디 조회
    @GET("/api/v1/study-posts/mypost")
    suspend fun getMyStudy(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<MyStudyListResponse>

    // 댓글 조회
    @GET("/api/v1/comments/{postId}")
    suspend fun getComments(
        @Path("postId") postId: Int?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<CommentsListResponse>

    // 댓글 작성
    @POST("/api/v1/comments")
    suspend fun setComment(
        @Body commentRequest: CommentRequest
    ): Response<Unit>

    // 댓글 삭제
    @DELETE("/api/v1/comments/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Int): Response<Unit>

    // 댓글 수정하기
    @PUT("/api/v1/comments")
    suspend fun correctComment(@Body commentCorrectRequest: CommentCorrectRequest):Response<Unit>
}
