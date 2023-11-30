package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.CorrectStudyRequest
import kr.co.gamja.study_hub.data.model.CreateStudyResponse
import kr.co.gamja.study_hub.data.model.StudyContentResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.global.Functions

class ContentViewModel : ViewModel() {
    val tag: String = this.javaClass.simpleName
    private val functions = Functions()

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

    // todo("작성자 사진")

    // 작성자 학과
    private val _writerMajor = MutableLiveData<String>()
    val writerMajor: LiveData<String> get() = _writerMajor

    // 작성자이름
    private val _writerName = MutableLiveData<String>()
    val writerName: LiveData<String> get() = _writerName

    // 작성자인지 확인
    private val _isWriter = MutableLiveData<Boolean>()
    val isWriter: LiveData<Boolean> get() = _isWriter

    fun getStudyContent(adapter: ContentAdapter, postId: Int) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getStudyContent(postId)
                if (response.isSuccessful) {
                    val result = response.body() as StudyContentResponse
                    getInformationOfStudy(result)
                    getRecommendList(adapter, result)
                    _isWriter.value = result.usersPost
                }
            } catch (e: Exception) {
                Log.e(tag, "스터디 content조회 Exception: ${e.message}")
            }
        }
    }

    private fun getInformationOfStudy(result: StudyContentResponse) {
        // 상단 관련학과
        val koreanRelativeMajor = functions.convertToKoreanMajor(result.major)
        _majorData.value = koreanRelativeMajor
        // 제목
        _headData.value = result.title
        // 생성날짜
        val year = result.createdDate.get(0)
        val month = result.createdDate.get(1)
        val day = result.createdDate.get(2)
        val date = "$year\\.$month\\.$day 작성"
        _writingDate.value = date
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
        val startDate = "" + result.studyStartDate[0] + "." + result.studyStartDate[1] +
                "." + result.studyStartDate[2]
        val endDate = "" + result.studyEndDate[0] + "." + result.studyEndDate[1] +
                "." + result.studyEndDate[2]
        _period.value = "$startDate~$endDate"
        // 지각비
        when (result.penalty) {
            0 -> {
                _feeWithReason.value = "없어요"
                _fee.value = "없어요"
            }
            else -> {
                _feeWithReason.value =
                    result.penaltyWay.toString() + " " + result.penalty.toString() + "원"
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
        // todo("작성자사진")
    }

    // 추천리스트 반영 함수
    private fun getRecommendList(adapter: ContentAdapter, result: StudyContentResponse) {
        adapter.studyPosts = result.relatedPost
        adapter.notifyDataSetChanged()
    }
}