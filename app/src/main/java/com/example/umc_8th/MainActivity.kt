package com.example.umc_8th

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.umc_8th.databinding.ActivitiyMainBinding
import com.example.umc_8th.ui.theme.UMC_8thTheme

class MainActivity : AppCompatActivity() { //xml버전으로 하였으므로 Apcompactivitiy로 바꿔줌

    private lateinit var mBinding : ActivitiyMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitiyMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션을 컨트롤러
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 묶어줌
        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)
    }

}
