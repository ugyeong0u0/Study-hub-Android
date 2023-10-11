package kr.co.gamja.study_hub.feature.mypage.changeMajor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ChangeMajorRequest
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener

class ChangeMajorViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    // 통신시 사용 학과 설정
    private val _major = MutableLiveData<String>()
    val major: LiveData<String> get() = _major

    // 완료 버튼 활성화 여부
    private val _enableBtn = MutableLiveData<Boolean>(false)
    val enableBtn: LiveData<Boolean> get() = _enableBtn

    private fun setEnableBtn(new: Boolean) {
        _enableBtn.value = new
    }

    private fun setMajor(newMajor: String) {
        _major.value = newMajor
        setEnableBtn(true)
        Log.d(tag, "학과 선택값" + major.value.toString())
    }

    fun changeMajor(){
        val req = ChangeMajorRequest(major = _major.value.toString())
        viewModelScope.launch {
            val response=AuthRetrofitManager.api.putNewMajor(req)
            if (response.isSuccessful){
                Log.d(tag, " 학과 수정 성공 ${response.code()}")
            }else{
                Log.e(tag, "학과 수정 api 에러 ${response.code()} ")
            }
        }
    }


    fun setUserMajor(item: String) {
        when (item) {
            "공연예술과" -> setMajor("PERFORMING_ART")
            "IBE전공" -> setMajor("IBE")
            "건설환경공학" -> setMajor("CIVIL_AND_ENVIRONMENTAL_ENGINEERING")
            "건축공학" -> setMajor("ARCHITECTURAL_ENGINEERING")
            "경영학부" -> setMajor("BUSINESS_ADMINISTRATION")
            "경제학과" -> setMajor("ECONOMICS")
            "국어교육과" -> setMajor("KOREAN_LANGUAGE_EDUCATION")
            "국어국문학과" -> setMajor("KOREAN_LANGUAGE_LITERATURE")
            "기계공학과" -> setMajor("MECHANICAL_ENGINEERING")
            "데이터과학과" -> setMajor("BUSINESS_DATA_SCIENCE")
            "도시건축학" -> setMajor("ARCHITECTURE_AND_URBAN_DESIGN")
            "도시공학과" -> setMajor("URBAN_ENGINEERING")
            "도시행정학과" -> setMajor("URBAN_POLICY_AND_ADMINISTRATION")
            "독어독문학과" -> setMajor("GERMAN_LANGUAGE_LITERATURE")
            "동북아통상전공" -> setMajor("SCHOOL_OF_NORTHEAST_ASIAN")
            "디자인학부" -> setMajor("DESIGN")
            "무역학부" -> setMajor("INTERNATIONAL_TRADE")
            "문헌정보학과" -> setMajor("LIBRARY_AND_INFORMATION")
            "물리학과" -> setMajor("PHYSICS")
            "미디어커뮤니케이션학과" -> setMajor("MASS_COMMUNICATION")
            "바이오-로봇 시스템 공학과" -> setMajor("MECHANICAL_ENGINEERING_AND_ROBOTICS")
            "법학부" -> setMajor("LAW")
            "불어불문학과" -> setMajor("FRENCH_LANGUAGE_LITERATURE")
            "사회복지학과" -> setMajor("SOCIAL_WELFARE")
            "산업경영공학과" -> setMajor("INDUSTRIAL_AND_MANAGEMENT_ENGINEERING")
            "생명공학부(나노바이오공학전공)" -> setMajor("NANO_BIO_ENGINEERING")
            "생명공학부(생명공학전공)" -> setMajor("BIO_ENGINEERING")
            "생명과학부(분자의생명전공)" -> setMajor("MOLECULAR_MEDICAL_SCIENCE")
            "생명과학부(생명과학전공)" -> setMajor("BIOLOGICAL_SCIENCE")
            "서양화전공" -> setMajor("WESTERN_PAINTING")
            "세무회계학과" -> setMajor("TAX_ACCOUNTING")
            "소비자학과" -> setMajor("CONSUMER_SCIENCE")
            "수학과" -> setMajor("MATHEMATICS")
            "수학교육과" -> setMajor("MATHEMATICS_EDUCATION")
            "스마트물류공학전공" -> setMajor("SMART_LOGISTICS_ENGINEERING")
            "스포츠과학부" -> setMajor("SPORT_SCIENCE")
            "신소재공학과" -> setMajor("MATERIALS_SCIENCE_ENGINEERING")
            "안전공학과" -> setMajor("SAFETY_ENGINEERING")
            "에너지화학공학" -> setMajor("ENERGY_CHEMICAL_ENGINEERING")
            "역사교육과" -> setMajor("HISTORY_EDUCATION")
            "영어교육학과" -> setMajor("ENGLISH_LANGUAGE_EDUCATION")
            "영어영문학과" -> setMajor("ENGLISH_LANGUAGE_LITERATURE")
            "운동건강학부" -> setMajor("HEALTH_KINESIOLOGY")
            "유아교육과" -> setMajor("EARLY_CHILDHOOD_EDUCATION")
            "윤리교육과" -> setMajor("ETHICS_EDUCATION")
            "일본지역문화학과" -> setMajor("JAPANESE_LANGUAGE_LITERATURE")
            "일어교육과" -> setMajor("JAPANESE_LANGUAGE_EDUCATION")
            "임베디드시스템공학과" -> setMajor("EMBEDDED_SYSTEM_ENGINEERING")
            "전기공학과" -> setMajor("ELECTRICAL_ENGINEERING")
            "전자공학과" -> setMajor("INFORMATION_TELECOMMUNICATION_ENGINEERING")
            "정보통신학과" -> setMajor("Information Telecommunication Engineering")
            "정치외교학과" -> setMajor("POLITICAL_INTERNATIONAL")
            "중어중문학과" -> setMajor("CHINESE_LANGUAGE_LITERATURE")
            "창의인재개발학과" -> setMajor("CREATIVE_HUMAN_RESOURCE_DEVELOPMENT")
            "체육교육과" -> setMajor("PHYSICAL_EDUCATION")
            "컴퓨터공학부" -> setMajor("COMPUTER_SCIENCE_ENGINEERING")
            "테크노경영학과" -> setMajor("TECHNOLOGY_MANAGEMENT")
            "패션산업학과" -> setMajor("FASHION_INDUSTRY")
            "한국화전공" -> setMajor("KOREAN_PAINTING")
            "해양학과" -> setMajor("MARINE_SCIENCE")
            "행정학과" -> setMajor("PUBLIC_ADMINISTRATION")
            "화학과" -> setMajor("CHEMISTRY")
            "환경공학" -> setMajor("ENVIRONMENTAL_ENGINEERING")
        }

    }
}