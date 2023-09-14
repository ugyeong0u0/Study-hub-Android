package kr.co.gamja.study_hub.feature.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.RegisterErrorResponse
import kr.co.gamja.study_hub.data.model.SignupRequest
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class SignupViewModel : ViewModel() {
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
            /*10 -> User.grade = "BUSINESS_DATA_SCIENCE"
            11 -> User.grade = "ARCHITECTURE_AND_URBAN_DESIGN"
            12 -> User.grade = "URBAN_ENGINEERING"
            13 -> User.grade = "URBAN_POLICY_AND_ADMINISTRATION"
            14 -> User.grade = "GERMAN_LANGUAGE_LITERATURE"
            15 -> User.grade = "SCHOOL_OF_NORTHEAST_ASIAN"
            16 -> User.grade = "DESIGN"
            18 -> User.grade = "INTERNATIONAL_TRADE"
            19 -> User.grade = "LIBRARY_AND_INFORMATION"
            20 -> User.grade = "PHYSICS"
            21 -> User.grade = "MASS_COMMUNICATION"
            22 -> User.grade = "MECHANICAL_ENGINEERING_AND_ROBOTICS"
            23 -> User.grade = "LAW"
            24 -> User.grade = "FRENCH_LANGUAGE_LITERATURE"
            25 -> User.grade = "SOCIAL_WELFARE"
            26 -> User.grade = "INDUSTRIAL_AND_MANAGEMENT_ENGINEERING"
            27 -> User.grade = "NANO_BIO_ENGINEERING"
            28 -> User.grade = "BIO_ENGINEERING"
            29 -> User.grade = "MOLECULAR_MEDICAL_SCIENCE"
            30 -> User.grade = "BIOLOGICAL_SCIENCE"
            31 -> User.grade = "WESTERN_PAINTING"
            32 -> User.grade = "TAX_ACCOUNTING"
            33 -> User.grade = "CONSUMER_SCIENCE"
            34 -> User.grade = "MATHEMATICS"
            35 -> User.grade = "MATHEMATICS_EDUCATION"
            36 -> User.grade = "SMART_LOGISTICS_ENGINEERING"
            37 -> User.grade = "SPORT_SCIENCE"
            38 -> User.grade = "MATERIALS_SCIENCE_ENGINEERING"
            39 -> User.grade = "SAFETY_ENGINEERING"
            40 -> User.grade = "ENERGY_CHEMICAL_ENGINEERING"
            41 -> User.grade = "HISTORY_EDUCATION"
            42 -> User.grade = "ENGLISH_LANGUAGE_EDUCATION"
            43 -> User.grade = "ENGLISH_LANGUAGE_LITERATURE"
            44 -> User.grade = "HEALTH_KINESIOLOGY"
            45 -> User.grade = "EARLY_CHILDHOOD_EDUCATION"
            46 -> User.grade = "ETHICS_EDUCATION"
            47 -> User.grade = "JAPANESE_LANGUAGE_LITERATURE"
            48 -> User.grade = "JAPANESE_LANGUAGE_EDUCATION"
            49 -> User.grade = "EMBEDDED_SYSTEM_ENGINEERING"
            50 -> User.grade = "ELECTRICAL_ENGINEERING"
            51 -> User.grade = "INFORMATION_TELECOMMUNICATION_ENGINEERING"
            52 -> User.grade = "POLITICAL_INTERNATIONAL"
            53 -> User.grade = "CHINESE_LANGUAGE_LITERATURE"
            54 -> User.grade = "CREATIVE_HUMAN_RESOURCE_DEVELOPMENT"
            55 -> User.grade = "PHYSICAL_EDUCATION"
            56 -> User.grade = "COMPUTER_SCIENCE_ENGINEERING"
            57 -> User.grade = "TECHNOLOGY_MANAGEMENT"
            58 -> User.grade = "FASHION_INDUSTRY"
            59 -> User.grade = "KOREAN_PAINTING"
            60 -> User.grade = "MARINE_SCIENCE"
            61 -> User.grade = "PUBLIC_ADMINISTRATION"
            62 -> User.grade = "CHEMISTRY"
            63 -> User.grade = "ENVIRONMENTAL_ENGINEERING"*/
        }
    }
}

// 회원가입 성공여부 callback
interface RegisterCallback {
    fun onSucess(isValid: Boolean = false)
    fun onFail(eIsValid: Boolean = false, eStatus: String, eMessage: String)
}