package kr.co.gamja.study_hub.data

import android.os.Message
import com.google.gson.annotations.SerializedName

/*UserInfo: nickname, major
message : 에러 메시지*/
data class UserInfoResponse(
    @SerializedName("userInfoResponse")
    val userInfo: UserInfo,
    val message: String
)