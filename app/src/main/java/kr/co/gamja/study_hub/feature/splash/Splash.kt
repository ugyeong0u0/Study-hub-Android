package kr.co.gamja.study_hub.feature.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.feature.login.LoginCallback
import kr.co.gamja.study_hub.feature.main.MainActivity

class Splash : AppCompatActivity() {
    val tagMsg = this.javaClass.simpleName
    private lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        autoUserLoginF() // 자동로그인 실행
        getSupportActionBar()?.hide();
    }

    fun autoUserLoginF() {
        var refreshToken: String? = null

        CoroutineScope(Dispatchers.IO).launch() {
            launch {
                refreshToken = App.getInstance().getDataStore().refreshToken.first()
                Log.d(tagMsg, "1. 데이터스토어에서 불러온 리프레시토큰 " + refreshToken)
            }.join()
            launch {
                if (refreshToken != null) {
                    Log.d(tagMsg, "2. 데이터스토어에서 불러온 리프레시토큰으로 통신실행 : " + refreshToken)
                    viewModel.autoLogin(refreshToken!!, object : LoginCallback {
                        override fun onSuccess(
                            isBoolean: Boolean,
                            accessToken: String,
                            refreshToken: String
                        ) {
                            if (isBoolean) {
                                Log.d(tagMsg, "3. 리프레시토큰 유효하고 자동로그인 성공")

                                // 리프레시 토큰이 유효하다면, 리프레시 액세스토큰 새롭게 저장
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dataStoreInstance = App.getInstance().getDataStore()

                                    dataStoreInstance.clearDataStore() // 초기화 후
                                    dataStoreInstance.setAccessToken(accessToken)
                                    dataStoreInstance.setRefreshToken(refreshToken)
                                    Log.d(tagMsg, "4.새리프레시 토큰 확인 $refreshToken")
                                }
                                Handler().postDelayed({
                                    val intent = Intent(this@Splash, MainActivity::class.java)
                                    intent.putExtra("autoLogin", true)
                                    startActivity(intent)
                                    finish()
                                }, 2000)
                            }
                        }

                        override fun onfail(isBoolean: Boolean) {
                            if (isBoolean) {
                                Log.e(tagMsg, "3. 리프레시토큰 유효x ")
                                Toast.makeText(this@Splash, "리프레쉬 토큰 만료", Toast.LENGTH_LONG)
                                    .show()

                                Log.d(tagMsg, "리프레시 토큰 지우기")
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dataStoreInstance = App.getInstance().getDataStore()
                                    dataStoreInstance.clearDataStore() // 초기화 후
                                }

                                Handler().postDelayed({
                                    val intent = Intent(this@Splash, MainActivity::class.java)
                                    intent.putExtra("autoLogin", false)
                                    startActivity(intent)
                                    finish()
                                }, 2000)

                            }
                        }
                    })
                } else {
                    Log.d(tagMsg, "2. 리프레시 토큰 null임 ")

                    Handler().postDelayed({
                        val intent = Intent(this@Splash, MainActivity::class.java)
                        intent.putExtra("autoLogin", false)
                        startActivity(intent)
                        finish()
                    }, 2000)

                }
            }
        }
    }

}