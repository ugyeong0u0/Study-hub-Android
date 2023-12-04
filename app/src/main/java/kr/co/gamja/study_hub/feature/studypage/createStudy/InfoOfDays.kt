package kr.co.gamja.study_hub.feature.studypage.createStudy


// 캘린더 현재 선택된 날짜만 동그라미 표시(중복x)
data class InfoOfDays(
    var yearMonthDay:String, // yyyyMM
    val infoDay:String,
    var isSelected:Boolean
)
// 선택된 날짜 유지
data class InfoOfSelectedDay(
    var selectedYearMonth:String?,
    var selectedDay:String?
)
// 시작 날짜 < 종료 날짜 표시
data class StartDate(
    var selectedYearMonth:String?,
    var selectedDay:String?
)