package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.StudyContentResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.global.Functions

class ContentViewModel : ViewModel() {
    val tag = this.javaClass.simpleName
    val functions = Functions()
    // 작성 날짜
    private val _writingDate = MutableLiveData<String>()
    val writingDate: LiveData<String> get() = _writingDate

    // 전공
    private val _majorData = MutableLiveData<String>()
    val majorData: LiveData<String> get() = _majorData

    // 제목
    private val _headData = MutableLiveData<String>()
    val headData: LiveData<String> get() = _headData
    // todo("인원수")

    // 벌금 todo("연결하기")
    private val _fee = MutableLiveData<Int>(0)
    val fee: LiveData<Int> get() = _fee

    // 성별
    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> get() = _gender

    // 스터디 내용
    private val _studyExplanation = MutableLiveData<String>()
    val studyExplanation: LiveData<String> get() = _studyExplanation

    // 기간
    private val _period = MutableLiveData<String>()
    val period: LiveData<String> get() = _period

    // todo("지각비")

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

    fun getStudyContent(postId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.getStudyContent(postId)
                if (response.isSuccessful) {
                    val result = response.body() as StudyContentResponse
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
                    // 성별
                    val koreanGender= functions.convertToKoreanGender(result.filteredGender)
                    _gender.value = koreanGender
                    // 스터디 내용
                    _studyExplanation.value = result.content
                    // 기간
                    val startDate = "" + result.studyStartDate[0] + "." + result.studyStartDate[1] +
                            "." + result.studyStartDate[2]
                    val endDate = "" + result.studyEndDate[0] + "." + result.studyEndDate[1] +
                            "." + result.studyEndDate[2]
                    _period.value = "$startDate~$endDate"
                    // todo("지각비")
                    // 대면여부
                    val meetingMethod=functions.convertToKoreanMeetMethod(result.studyWay)
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
            } catch (e: Exception) {
                Log.e(tag, "스터디 content조회 Exception: ${e.message}")
            }
        }
    }

}