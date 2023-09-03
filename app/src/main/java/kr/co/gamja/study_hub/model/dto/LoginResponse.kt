package kr.co.gamja.study_hub.model.dto

import kr.co.gamja.study_hub.model.dto.Data

data class LoginResponse(
    val code: Int,
    val `data`: Data,
    val msg: String
)