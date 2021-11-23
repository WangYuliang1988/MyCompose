package com.example.crane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.crane.data.DestinationsLocalDataSource
import com.example.crane.data.DestinationsRepository
import com.example.crane.ui.components.OnExploreItemClicked
import com.example.crane.ui.home.CraneHome
import com.example.crane.ui.home.LandingScreen
import com.example.crane.ui.theme.CraneTheme
import com.example.crane.util.ProvideImageLoader
import com.example.crane.vm.MainViewModel
import com.example.crane.vm.MainViewModelFactory
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.Dispatchers

class CraneActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val repository = DestinationsRepository(DestinationsLocalDataSource)
        val factory = MainViewModelFactory(repository, Dispatchers.Default)
        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setContent {
            ProvideWindowInsets {
                ProvideImageLoader {
                    CraneTheme {
                        MainScreen(
                            viewModel = viewModel,
                            onExploreItemClicked = {
                                launchDetailsActivity(context = this, item = it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    viewModel: MainViewModel,
    onExploreItemClicked: OnExploreItemClicked
) {
    Surface(color = MaterialTheme.colors.primary) {
        var showLandingScreen by remember { mutableStateOf(true) }
        if (showLandingScreen) {
            LandingScreen(
                onTimeOut = {
                    showLandingScreen = false
                }
            )
        } else {
            CraneHome(
                viewModel = viewModel,
                onExploreItemClicked = onExploreItemClicked
            )
        }
    }
}