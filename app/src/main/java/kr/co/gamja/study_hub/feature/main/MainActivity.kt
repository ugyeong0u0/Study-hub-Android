package kr.co.gamja.study_hub.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val tag = this.javaClass.simpleName
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private var autoUserLoginResult: Boolean = false // splash에서 받은 자동 로그인 통신 결과
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        autoUserLoginResult = intent.getBooleanExtra("autoLogin", false) // 스플래시에서 받은 자동로그인 값

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController


        val navInflater = navController.navInflater
        // splah 화면에서 온 로그인 여부에 따른 nav graph첨부
        when (autoUserLoginResult) {
            true -> {
                val loginNavGraph = navInflater.inflate(R.navigation.nav_graph_from_home)
                navController.graph = loginNavGraph
            }
            false -> {
                val notLoginNavGraph =
                    navInflater.inflate(R.navigation.nav_graph_login_signup)
                navController.graph = notLoginNavGraph
            }
        }


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
}