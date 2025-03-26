package umc.study.umc_8th

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavOptions

class TaskTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_two)

        //fragmentManager랑 navController 사용  fragment 이동 관리 + 백스택
        //FragmentContainer를 HavHostFragment처럼 사용하자
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.ct_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_myapp)
        bottomNavigationView.setupWithNavController(navController)
        

        bottomNavigationView.setOnItemSelectedListener { item->

            //백스택에 fragment들이 쌓이는 것을 막기
            val navOptions = NavOptions.Builder().
                setPopUpTo(R.id.nav_graph, true) //전체 그래프에 popup옵션
                .build()

            when(item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home, null, navOptions)
                    true
                }
                R.id.navigation_favorite -> {
                    navController.navigate(R.id.navigation_favorite, null, navOptions)
                    true
                }
                R.id.navigation_setting -> {
                    navController.navigate(R.id.navigation_setting, null, navOptions)
                    true
                }
                R.id.navigation_myapge -> {
                    navController.navigate(R.id.navigation_myapge, null, navOptions)
                    true
                }
                else -> false

            }
        }


        //windowInsets(시스템 바) 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}