package kr.co.gamja.study_hub.feature.studypage.createStudy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.CorrectStudyRequest
import kr.co.gamja.study_hub.data.model.CreateStudyRequest
import kr.co.gamja.study_hub.data.model.StudyContentResponseM
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackIntegerListener
import kr.co.gamja.study_hub.global.Functions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CreateStudyViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName
    val function = Functions()

    // 화면 표시 여부 Boolean - 학과 chip
    private val _isRelativeMajor = MutableLiveData<Boolean>(false)
    val isRelativeMajor: LiveData<Boolean> get() = _isRelativeMajor

    fun setIsRelativeMajor(new: Boolean) {
        _isRelativeMajor.value = new
    }

    // 화면 표시 글자(chip) 학과 (relativeMajorFragment->CreateStudy)
    private val _postRelativeMajor = MutableLiveData<String>()
    val postRelativeMajor: LiveData<String> get() = _postRelativeMajor

    fun setPostRelativeMajor(new: String) {
        _postRelativeMajor.value = new
    }

    // 성별
    private val _regardlessOfGender = MutableLiveData<Boolean>()
    val regardlessOfGender: LiveData<Boolean> get() = _regardlessOfGender

    private val _male = MutableLiveData<Boolean>()
    val male: LiveData<Boolean> get() = _male

    private val _female = MutableLiveData<Boolean>()
    val female: LiveData<Boolean> get() = _female

    // 대면여부
    private val _mix = MutableLiveData<Boolean>()
    val mix: LiveData<Boolean> get() = _mix

    private val _offline = MutableLiveData<Boolean>()
    val offline: LiveData<Boolean> get() = _offline

    private val _online = MutableLiveData<Boolean>()
    val online: LiveData<Boolean> get() = _online

    // 날짜 표시
    private val _startDay = MutableLiveData<String>("선택하기")
    val startDay: LiveData<String> get() = _startDay

    private val _endDay = MutableLiveData<String>("선택하기")
    val endDay: LiveData<String> get() = _endDay

    // 날짜 글씨 색
    private val _startDayColor = MutableLiveData<Boolean>(false)
    val startDayColor: LiveData<Boolean> get() = _startDayColor

    private val _endDayColor = MutableLiveData<Boolean>(false)
    val endDayColor: LiveData<Boolean> get() = _endDayColor

    // CreateStudyRequest에 넣을값

    // url editText 양방향
    val urlEditText = MutableLiveData<String>()

    // 스터디 제목
    val studyTitle = MutableLiveData<String>()

    // 스터디 내용
    val studyContent = MutableLiveData<String>()

    // 통신시 사용 학과 설정(relativeMajorFragment->CreateStudy)
    private val _relativeMajor = MutableLiveData<String>()
    val relativeMajor: LiveData<String> get() = _relativeMajor

    // 학과 정함
    fun setRelativeMajor(new: String) {
        _relativeMajor.value = new
        setIsRelativeMajor(true)
        Log.d(tag, "학과 선택값" + relativeMajor.value.toString())
    }

    // 스터디 인원
    val persons = MutableLiveData<String>()

    //스터디 인원 0인 경우 에러메시지
    private val _errorPersons = MutableLiveData<Boolean>(false)
    val errorPersons: LiveData<Boolean> get() = _errorPersons
    fun setErrorPersons(new: Boolean) {
        _errorPersons.value = new
    }


    // 스터디 성별
    val gender = MutableLiveData<String>()

    // 대면여부
    val meetMethod = MutableLiveData<String>()

    // 벌금여부
    private val _selectedFee = MutableLiveData<Boolean>()
    val selectedFee: LiveData<Boolean> get() = _selectedFee
    fun setSelectedFee(options: Boolean) {
        _selectedFee.value = options
    }

    // 벌금 종류(통신)
    val whatFee = MutableLiveData<String>()

    // 벌금 얼마인지
    val howMuch = MutableLiveData<String>("0")

    // 시작날짜 2023-10-04
    val editStartDay = MutableLiveData<String>()

    //종료날짜
    val editEndDay = MutableLiveData<String>()

    // 완료 버튼 enable
    private val _completeBtn = MutableLiveData<Boolean>(false)
    val completeBtn: LiveData<Boolean> get() = _completeBtn
    fun setCompleteBtn(new: Boolean) {
        _completeBtn.value = new
    }

    fun setRegardlessOfGender(new: Boolean) {
        _regardlessOfGender.value = true
        _male.value = false
        _female.value = false
        gender.value = "NULL"
    }

    fun setMale(new: Boolean) {
        _regardlessOfGender.value = false
        _male.value = true
        _female.value = false
        gender.value = "MALE"
    }

    fun setFemale(new: Boolean) {
        _regardlessOfGender.value = false
        _male.value = false
        _female.value = true
        gender.value = "FEMALE"
    }

    fun initGender(new: Boolean) {
        _regardlessOfGender.value = false
        _male.value = false
        _female.value = false
        gender.value = ""
    }


    //대면 여부
    fun setMix(new: Boolean) {
        _mix.value = true
        _offline.value = false
        _online.value = false
        meetMethod.value = "MIX" // 통신 값
    }

    fun setOffline(new: Boolean) {
        _mix.value = false
        _offline.value = true
        _online.value = false
        meetMethod.value = "UNTACT"
    }

    fun setOnline(new: Boolean) {
        _mix.value = false
        _offline.value = false
        _online.value = true
        meetMethod.value = "CONTACT"
    }

    fun initMeet(new: Boolean) {
        _mix.value = false
        _offline.value = false
        _online.value = false
        meetMethod.value = ""
    }

    fun setStartDay(new: String) {
        _startDay.value = new // 화면 표시 날짜 2023년 10월 4일
        _startDayColor.value = true
        editStartDay.value = converDate(new) // api 통신시 사용 2023-10-04
    }

    fun setEndDay(new: String) {
        _endDay.value = new
        _endDayColor.value = true
        editEndDay.value = converDate(new)
    }

    fun initDay() {
        _startDay.value = "선택하기"
        editStartDay.value = ""
        _startDayColor.value = false
        _endDay.value = "선택하기"
        editEndDay.value = ""
        _endDayColor.value = false
    }


    fun converDate(inputDate: String): String {
        try {
            // 입력된 날짜 형식 지정
            val inputDateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
            val date: Date = inputDateFormat.parse(inputDate)!!
            // 원하는 날짜 형식 지정
            val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            return outputDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "" // 날짜 변환 실패 시 빈 문자열 반환
        }
    }

    // 시작 날짜 < 종료 날짜 나타내기 위해 씀
    fun convertYYYYMM(inputDate: String): String {
        try {
            // 입력된 날짜 형식 지정
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date: Date = inputDateFormat.parse(inputDate)!!
            // 원하는 날짜 형식 지정
            val outputDateFormat = SimpleDateFormat("yyyyMM", Locale.KOREAN)
            return outputDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "" // 날짜 변환 실패 시 빈 문자열 반환
        }
    }

    // 시작 날짜 < 종료 날짜 나타내기 위해 씀
    fun convertDay(inputDate: String): String {
        try {
            // 입력된 날짜 형식 지정
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date: Date = inputDateFormat.parse(inputDate)!!
            // 원하는 날짜 형식 지정
            val outputDateFormat = SimpleDateFormat("dd", Locale.KOREAN)
            return outputDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "" // 날짜 변환 실패 시 빈 문자열 반환
        }
    }


    // editText초기화
    fun setInit() {
        urlEditText.value = ""
        studyContent.value = ""
        initGender(false)
        setRelativeMajor("")
        setPostRelativeMajor("")
        setIsRelativeMajor(false)
        setSelectedFee(false)
        whatFee.value = ""
        howMuch.value = "0"
        initDay()
        persons.value = ""
        initMeet(false)
        studyTitle.value = ""
    }

    fun setUserMajor(item: String) {
        when (item) {
            "공연예술과" -> setRelativeMajor("PERFORMING_ART")
            "IBE전공" -> setRelativeMajor("IBE")
            "건설환경공학" -> setRelativeMajor("CIVIL_AND_ENVIRONMENTAL_ENGINEERING")
            "건축공학" -> setRelativeMajor("ARCHITECTURAL_ENGINEERING")
            "경영학부" -> setRelativeMajor("BUSINESS_ADMINISTRATION")
            "경제학과" -> setRelativeMajor("ECONOMICS")
            "국어교육과" -> setRelativeMajor("KOREAN_LANGUAGE_EDUCATION")
            "국어국문학과" -> setRelativeMajor("KOREAN_LANGUAGE_LITERATURE")
            "기계공학과" -> setRelativeMajor("MECHANICAL_ENGINEERING")
            "데이터과학과" -> setRelativeMajor("BUSINESS_DATA_SCIENCE")
            "도시건축학" -> setRelativeMajor("ARCHITECTURE_AND_URBAN_DESIGN")
            "도시공학과" -> setRelativeMajor("URBAN_ENGINEERING")
            "도시행정학과" -> setRelativeMajor("URBAN_POLICY_AND_ADMINISTRATION")
            "독어독문학과" -> setRelativeMajor("GERMAN_LANGUAGE_LITERATURE")
            "동북아통상전공" -> setRelativeMajor("SCHOOL_OF_NORTHEAST_ASIAN")
            "디자인학부" -> setRelativeMajor("DESIGN")
            "무역학부" -> setRelativeMajor("INTERNATIONAL_TRADE")
            "문헌정보학과" -> setRelativeMajor("LIBRARY_AND_INFORMATION")
            "물리학과" -> setRelativeMajor("PHYSICS")
            "미디어커뮤니케이션학과" -> setRelativeMajor("MASS_COMMUNICATION")
            "바이오-로봇 시스템 공학과" -> setRelativeMajor("MECHANICAL_ENGINEERING_AND_ROBOTICS")
            "법학부" -> setRelativeMajor("LAW")
            "불어불문학과" -> setRelativeMajor("FRENCH_LANGUAGE_LITERATURE")
            "사회복지학과" -> setRelativeMajor("SOCIAL_WELFARE")
            "산업경영공학과" -> setRelativeMajor("INDUSTRIAL_AND_MANAGEMENT_ENGINEERING")
            "생명공학부(나노바이오공학전공)" -> setRelativeMajor("NANO_BIO_ENGINEERING")
            "생명공학부(생명공학전공)" -> setRelativeMajor("BIO_ENGINEERING")
            "생명과학부(분자의생명전공)" -> setRelativeMajor("MOLECULAR_MEDICAL_SCIENCE")
            "생명과학부(생명과학전공)" -> setRelativeMajor("BIOLOGICAL_SCIENCE")
            "서양화전공" -> setRelativeMajor("WESTERN_PAINTING")
            "세무회계학과" -> setRelativeMajor("TAX_ACCOUNTING")
            "소비자학과" -> setRelativeMajor("CONSUMER_SCIENCE")
            "수학과" -> setRelativeMajor("MATHEMATICS")
            "수학교육과" -> setRelativeMajor("MATHEMATICS_EDUCATION")
            "스마트물류공학전공" -> setRelativeMajor("SMART_LOGISTICS_ENGINEERING")
            "스포츠과학부" -> setRelativeMajor("SPORT_SCIENCE")
            "신소재공학과" -> setRelativeMajor("MATERIALS_SCIENCE_ENGINEERING")
            "안전공학과" -> setRelativeMajor("SAFETY_ENGINEERING")
            "에너지화학공학" -> setRelativeMajor("ENERGY_CHEMICAL_ENGINEERING")
            "역사교육과" -> setRelativeMajor("HISTORY_EDUCATION")
            "영어교육학과" -> setRelativeMajor("ENGLISH_LANGUAGE_EDUCATION")
            "영어영문학과" -> setRelativeMajor("ENGLISH_LANGUAGE_LITERATURE")
            "운동건강학부" -> setRelativeMajor("HEALTH_KINESIOLOGY")
            "유아교육과" -> setRelativeMajor("EARLY_CHILDHOOD_EDUCATION")
            "윤리교육과" -> setRelativeMajor("ETHICS_EDUCATION")
            "일본지역문화학과" -> setRelativeMajor("JAPANESE_LANGUAGE_LITERATURE")
            "일어교육과" -> setRelativeMajor("JAPANESE_LANGUAGE_EDUCATION")
            "임베디드시스템공학과" -> setRelativeMajor("EMBEDDED_SYSTEM_ENGINEERING")
            "전기공학과" -> setRelativeMajor("ELECTRICAL_ENGINEERING")
            "전자공학과" -> setRelativeMajor("INFORMATION_TELECOMMUNICATION_ENGINEERING")
            "정보통신학과" -> setRelativeMajor("Information Telecommunication Engineering")
            "정치외교학과" -> setRelativeMajor("POLITICAL_INTERNATIONAL")
            "중어중문학과" -> setRelativeMajor("CHINESE_LANGUAGE_LITERATURE")
            "창의인재개발학과" -> setRelativeMajor("CREATIVE_HUMAN_RESOURCE_DEVELOPMENT")
            "체육교육과" -> setRelativeMajor("PHYSICAL_EDUCATION")
            "컴퓨터공학부" -> setRelativeMajor("COMPUTER_SCIENCE_ENGINEERING")
            "테크노경영학과" -> setRelativeMajor("TECHNOLOGY_MANAGEMENT")
            "패션산업학과" -> setRelativeMajor("FASHION_INDUSTRY")
            "한국화전공" -> setRelativeMajor("KOREAN_PAINTING")
            "해양학과" -> setRelativeMajor("MARINE_SCIENCE")
            "행정학과" -> setRelativeMajor("PUBLIC_ADMINISTRATION")
            "화학과" -> setRelativeMajor("CHEMISTRY")
            "환경공학" -> setRelativeMajor("ENVIRONMENTAL_ENGINEERING")
            else -> setRelativeMajor("null")
        }

    }

    // 스터디 생성 api
    fun createStudy(params: CallBackIntegerListener) {
        val req = CreateStudyRequest(
            urlEditText.value.toString(),
            false,
            studyContent.value.toString(),
            gender.value.toString(),
            relativeMajor.value.toString(),
            howMuch.value.toString().toInt(),
            whatFee.value.toString(),
            editEndDay.value.toString(),
            persons.value.toString().toInt(),
            editStartDay.value.toString(),
            meetMethod.value.toString(),
            studyTitle.value.toString()
        )
        Log.d(tag, "로그인-request데이터" + req.toString())
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.setCreateStudy(req)
                if (response.isSuccessful) {
                    Log.d(tag, "스터디 생성 code" + response.code().toString())
                    val result = response.body()
                    params.isSuccess(result!!)
                    setInit() // 값 초기화 진행
                } else {
                    Log.e(tag, "스터디 생성 실패 code" + response.code().toString())
                }
            } catch (e: Exception) {
                Log.e(tag, "스터디 생성 Exception: ${e.message}")
            }
        }
    }

    // 수정하기 - 스터디 단건 조회 api연결 - 반환값 바로 반영하기 위해 contentviewModel 함수랑 중복
    fun getMyCreatedStudy(postId: Int) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getStudyContent(postId)
                if (response.isSuccessful) {
                    val result = response.body() as StudyContentResponseM

                    urlEditText.value = result.chatUrl
                    studyTitle.value = result.title
                    Log.e("스터디 타이틀", studyTitle.value.toString())
                    val startDateBuilder: StringBuilder = StringBuilder()
                    val endDateBuilder: StringBuilder = StringBuilder()

                    startDateBuilder.append(result.createdDate[0])
                        .append("년 ")
                        .append(result.createdDate[1])
                        .append("월 ")
                        .append(result.createdDate[2])
                        .append("일 ")
                    endDateBuilder.append(result.studyEndDate[0])
                        .append("년 ")
                        .append(result.studyEndDate[1])
                        .append("월 ")
                        .append(result.studyEndDate[2])
                        .append("일 ")
                    studyContent.value = result.content
                    when (result.filteredGender) {
                        "FEMALE" -> setFemale(true)
                        "MALE" -> setMale(true)
                        "NULL" -> setRegardlessOfGender(true)
                    }
                    setRelativeMajor(result.major) // 통신용
                    val koreanMajor = function.convertToKoreanMajor(result.major)
                    setPostRelativeMajor(koreanMajor) // 화면 chip표시용

                    if (result.penalty == 0) {
                        setSelectedFee(false)
                        whatFee.value = ""
                    } else {
                        setSelectedFee(true)
                        howMuch.value = result.penalty.toString()
                        whatFee.value = result.penaltyWay.toString()
                    }

                    setEndDay(endDateBuilder.toString())
                    setStartDay(startDateBuilder.toString())
                    persons.value = result.studyPerson.toString()

                    when (result.studyWay) {
                        "CONTACT" -> setOffline(true)
                        "UNTACT" -> setOnline(true)
                        "MIX" -> setMix(true)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "스터디 수정 단건 조회 Exception: ${e.message}")
            }
        }
    }

    // 스터디 수정 api
    fun correctStudy(postId: Int, prams: CallBackIntegerListener) {
        val correctStudyRequest = CorrectStudyRequest(
            urlEditText.value.toString(),
            studyContent.value.toString(),
            gender.value.toString(),
            relativeMajor.value.toString(),
            howMuch.value.toString().toInt(),
            whatFee.value.toString(),
            postId,
            editEndDay.value.toString(),
            persons.value.toString().toInt(),
            editStartDay.value.toString(),
            meetMethod.value.toString(),
            studyTitle.value.toString()
        )
        Log.e("스터디 수정값 ", correctStudyRequest.toString() + "_endDay: " + _endDay.value.toString())
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.correctMyStudy(correctStudyRequest)
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d(tag, "postId$result")
                    prams.isSuccess(result!!)
                } else {
                    Log.e(tag, "스터디 수정 불가")
                }
            } catch (e: Exception) {
                e.stackTrace
                Log.e(tag, "스터디 content 수정 Exception: ${e.message}")
            }
        }
    }

    // 입력 확인 후 생성 버튼 가능 여부
    fun setButtonEnable() {
        Log.d(tag, "버튼 확인 호출됨")
        if (!urlEditText.value.isNullOrEmpty() && !studyTitle.value.isNullOrEmpty() && !studyContent.value.isNullOrEmpty()
            && !persons.value.isNullOrEmpty() && persons.value.toString()
                .toInt() > 0 && !editStartDay.value.isNullOrEmpty() && !editEndDay.value.isNullOrEmpty()
            && !relativeMajor.value.isNullOrEmpty() && relativeMajor.value != "null" && !gender.value.isNullOrEmpty() && !meetMethod.value.isNullOrEmpty()
        ) {
            if (selectedFee.value == true) {
                if (!whatFee.value.isNullOrEmpty() && !howMuch.value.isNullOrEmpty() && howMuch.value.toString()
                        .toInt() > 0
                ) {
                    Log.d(tag, "통신시작" + selectedFee.value.toString())
                    setCompleteBtn(true)
                } else {
                    Log.d(tag, "입력불충분" + selectedFee.value.toString())
                    setCompleteBtn(false)
                }
            } else {
                Log.d(tag, "통신시작" + selectedFee.value.toString())
                setCompleteBtn(true)
            }
        } else {
            Log.d(tag, "입력불충분" + selectedFee.value.toString())
            setCompleteBtn(false)
        }
    }

    //뒤로 가기시 알림 띄울지 말지
    // 아무것도 안써져있는경우 메시지 안 띄움 return false
    fun goBack(): Boolean {
        return !(urlEditText.value.isNullOrEmpty() && studyTitle.value.isNullOrEmpty() && studyContent.value.isNullOrEmpty()
                && persons.value.isNullOrEmpty() && editStartDay.value.isNullOrEmpty() && editEndDay.value.isNullOrEmpty()
                && (relativeMajor.value.isNullOrEmpty() || relativeMajor.value != "null") && gender.value.isNullOrEmpty() && meetMethod.value.isNullOrEmpty()
                && selectedFee.value.toString()
            .isNotEmpty() && whatFee.value.isNullOrEmpty() && howMuch.value.toString()
            .toInt() >= 0)
    }

}
