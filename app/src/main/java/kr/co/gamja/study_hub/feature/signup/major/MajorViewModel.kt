package kr.co.gamja.study_hub.feature.signup.major

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.RegisterErrorResponse
import kr.co.gamja.study_hub.data.model.SignupRequest
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class MajorViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    fun requestSignup(params: RegisterCallback) {
        val signupReq = SignupRequest(
            User.email!!, User.gender!!,
            User.grade!!, User.nickname!!, User.password!!
        )
        Log.d(tag, "회원가입 요청시 데이터확인" + signupReq.toString())

        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.signup(signupReq)
                if (response.isSuccessful) {
                    Log.d(tag, "회원가입 성공" + response.code().toString())
                    params.onSucess(true)
                } else {
                    Log.d(tag, "회원가입 error code" + response.code().toString())
                    val errorResponse: RegisterErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), RegisterErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val message = errorResponse.message.toString()
                        val status = errorResponse.status.toString()
                        params.onFail(true, status, message)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "회원가입 Exception: ${e.message}")
            }

        }
    }

    // 힉과 데이터
    fun setUserMajor(item: String) {
        when (item) {
            "공연예술과" -> User.grade = "PERFORMING_ART"
            "IBE전공" -> User.grade = "IBE"
            "건설환경공학" -> User.grade = "CIVIL_AND_ENVIRONMENTAL_ENGINEERING"
            "건축공학" -> User.grade = "ARCHITECTURAL_ENGINEERING"
            "경영학부" -> User.grade = "BUSINESS_ADMINISTRATION"
            "경제학과" -> User.grade = "ECONOMICS"
            "국어교육과" -> User.grade = "KOREAN_LANGUAGE_EDUCATION"
            "국어국문학과" -> User.grade = "KOREAN_LANGUAGE_LITERATURE"
            "기계공학과" -> User.grade = "MECHANICAL_ENGINEERING"
            "데이터과학과" -> User.grade = "BUSINESS_DATA_SCIENCE"
            "도시건축학" -> User.grade = "ARCHITECTURE_AND_URBAN_DESIGN"
            "도시공학과" -> User.grade = "URBAN_ENGINEERING"
            "도시행정학과" -> User.grade = "URBAN_POLICY_AND_ADMINISTRATION"
            "독어독문학과" -> User.grade = "GERMAN_LANGUAGE_LITERATURE"
            "동북아통상전공" -> User.grade = "SCHOOL_OF_NORTHEAST_ASIAN"
            "디자인학부" -> User.grade = "DESIGN"
            "무역학부" -> User.grade = "INTERNATIONAL_TRADE"
            "문헌정보학과" -> User.grade = "LIBRARY_AND_INFORMATION"
            "물리학과" -> User.grade = "PHYSICS"
            "미디어커뮤니케이션학과" -> User.grade = "MASS_COMMUNICATION"
            "바이오-로봇 시스템 공학과" -> User.grade = "MECHANICAL_ENGINEERING_AND_ROBOTICS"
            "법학부" -> User.grade = "LAW"
            "불어불문학과" -> User.grade = "FRENCH_LANGUAGE_LITERATURE"
            "사회복지학과" -> User.grade = "SOCIAL_WELFARE"
            "산업경영공학과" -> User.grade = "INDUSTRIAL_AND_MANAGEMENT_ENGINEERING"
            "생명공학부(나노바이오공학전공)" -> User.grade = "NANO_BIO_ENGINEERING"
            "생명공학부(생명공학전공)" -> User.grade = "BIO_ENGINEERING"
            "생명과학부(분자의생명전공)" -> User.grade = "MOLECULAR_MEDICAL_SCIENCE"
            "생명과학부(생명과학전공)" -> User.grade = "BIOLOGICAL_SCIENCE"
            "서양화전공" -> User.grade = "WESTERN_PAINTING"
            "세무회계학과" -> User.grade = "TAX_ACCOUNTING"
            "소비자학과" -> User.grade = "CONSUMER_SCIENCE"
            "수학과" -> User.grade = "MATHEMATICS"
            "수학교육과" -> User.grade = "MATHEMATICS_EDUCATION"
            "스마트물류공학전공" -> User.grade = "SMART_LOGISTICS_ENGINEERING"
            "스포츠과학부" -> User.grade = "SPORT_SCIENCE"
            "신소재공학과" -> User.grade = "MATERIALS_SCIENCE_ENGINEERING"
            "안전공학과" -> User.grade = "SAFETY_ENGINEERING"
            "에너지화학공학" -> User.grade = "ENERGY_CHEMICAL_ENGINEERING"
            "역사교육과" -> User.grade = "HISTORY_EDUCATION"
            "영어교육학과" -> User.grade = "ENGLISH_LANGUAGE_EDUCATION"
            "영어영문학과" -> User.grade = "ENGLISH_LANGUAGE_LITERATURE"
            "운동건강학부" -> User.grade = "HEALTH_KINESIOLOGY"
            "유아교육과" -> User.grade = "EARLY_CHILDHOOD_EDUCATION"
            "윤리교육과" -> User.grade = "ETHICS_EDUCATION"
            "일본지역문화학과" -> User.grade = "JAPANESE_LANGUAGE_LITERATURE"
            "일어교육과" -> User.grade = "JAPANESE_LANGUAGE_EDUCATION"
            "임베디드시스템공학과" -> User.grade = "EMBEDDED_SYSTEM_ENGINEERING"
            "전기공학과" -> User.grade = "ELECTRICAL_ENGINEERING"
            "전자공학과" -> User.grade = "INFORMATION_TELECOMMUNICATION_ENGINEERING"
            "정보통신학과" -> User.grade = "Information Telecommunication Engineering"
            "정치외교학과" -> User.grade = "POLITICAL_INTERNATIONAL"
            "중어중문학과" -> User.grade = "CHINESE_LANGUAGE_LITERATURE"
            "창의인재개발학과" -> User.grade = "CREATIVE_HUMAN_RESOURCE_DEVELOPMENT"
            "체육교육과" -> User.grade = "PHYSICAL_EDUCATION"
            "컴퓨터공학부" -> User.grade = "COMPUTER_SCIENCE_ENGINEERING"
            "테크노경영학과" -> User.grade = "TECHNOLOGY_MANAGEMENT"
            "패션산업학과" -> User.grade = "FASHION_INDUSTRY"
            "한국화전공" -> User.grade = "KOREAN_PAINTING"
            "해양학과" -> User.grade = "MARINE_SCIENCE"
            "행정학과" -> User.grade = "PUBLIC_ADMINISTRATION"
            "화학과" -> User.grade = "CHEMISTRY"
            "환경공학" -> User.grade = "ENVIRONMENTAL_ENGINEERING"
        }
    }
}

// 회원가입 성공여부 callback
interface RegisterCallback {
    fun onSucess(isValid: Boolean = false)
    fun onFail(eIsValid: Boolean = false, eStatus: String, eMessage: String)
}