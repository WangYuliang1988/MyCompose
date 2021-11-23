package com.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

    // private state
    private var currentEditPosition by mutableStateOf(-1)

    // state
    var todoItems = mutableStateListOf<TodoItem>()
        private set

    // state
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    // event
    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    // event
    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        onEditDone() // don't keep the editor open when removing items
    }

    // event
    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todoItems.indexOf(item)
    }

    // event
    fun onEditDone() {
        currentEditPosition = -1
    }

    // event
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }

        todoItems[currentEditPosition] = item
    }
}