package kr.co.gamja.study_hub.custom

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import kr.co.gamja.study_hub.R
import android.content.Context
import android.util.Log

/* 구성
loginTextWatcher: 이메일, 비밀번호 editText 확인 코드
authTextWatcher : 회원가입페이지 editText 확인 코드*/

const val EMAIL = "^[a-zA-Z0-9+-\\_.]+(@inu\\.ac\\.kr)$"

//^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$).*$
// ^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*]).{8,15}$
// 세개 다 하나 들어가는 패스워드로 함
const val PASSWORD = """^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*]).{8,15}$"""
const val existEmail = "a@inu.ac.kr"

class FunctionLogin(val context: Context) {
    // 입력방식 확인
    var flag_email: Boolean = false // 로그인&비밀번호
    var flag_password: Boolean = false
    var flag_auth = false // 인증코드
    var flag_nickname = false // 닉네임

    //loginTextWatcher: 이메일, 비밀번호 editText 확인 코드
    fun loginTextWatcher(
        emailLayout: TextInputLayout?,
        passLayout: TextInputLayout?,
        flag: Int
    ): Boolean {
        // edit email 부분
        if (emailLayout != null) {
            emailLayout.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    var id = emailLayout.editText?.text.toString()
                    if (!(id.matches(EMAIL.toRegex()))) {
                        emailLayout.error = context.getString(R.string.txterror_email)
                    } else if (flag == 1 && id == existEmail) {
                        emailLayout.error = context.getString(R.string.txterror2_email)
                    } else {
                        emailLayout.error = null
                        flag_email = true
                    }
                    Log.d("이메일함수안1 ", "$flag_email")
                }
            })
        } else {
            flag_email = true
        }
        Log.d("이메일만", "$flag_email")

        // page 회원가입 : 비번
        if (passLayout != null) {
            passLayout.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    var password = passLayout.editText?.text.toString()
                    if (!(password.length < 10) or !(password.matches(PASSWORD.toRegex()))) { // todo("확인필요")
                        passLayout.error =
                            context.getString(R.string.txterror_password) // 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자 ?
                    } else {
                        passLayout.error = null
                        flag_password = true
                    }
                }
            })
        } else {
            flag_password = true
        }
        Log.d("이메일", "$flag_email+$flag_password")
        if ((flag_email == true) and (flag_password == true)) {
            return true
        } else {
            return false
        }
    }

    //authTextWatcher : 회원가입페이지 editText 확인 코드
    fun authTextWatcher(fcaAuthLayout: TextInputLayout): Boolean {
        fcaAuthLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                var authcode = fcaAuthLayout.editText?.text.toString()
                if ((authcode.length > 4) or (authcode.toInt() != 1)) { // todo("임시비번설정")
                    fcaAuthLayout.error =
                        context.getString(R.string.txterror_password) // TODO("인증코드 틀렸을때 에러 메시지")
                } else {
                    fcaAuthLayout.error = null
                    flag_auth = true
                }
            }

        })
        return flag_auth
    }

    // page - 회원가입 : 닉네임
    fun nicknameWatcher(fca03NicknameLayout: TextInputLayout): Boolean {

        fca03NicknameLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                var nickname = fca03NicknameLayout.editText?.text.toString()
                if (!(nickname.length > 2) or (nickname != "a")) { // todo("임시")
                    fca03NicknameLayout.error =
                        context.getString(R.string.txterror_password) // TODO("인증코드 틀렸을때 에러 메시지")
                } else {
                    fca03NicknameLayout.error = null
                    //TODO("사용가능한닉네임일 경우 초록색 표시")
                    flag_nickname = true
                }
            }

        })
        return flag_nickname
    }

}