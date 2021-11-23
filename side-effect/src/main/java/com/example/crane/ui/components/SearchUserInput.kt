package com.example.crane.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.crane.R
import com.example.crane.ui.theme.CraneTheme
import com.example.crane.vm.MAX_PEOPLE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

enum class PeopleUserInputAnimationState {
    Valid,
    Invalid
}

class PeopleUserInputState {
    var people by mutableStateOf(1)
        private set

    val animationState: MutableTransitionState<PeopleUserInputAnimationState> =
        MutableTransitionState(PeopleUserInputAnimationState.Valid)

    fun addPerson() {
        people = (people % (MAX_PEOPLE + 1)) + 1
        updateAnimationState()
    }

    private fun updateAnimationState() {
        val newState =
            if (people > MAX_PEOPLE) PeopleUserInputAnimationState.Invalid
            else PeopleUserInputAnimationState.Valid

        if (animationState.currentState != newState) animationState.targetState = newState
    }
}

@Composable
fun PeopleUserInput(
    titleSuffix: String? = "",
    onPeopleChanged: (Int) -> Unit,
    peopleState: PeopleUserInputState = remember { PeopleUserInputState() }
) {
    Column {
        val transitionState = remember { peopleState.animationState }
        val tint = tintPeopleUserInput(transitionState)

        val people = peopleState.people
        val text = if (people == 1) "$people Adult$titleSuffix" else "$people Adults$titleSuffix"
        CraneUserInput(
            text = text,
            vectorImageId = R.drawable.ic_person,
            tint = tint.value,
            onClick = {
                peopleState.addPerson()
                onPeopleChanged(peopleState.people)
            }
        )
        if (transitionState.targetState == PeopleUserInputAnimationState.Invalid) {
            Text(
                text = "Error: We don't support more than $MAX_PEOPLE people",
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary)
            )
        }
    }
}

@Composable
fun FromDestination() {
    CraneUserInput(text = "Seoul, South Korea", vectorImageId = R.drawable.ic_location)
}

@Composable
fun ToDestinationUserInput(onToDestinationChanged: (String) -> Unit) {
    val editableUserInputState = rememberEditableUserInputState(hint = "Choose Destination")
    CraneEditableUserInput(
        state = editableUserInputState,
        caption = "To",
        vectorImageId = R.drawable.ic_plane
    )

    val currentOnDestinationChanged by rememberUpdatedState(onToDestinationChanged)
    LaunchedEffect(editableUserInputState) {
        // We use the snapshotFlow API to convert Compose State<T> objects into a Flow. When the
        // state read inside snapshotFlow mutates, the Flow will emit the new value to the collector.
        snapshotFlow {
            editableUserInputState.text
        }.filter {
            !editableUserInputState.isHint
        }.collect {
            currentOnDestinationChanged(editableUserInputState.text)
        }
    }
}

@Composable
fun DatesUserInput() {
    CraneUserInput(
        text = "",
        caption = "Select Dates",
        vectorImageId = R.drawable.ic_calendar
    )
}

@Composable
private fun tintPeopleUserInput(
    transitionState: MutableTransitionState<PeopleUserInputAnimationState>
): State<Color> {
    val validColor = MaterialTheme.colors.onSurface
    val invalidColor = MaterialTheme.colors.secondary

    val transition = updateTransition(transitionState, label = "Tint transition")
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Tint animate color"
    ) {
        if (it == PeopleUserInputAnimationState.Valid) validColor else invalidColor
    }
}

@Preview
@Composable
private fun PeopleUserInputPreview() {
    CraneTheme {
        PeopleUserInput(onPeopleChanged = {})
    }
}
