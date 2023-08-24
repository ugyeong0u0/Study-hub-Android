package kr.co.gamja.study_hub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.view.Menu
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kr.co.gamja.study_hub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

//        setSupportActionBar(binding.toolbar)
//        setupActionBarWithNavController(navController)

        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{
            _,_,arguments ->
            binding.bottomNav.isVisible=arguments?.getBoolean("showBottomNav",false)==true
        }
    }

   /* // 메뉴 활성화
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_navigation,menu)
        return true
    }
    // 메뉴 아이템 클릭
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) ||super.onOptionsItemSelected(item)
    }*/
    /*// 앱바 <-(뒤로가기) 처리
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }*/
}