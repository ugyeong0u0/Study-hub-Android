package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.CommentsListResponse
import kr.co.gamja.study_hub.data.model.StudyContentResponseM
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.global.Functions

class ContentViewModel : ViewModel() {
    val tag: String = this.javaClass.simpleName
    private val functions = Functions()

    // studyId 신청하기 할 때 필요
    private val _studyId = MutableLiveData<Int>()
    val studyId: LiveData<Int> get() = _studyId

    // 작성 날짜
    private val _writingDate = MutableLiveData<String>()
    val writingDate: LiveData<String> get() = _writingDate

    // 전공
    private val _majorData = MutableLiveData<String>()
    val majorData: LiveData<String> get() = _majorData

    // 제목
    private val _headData = MutableLiveData<String>()
    val headData: LiveData<String> get() = _headData

    // 총 인원수
    private val _totalPeople = MutableLiveData<Int>()
    val totalPeople: LiveData<Int> get() = _totalPeople

    // 인원 수
    private val _participatingPeople = MutableLiveData<Int>()
    val participatingPeople: LiveData<Int> get() = _participatingPeople

    // "지각비 벌금" 구성
    private val _feeWithReason = MutableLiveData<String>()
    val feeWithReason: LiveData<String> get() = _feeWithReason

    // 벌금만
    private val _fee = MutableLiveData<String>()
    val fee: LiveData<String> get() = _fee

    // 성별
    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> get() = _gender

    // 스터디 내용
    private val _studyExplanation = MutableLiveData<String>()
    val studyExplanation: LiveData<String> get() = _studyExplanation

    // 기간
    private val _period = MutableLiveData<String>()
    val period: LiveData<String> get() = _period

    // 대면여부
    private val _meetMethod = MutableLiveData<String>()
    val meetMethod: LiveData<String> get() = _meetMethod

    // 대면여부
    private val _relativeMajor = MutableLiveData<String>()
    val relativeMajor: LiveData<String> get() = _relativeMajor

    private val _userImg = MutableLiveData<String>()
    val userImg: LiveData<String> get() = _userImg

    // 작성자 학과
    private val _writerMajor = MutableLiveData<String>()
    val writerMajor: LiveData<String> get() = _writerMajor

    // 작성자이름
    private val _writerName = MutableLiveData<String>()
    val writerName: LiveData<String> get() = _writerName

    // 작성자인지 확인
    private val _isWriter = MutableLiveData<Boolean>()
    val isWriter: LiveData<Boolean> get() = _isWriter

    // 현재 보고있는 포스트 id
    private val _postId = MutableLiveData<Int>()

    // 북마크 현황
    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    // 댓글 양방향 데이터 todo("연결하기")
    var studyComment = MutableLiveData<String>()

    // 댓글 개수
    var totalComment = MutableLiveData<Int>()

    fun getStudyContent(adapter: ContentAdapter, postId: Int, params: CallBackListener) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getStudyContent(postId)
                if (response.isSuccessful) {
                    val result = response.body() as StudyContentResponseM
                    getInformationOfStudy(result)
                    getRecommendList(adapter, result)
                    _isWriter.value = result.usersPost
                    params.isSuccess(true)
                }
            } catch (e: Exception) {
                Log.e(tag, "스터디 content조회 Exception: ${e.message}")
            }
        }
    }

    private fun getInformationOfStudy(result: StudyContentResponseM) {
        // 상단 관련학과
        val koreanRelativeMajor = functions.convertToKoreanMajor(result.major)
        // 신청하기 때 쓸 studyId
        _studyId.value=result.studyId

        _majorData.value = koreanRelativeMajor
        // 제목
        _headData.value = result.title
        // 생성날짜
        val createdDate = StringBuilder()
        createdDate.append(result.createdDate[0])
            .append(".")
            .append(result.createdDate[1])
            .append(".")
            .append(result.createdDate[2])
            .append("작성")
        _writingDate.value = createdDate.toString()
        // 총 인원수
        _totalPeople.value = result.studyPerson
        // 참여 인원
        _participatingPeople.value = result.studyPerson - result.remainingSeat
        // 성별
        val koreanGender = functions.convertToKoreanGender(result.filteredGender)
        _gender.value = koreanGender
        // 스터디 내용
        _studyExplanation.value = result.content
        // 기간
        val dateBuilder = StringBuilder()
        dateBuilder.append(result.studyStartDate[0])
            .append(".")
            .append(result.studyStartDate[1])
            .append(".")
            .append(result.studyStartDate[2])
            .append("~")
            .append(result.studyEndDate[0])
            .append(".")
            .append(result.studyEndDate[1])
            .append(".")
            .append(result.studyEndDate[2])
        _period.value = dateBuilder.toString()

        // 지각비
        when (result.penalty) {
            0 -> {
                _feeWithReason.value = "없어요"
                _fee.value = "없어요"
            }
            else -> {
                val builder = StringBuilder()
                builder.append(result.penaltyWay.toString())
                    .append(" ")
                    .append(result.penalty.toString())
                    .append("원")
                _feeWithReason.value = builder.toString()
                _fee.value = result.penaltyWay.toString() + "원"
            }
        }
        // 대면여부
        val meetingMethod = functions.convertToKoreanMeetMethod(result.studyWay)
        _meetMethod.value = meetingMethod
        // 관련학과
        _relativeMajor.value = koreanRelativeMajor
        // 작성자 학부
        val koreanWriterMajor = functions.convertToKoreanMajor(result.postedUser.major)
        _writerMajor.value = koreanWriterMajor
        // 작성자 이름
        _writerName.value = result.postedUser.nickname
        // 작성자 이미지
        _userImg.value = result.postedUser.imageUrl
        _postId.value = result.postId
        // 북마크 여부
        _isBookmarked.value = result.bookmarked
        Log.d(
            tag,
            "북마크된지 통신값: " + result.bookmarked.toString() + "북마크된지 livedata 값" + _isBookmarked.value.toString()
        )

    }

    // 추천리스트 반영 함수
    private fun getRecommendList(adapter: ContentAdapter, result: StudyContentResponseM) {
        adapter.studyPosts = result.relatedPost
        Log.d(tag + ": 추천리스트", result.postId.toString() + ":" + result.relatedPost.toString())
        adapter.notifyDataSetChanged()
    }

    // 북마크 저장/삭제
    fun saveBookmark() {
        Log.d(tag, _postId.value.toString())
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.saveDeleteBookmark(_postId.value)
                if (response.isSuccessful) {
                    Log.d(tag, "북마크 저장 코드 code" + response.code().toString())
                    val result = response.body() as BookmarkSaveDeleteResponse
                    Log.d(tag, "저장인지 삭제인지 :" + result.created.toString())

                }
            } catch (e: Exception) {
                Log.e(tag, "북마크 저장삭제 Exception: ${e.message}")
            }
        }
    }

    // 댓글 리스트 조회
    fun getCommentsList(adapter: CommentAdapter, postId: Int) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getComments(postId, 0, 8)
                Log.d(tag, "conmmentsList postID" + _postId.value.toString())
                if (response.isSuccessful) {
                    Log.d(tag, "conmmentsList 코드 code" + response.code().toString())
                    val result = response.body() as CommentsListResponse
                    adapter.commentsList = result.content
                    adapter.notifyDataSetChanged()
                    totalComment.value = result.numberOfElements // 댓글 개수 저장
                }
            } catch (e: Exception) {
                Log.e(tag, "conmmentsList 코드 Exception: ${e.message}")
            }
        }
    }
}