package kr.co.gamja.study_hub.global

import kr.co.gamja.study_hub.feature.studypage.createStudy.CreateStudyViewModel

class Functions {
    // 전공이넘값 한글로 변경
    fun convertToKoreanMajor(majorValue: String): String {
        when (majorValue) {
            "PERFORMING_ART" -> return "공연예술과"
            "IBE" -> return "IBE전공"
            "CIVIL_AND_ENVIRONMENTAL_ENGINEERING" -> return "건설환경공학"
            "ARCHITECTURAL_ENGINEERING" -> return "건축공학"
            "BUSINESS_ADMINISTRATION" -> return "경영학부"
            "ECONOMICS" -> return "경제학과"
            "KOREAN_LANGUAGE_EDUCATION" -> return "국어교육과"
            "KOREAN_LANGUAGE_LITERATURE" -> return "국어국문학과"
            "MECHANICAL_ENGINEERING" -> return "기계공학과"
            "BUSINESS_DATA_SCIENCE" -> return "데이터과학과"
            "ARCHITECTURE_AND_URBAN_DESIGN" -> return "도시건축학"
            "URBAN_ENGINEERING" -> return "도시공학과"
            "URBAN_POLICY_AND_ADMINISTRATION" -> return "도시행정학과"
            "GERMAN_LANGUAGE_LITERATURE" -> return "독어독문학과"
            "SCHOOL_OF_NORTHEAST_ASIAN" -> return "동북아통상전공"
            "DESIGN" -> return "디자인학부"
            "INTERNATIONAL_TRADE" -> return "무역학부"
            "LIBRARY_AND_INFORMATION" -> return "문헌정보학과"
            "PHYSICS" -> return "물리학과"
            "MASS_COMMUNICATION" -> return "미디어커뮤니케이션학과"
            "MECHANICAL_ENGINEERING_AND_ROBOTICS" -> return "바이오-로봇 시스템 공학과"
            "LAW" -> return "법학부"
            "FRENCH_LANGUAGE_LITERATURE" -> return "불어불문학과"
            "SOCIAL_WELFARE" -> return "사회복지학과"
            "INDUSTRIAL_AND_MANAGEMENT_ENGINEERING" -> return "산업경영공학과"
            "NANO_BIO_ENGINEERING" -> return "생명공학부(나노바이오공학전공)"
            "BIO_ENGINEERING" -> return "생명공학부(생명공학전공)"
            "MOLECULAR_MEDICAL_SCIENCE" -> return "생명과학부(분자의생명전공)"
            "BIOLOGICAL_SCIENCE" -> return "생명과학부(생명과학전공)"
            "WESTERN_PAINTING" -> return "서양화전공"
            "TAX_ACCOUNTING" -> return "세무회계학과"
            "CONSUMER_SCIENCE" -> return "소비자학과"
            "MATHEMATICS" -> return "수학과"
            "MATHEMATICS_EDUCATION" -> return "수학교육과"
            "SMART_LOGISTICS_ENGINEERING" -> return "스마트물류공학전공"
            "SPORT_SCIENCE" -> return "스포츠과학부"
            "MATERIALS_SCIENCE_ENGINEERING" -> return "신소재공학과"
            "SAFETY_ENGINEERING" -> return "안전공학과"
            "ENERGY_CHEMICAL_ENGINEERING" -> return "에너지화학공학"
            "HISTORY_EDUCATION" -> return "역사교육과"
            "ENGLISH_LANGUAGE_EDUCATION" -> return "영어교육학과"
            "ENGLISH_LANGUAGE_LITERATURE" -> return "영어영문학과"
            "HEALTH_KINESIOLOGY" -> return "운동건강학부"
            "EARLY_CHILDHOOD_EDUCATION" -> return "유아교육과"
            "ETHICS_EDUCATION" -> return "윤리교육과"
            "JAPANESE_LANGUAGE_LITERATURE" -> return "일본지역문화학과"
            "JAPANESE_LANGUAGE_EDUCATION" -> return "일어교육과"
            "EMBEDDED_SYSTEM_ENGINEERING" -> return "임베디드시스템공학과"
            "ELECTRICAL_ENGINEERING" -> return "전기공학과"
            "ELECTRONICS_ENGINEERING" -> return "전자공학과"
            "INFORMATION_TELECOMMUNICATION_ENGINEERING" -> return "정보통신학과"
            "POLITICAL_INTERNATIONAL" -> return "정치외교학과"
            "CHINESE_LANGUAGE_LITERATURE" -> return "중어중문학과"
            "CREATIVE_HUMAN_RESOURCE_DEVELOPMENT" -> return "창의인재개발학과"
            "PHYSICAL_EDUCATION" -> return "체육교육과"
            "COMPUTER_SCIENCE_ENGINEERING" -> return "컴퓨터공학부"
            "TECHNOLOGY_MANAGEMENT" -> return "테크노경영학과"
            "FASHION_INDUSTRY" -> return "패션산업학과"
            "KOREAN_PAINTING" -> return "한국화전공"
            "MARINE_SCIENCE" -> return "해양학과"
            "PUBLIC_ADMINISTRATION" -> return "행정학과"
            "CHEMISTRY" -> return "화학과"
            "ENVIRONMENTAL_ENGINEERING" -> return "환경공학"
            else -> return "error"
        }
    }

    // 성별 변경 함수
    fun convertToKoreanGender(gender: String): String {
        when (gender) {
            "FEMALE" -> return "여성"
            "MALE" -> return "남성"
            "NULL" -> return "무관"
            else -> return "error"
        }
    }
    // 대면여부
    fun convertToKoreanMeetMethod(meetMethod:String):String{
        when(meetMethod){
            "CONTACT"-> return "대면"
            "UNTACT" ->return "비대면"
            "MIX" ->return "혼합"
            else -> return "error"
        }
    }
}