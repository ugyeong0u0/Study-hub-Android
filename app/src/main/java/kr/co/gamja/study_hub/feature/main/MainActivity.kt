package kr.co.gamja.study_hub.feature.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.ActivityMainBinding
import kr.co.gamja.study_hub.feature.login.LoginCallback

class MainActivity : AppCompatActivity() {
    private val tag = this.javaClass.simpleName
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // 자동로그인
        autoLogin()
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, _, arguments ->
            binding.bottomNav.isVisible = arguments?.getBoolean("showBottomNav", false) == true
        }
        // 동일한 바텀 메뉴 재선택시 이벤트 발생 방지
        binding.bottomNav.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainHomeFragment -> {}
                R.id.studyMainFragment -> {}
                R.id.mypageMainFragment -> {}
            }
        }
    }

    fun autoLogin() {
        var refreshToken: String? = null

        CoroutineScope(Dispatchers.Main).launch() {
            launch {
                refreshToken = App.getInstance().getDataStore().refreshToken.first()
                Log.d(tag, "데이터스토어에서 불러온 리프레시토큰 " + refreshToken)
            }.join()
            launch {
                if (refreshToken != null) {
                    Log.d(tag, "데이터스토어에서 불러온 리프레시토큰 통신실행? " + refreshToken)
                    viewModel.autoLogin(refreshToken!!, object : LoginCallback {
                        override fun onSuccess(
                            isBoolean: Boolean,
                            accessToken: String,
                            refreshToken: String
                        ) {
                            if (isBoolean) {
                                Log.d(tag, "리프레시토큰 유효하고 자동로그인 성공, 그래프 변동 x ")
                                val navInflater = navController.navInflater
                                val navGraph = navInflater.inflate(R.navigation.nav_graph_from_home)
                                navController.graph = navGraph
                                // 리프레시 토큰이 유효하다면, 리프레시 액세스토큰 새롭게 저장
                                CoroutineScope(Dispatchers.Main).launch {
                                    val dataStoreInstance = App.getInstance().getDataStore()

                                    dataStoreInstance.clearDataStore() // 초기화 후
                                    dataStoreInstance.setAccessToken(accessToken)
                                    dataStoreInstance.setRefreshToken(refreshToken)
                                    Log.d(tag, "새리프레시 토큰 확인 $refreshToken")
                                }
                            }
                        }

                        override fun onfail(isBoolean: Boolean) {
                            if (isBoolean) {
                                Log.e(tag, "리프레시토큰 유효x")
                                Toast.makeText(this@MainActivity, "리프레쉬 토큰 만료", Toast.LENGTH_LONG)
                                    .show()
                                val navInflater = navController.navInflater
                                val navGraph =
                                    navInflater.inflate(R.navigation.nav_graph_login_signup)
                                navController.graph = navGraph
                            }
                        }
                    })
                } else {
                    Log.d(tag, "리프레시 토큰 null로 넘어감 ")
                    val navInflater = navController.navInflater
                    val navGraph = navInflater.inflate(R.navigation.nav_graph_login_signup)
                    navController.graph = navGraph
                }
            }
        }
    }

}