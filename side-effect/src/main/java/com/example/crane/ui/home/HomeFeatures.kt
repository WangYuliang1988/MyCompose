package com.example.crane.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crane.R
import com.example.crane.ui.components.*
import com.example.crane.ui.theme.CraneTheme

@Composable
fun FlySearchContent(
    onPeopleChanged: (Int) -> Unit,
    onToDestinationChanged: (String) -> Unit
) {
    CraneSearch {
        PeopleUserInput(
            titleSuffix = ", Economy",
            onPeopleChanged = onPeopleChanged
        )
        Spacer(Modifier.height(8.dp))
        FromDestination()
        Spacer(Modifier.height(8.dp))
        ToDestinationUserInput(onToDestinationChanged)
        Spacer(Modifier.height(8.dp))
        DatesUserInput()
    }
}

@Composable
fun SleepSearchContent(onPeopleChanged: (Int) -> Unit) {
    CraneSearch {
        PeopleUserInput(onPeopleChanged = onPeopleChanged)
        Spacer(Modifier.height(8.dp))
        DatesUserInput()
        Spacer(Modifier.height(8.dp))
        SimpleUserInput(caption = "Select Location", vectorImageId = R.drawable.ic_hotel)
    }
}

@Composable
fun EatSearchContent(onPeopleChanged: (Int) -> Unit) {
    CraneSearch {
        PeopleUserInput(onPeopleChanged = onPeopleChanged)
        Spacer(Modifier.height(8.dp))
        DatesUserInput()
        Spacer(Modifier.height(8.dp))
        SimpleUserInput(caption = "Select Time", vectorImageId = R.drawable.ic_time)
        Spacer(Modifier.height(8.dp))
        SimpleUserInput(caption = "Select Location", vectorImageId = R.drawable.ic_restaurant)
    }
}

@Composable
private fun CraneSearch(content: @Composable () -> Unit) {
    Column(Modifier.padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 12.dp)) {
        content()
    }
}

@Preview
@Composable
private fun FlySearchContentPreview() {
    CraneTheme {
        FlySearchContent(
            onPeopleChanged = {},
            onToDestinationChanged = {}
        )
    }
}

@Preview
@Composable
private fun SleepSearchContentPreview() {
    CraneTheme {
        SleepSearchContent(
            onPeopleChanged = {}
        )
    }
}

@Preview
@Composable
private fun EatSearchContentPreview() {
    CraneTheme {
        EatSearchContent(
            onPeopleChanged = {}
        )
    }
}