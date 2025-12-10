package com.adrino.getin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.adrino.getin.navigation.GetInNavigation
import com.adrino.getin.ui.theme.GetInTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetInTheme {
                val navController = rememberNavController()
                GetInNavigation(navController)
            }
        }
    }
}
