package kr.co.gamja.study_hub.feature.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.ActivityMainBinding
import kr.co.gamja.study_hub.feature.login.LoginCallback
import kr.co.gamja.study_hub.feature.splash.SplashViewModel

class MainActivity : AppCompatActivity() {
    private val tag = this.javaClass.simpleName
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private var autoUserLoginResult: Boolean = false // splash에서 받은 자동 로그인 통신 결과

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        autoUserLoginResult =
            intent?.getBooleanExtra("autoLogin", false) ?: false// 스플래시에서 받은 자동로그인 값
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val navInflater = navController.navInflater

        // splash에서 온 경우, 화면 재생성이 x splah 화면에서 온 로그인 여부에 따른
            if (savedInstanceState == null && autoUserLoginResult) {
                Log.i(tag,"splash 거쳐옴")
                   navController.navigate(R.id.action_global_mainHomeFragment2) // 홈으로 가기
            }


        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, _, arguments ->
            binding.bottomNav.isVisible = arguments?.getBoolean("showBottomNav", false) == true
        }
        // 동일한 바텀 메뉴 재 선택시 이벤트 발생 방지
        binding.bottomNav.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainHomeFragment -> {}
                R.id.studyMainFragment -> {}
                R.id.mypageMainFragment -> {}
            }
        }
        // 무조건 탭 누르면 탭 초기화면으로가도록
        // 특히 검색 -> 스터디 탭->홈탭 눌렀을 떄 홈탭의 메인 페이지로 가게 설정
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainHomeFragment -> {
                    navController.popBackStack(R.id.mainHomeFragment, false)
                    true
                }
                R.id.studyMainFragment -> {
                    navController.navigate(R.id.studyMainFragment)
                    true
                }
                R.id.mypageMainFragment -> {
                    navController.navigate(R.id.mypageMainFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


}