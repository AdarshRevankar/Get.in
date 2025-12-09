package com.adrino.getin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.adrino.getin.data.remote.RetrofitClient
import com.adrino.getin.data.repository.EventRepository
import com.adrino.getin.navigation.GetInNavigation
import com.adrino.getin.ui.theme.GetInTheme
import com.adrino.getin.ui.viewmodel.EventViewModelFactory

class HomeActivity : ComponentActivity() {

    private val repository = EventRepository(RetrofitClient.apiService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetInTheme {
                val navController = rememberNavController()
                val eventViewModelFactory = EventViewModelFactory(repository)
                GetInNavigation(navController, repository, eventViewModelFactory)
            }
        }
    }
}
