package kr.co.gamja.study_hub.data

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName

// Post
//TODO("닉네임이나 학과도 보내야하는지")
data class Auth(
    @SerializedName("email_password")
    val email: String,
    val password: String,

)