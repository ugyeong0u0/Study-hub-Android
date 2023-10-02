package kr.co.gamja.study_hub.feature.studypage.createStudy

// 캘린더 현재 선택된 날짜만 동그라미 표시(중복x)
data class InfoOfDays(
    val day:String,
    var isSelected:Boolean
)