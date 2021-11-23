package com.example.crane

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.crane.data.DestinationsLocalDataSource
import com.example.crane.data.DestinationsRepository
import com.example.crane.data.ExploreModel
import com.example.crane.ui.theme.CraneTheme
import com.example.crane.util.ProvideImageLoader
import com.example.crane.util.Result
import com.example.crane.vm.DetailViewModelFactory
import com.example.crane.vm.DetailsViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.delay

const val KEY_ARG_DETAILS_CITY_NAME = "KEY_ARG_DETAILS_CITY_NAME"

fun launchDetailsActivity(context: Context, item: ExploreModel) {
    val intent = Intent(context, DetailsActivity::class.java)
    intent.putExtra(KEY_ARG_DETAILS_CITY_NAME, item.city.name)
    context.startActivity(intent)
}

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val repository = DestinationsRepository(DestinationsLocalDataSource)
        // Notice the 'intent.extras' parameter here, we use it to pass Activity Intent data to ViewModel
        val factory = DetailViewModelFactory(repository, this, intent.extras)
        val viewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)

        setContent {
            ProvideWindowInsets {
                ProvideImageLoader {
                    CraneTheme {
                        Surface {
                            DetailScreen(
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .navigationBarsPadding(),
                                viewModel = viewModel,
                                onErrorLoading = { finish() }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DetailsUiState(
    val cityDetails: ExploreModel? = null,
    val isLoading: Boolean = false,
    val throwError: Boolean = false
)

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel,
    onErrorLoading: () -> Unit
) {
    val uiState by produceState(initialValue = DetailsUiState(isLoading = true)) {
        delay(1500) // Simulates loading things

        // In a coroutine, this can call suspend functions or move the computation
        // to different Dispatchers.
        val cityDetailResult = viewModel.cityDetails
        value = if (cityDetailResult is Result.Success<ExploreModel>) {
            DetailsUiState(cityDetails = cityDetailResult.data)
        } else {
            DetailsUiState(throwError = true)
        }
    }

    when {
        uiState.cityDetails != null -> {
            DetailContent(modifier = modifier.fillMaxSize(), exploreModel = uiState.cityDetails!!)
        }
        uiState.isLoading -> {
            Box(modifier.fillMaxSize()) {
                Text(text = "Loading...", modifier = modifier.align(Alignment.Center))
            }
        }
        else -> {
            onErrorLoading()
        }
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    exploreModel: ExploreModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = exploreModel.city.nameToDisplay,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = exploreModel.description,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
    }
}