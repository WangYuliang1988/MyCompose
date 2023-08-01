package com.compose.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class BasicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(color = Color.White) {
            content()
        }
    }
}

@Composable
fun MyScreenContent(names: List<String> = List(1000) {"#$it"}) {
    val counterState = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxHeight()) {
        NameList(names = names, modifier = Modifier.weight(1f))
        Counter(count = counterState.value, updateCount = { newCount -> counterState.value = newCount })
    }
}

@Composable
fun NameList(names: List<String>, modifier: Modifier) {
    val selectedItems = remember { mutableStateListOf<Int>() }

    LazyColumn(modifier = modifier) {
        itemsIndexed(items = names) { index, name ->
            val isSelected = selectedItems.contains(index)
            Greeting(
                name = name,
                isSelected = isSelected,
                onClick = {
                    if (isSelected) {
                        selectedItems.remove(index)
                    } else {
                        selectedItems.add(index)
                    }
                }
            )
            Divider(color = Color.Black)
        }
    }
}

@Composable
fun Greeting(name: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) Color.Red else Color.Transparent)

    Text(text = "Hello $name!",
        modifier = Modifier
            .padding(24.dp)
            .background(color = backgroundColor)
            .clickable(onClick = onClick)
    )
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.End) {
        Button(onClick = { updateCount(count + 1) }) {
            Text(text = "I've been clicked $count times")
        }
    }
}

@Preview
@Composable
fun MyScreenPreview() {
    MyApp {
        MyScreenContent()
    }
}