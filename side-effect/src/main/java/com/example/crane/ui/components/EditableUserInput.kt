package com.example.crane.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.example.crane.R
import com.example.crane.ui.theme.CaptionTextStyle

// By creating a state holder responsible for the internal state of a composable, we can centralize
// all state changes in one place. With this, it's more difficult for the state to be out of sync,
// and the related logic is all grouped in a single class. Furthermore, the state can be easily
// hoisted up and can be consumed from callers of the composable.
class EditableUserInputState(
    private val hint: String,
    initialText: String
) {
    var text by mutableStateOf(initialText)

    val isHint: Boolean
        get() = text == hint

    companion object {
        val Saver: Saver<EditableUserInputState, *> = listSaver(
            save = {
                listOf(it.hint, it.text)
            },
            restore = {
                EditableUserInputState(
                    hint = it[0],
                    initialText = it[1]
                )
            }
        )
    }
}

// State holder always need to be remembered in order to keep them in the Composition and not create
// a new one every time. If we only remember the state by using the 'remember' API, it won't survive
// activity recreations. To achieve that, we can use the 'rememberSaveable' API.
//
// 'rememberSaveable' dose all this with no extra work for objects that can be stored inside a
// 'Bundle', but for other objects, such as the 'EditableUserInputState' we created above, we need
// to tell 'rememberSaveable' how to save and restore an instance of this class using a 'Saver'.
//
// A Saver describes how an object can be converted into something which is Saveable. It's a good
// practice to place Saver definitions close to the class they work with (for our case, in the
// EditableUserInputState class, and because it needs to be statically accessed, we add it in a
// 'companion object').
@Composable
fun rememberEditableUserInputState(hint: String): EditableUserInputState =
    rememberSaveable(hint, saver = EditableUserInputState.Saver) {
        EditableUserInputState(hint, hint)
    }

@Composable
fun CraneEditableUserInput(
    state: EditableUserInputState = rememberEditableUserInputState(""),
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null
) {
    CraneBaseUserInput(
        caption = caption,
        tintIcon = { !state.isHint },
        showCation = { !state.isHint },
        vectorImageId = vectorImageId
    ) {
        BasicTextField(
            value = state.text,
            onValueChange = { input ->
                state.text = input
            },
            textStyle = if (state.isHint) {
                CaptionTextStyle.copy(color = LocalContentColor.current)
            } else {
                MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
            },
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}

@Preview
@Composable
private fun CraneEditableUserInputPreview() {
    CraneEditableUserInput(
        state = rememberEditableUserInputState("Hint"),
        caption = null,
        vectorImageId = R.drawable.ic_crane_logo,
    )
}