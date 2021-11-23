package com.example.crane.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.crane.R
import com.example.crane.data.City
import com.example.crane.data.ExploreModel
import com.example.crane.ui.theme.BottomSheetShape
import com.example.crane.ui.theme.CraneCaption
import com.example.crane.ui.theme.CraneDividerColor
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch

typealias OnExploreItemClicked = (ExploreModel) -> Unit

@Composable
fun ExploreSection(
    title: String,
    exploreList: List<ExploreModel>,
    onItemClicked: OnExploreItemClicked
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
        shape = BottomSheetShape
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, top = 20.dp, end = 24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption.copy(color = CraneCaption)
            )
            Spacer(Modifier.height(8.dp))

            Box(Modifier.weight(1.0f)) {
                val listState = rememberLazyListState()
                ExploreList(
                    exploreList = exploreList,
                    onItemClicked = onItemClicked,
                    listState = listState
                )

                // Show the button if the first visible item is off the screen, we use a remembered
                // derived state to minimize unnecessary compositions.
                val showButton by remember {
                    // derivedStateOf API is used when you want a Compose State that's derived from
                    // another State. Using this function guarantees that the calculation will only
                    // occur whenever one of the states used in the calculation changed.
                    derivedStateOf {
                        listState.firstVisibleItemIndex > 0
                    }
                }
                if (showButton) {
                    val coroutineScope = rememberCoroutineScope()
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .navigationBarsPadding()
                            .padding(bottom = 8.dp),
                        onClick = {
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        }
                    ) {
                        Text("Up!")
                    }
                }
            }
        }
    }
}

@Composable
private fun ExploreList(
    exploreList: List<ExploreModel>,
    onItemClicked: OnExploreItemClicked,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(exploreList) { exploreItem ->
            Column(Modifier.fillParentMaxWidth()) {
                ExploreItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    item = exploreItem,
                    onItemClicked = onItemClicked
                )
                Divider(color = CraneDividerColor)
            }
        }
        item {
            Spacer(modifier = Modifier.navigationBarsHeight())
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ExploreItem(
    item: ExploreModel,
    onItemClicked: OnExploreItemClicked,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(item) }
            .padding(top = 12.dp, bottom = 12.dp)
    ) {
        ExploreImageContainer {
            Box {
                val painter = rememberImagePainter(
                    data = item.imageUrl,
                    builder = { crossfade(true) }
                )
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                if (painter.state is ImagePainter.State.Loading) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_crane_logo),
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(Modifier.width(24.dp))
        Column {
            Text(
                text = item.city.nameToDisplay,
                style = MaterialTheme.typography.h6
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.caption.copy(color = CraneCaption)
            )
        }
    }
}

@Composable
private fun ExploreImageContainer(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.size(width = 60.dp, height = 60.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        content()
    }
}

@Preview
@Composable
fun ExploreSectionPreview() {
    ExploreSection(
        title = "ExploreSection",
        exploreList = listOf(
            ExploreModel(
                city = City("city", "country", "0", "0"),
                description = "description",
                imageUrl = ""
            ),
            ExploreModel(
                city = City("city", "country", "0", "0"),
                description = "description",
                imageUrl = ""
            ),
            ExploreModel(
                city = City("city", "country", "0", "0"),
                description = "description",
                imageUrl = ""
            )
        ),
        onItemClicked = {}
    )
}