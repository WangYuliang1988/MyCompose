package com.example.crane.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.crane.R
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 2000

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    onTimeOut: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Use the rememberUpdatedState API to guarantee the latest onTimeout function is called.
        val currentOnTimeout by rememberUpdatedState(onTimeOut)

        // To call suspend functions safely from inside a composable, use the LaunchEffect API,
        // which triggers a coroutine-scoped side-effect in Compose.
        //
        // Some side-effect APIs like LaunchedEffect take a variable number of keys as a parameter
        // that are used to restart the effect whenever one of those keys changes. To trigger the
        // side-effect only once during the lifecycle of this composable, use a constant as a key,
        // for example 'LaunchedEffect(true) { ... }'.
        LaunchedEffect(true) {
            delay(SplashWaitTime) // Simulates loading things
            currentOnTimeout()
        }

        Image(
            painter = painterResource(R.drawable.ic_crane_drawer),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    LandingScreen(
        onTimeOut = {}
    )
}