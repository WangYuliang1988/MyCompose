package com.example.crane.ui.home

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.crane.ui.components.*
import com.example.crane.util.CraneScreen
import com.example.crane.vm.MainViewModel
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch

@Composable
fun CraneHome(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onExploreItemClicked: OnExploreItemClicked
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        drawerContent = {
            CraneDrawer()
        }
    ) {
        // Use the rememberCoroutineScope API to get a CoroutineScope that follows the lifecycle
        // of its call-site. With that scope, you can start coroutines when you're not in the
        // Composition. The scope will be automatically canceled once it leaves the Composition.
        val scope = rememberCoroutineScope()
        CraneHomeContent(
            modifier = modifier,
            viewModel = viewModel,
            onExploreItemClicked = onExploreItemClicked,
            openDrawer = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CraneHomeContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onExploreItemClicked: OnExploreItemClicked,
    openDrawer: () -> Unit
) {
    // When used in a composable function, collectAsState() collects values from the StateFlow and
    // represents the latest value via Compose's State API. This will make the Compose code that
    // reads that state value recompose on new emissions.
    val suggestedDestinations by viewModel.suggestedDestinations.collectAsState()

    val onPeopleChanged: (Int) -> Unit = { viewModel.updatePeople(it) }
    var tabSelected by remember { mutableStateOf(CraneScreen.Fly) }

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,
        appBar = {
            HomeTabBar(
                tabSelected = tabSelected,
                onTabSelected = { tabSelected = it },
                openDrawer = openDrawer
            )
        },
        backLayerContent = {
            SearchContent(
                tabSelected = tabSelected,
                viewModel = viewModel,
                onPeopleChanged = onPeopleChanged
            )
        },
        frontLayerContent = {
            when (tabSelected) {
                CraneScreen.Fly -> {
                    ExploreSection(
                        title = "Explore Flights by Destination",
                        exploreList = suggestedDestinations,
                        onItemClicked = onExploreItemClicked
                    )
                }
                CraneScreen.Sleep -> {
                    ExploreSection(
                        title = "Explore Properties by Destination",
                        exploreList = viewModel.hotels,
                        onItemClicked = onExploreItemClicked
                    )
                }
                CraneScreen.Eat -> {
                    ExploreSection(
                        title = "Explore Restaurants by Destination",
                        exploreList = viewModel.restaurants,
                        onItemClicked = onExploreItemClicked
                    )
                }
            }
        }
    )
}

@Composable
private fun HomeTabBar(
    modifier: Modifier = Modifier,
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit,
    openDrawer: () -> Unit
) {
    CraneTabBar(
        modifier = modifier,
        onMenuClicked = openDrawer
    ) { tabBarModifier ->
        CraneTabs(
            modifier = tabBarModifier,
            titles = CraneScreen.values().map { it.name },
            tabSelected = tabSelected,
            onTabSelected = { newTab ->
                onTabSelected(CraneScreen.values()[newTab.ordinal])
            }
        )
    }
}

@Composable
private fun SearchContent(
    tabSelected: CraneScreen,
    viewModel: MainViewModel,
    onPeopleChanged: (Int) -> Unit
) {
    when (tabSelected) {
        CraneScreen.Fly -> FlySearchContent(
            onPeopleChanged = onPeopleChanged,
            onToDestinationChanged = { viewModel.toDestinationChanged(it) }
        )
        CraneScreen.Sleep -> SleepSearchContent(
            onPeopleChanged = onPeopleChanged
        )
        CraneScreen.Eat -> EatSearchContent(
            onPeopleChanged = onPeopleChanged
        )
    }
}