package com.nsoft.comunityapp.draganddrop.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.RowPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.reOrderList

class MainViewModel: ViewModel() {


    var columnsItems = mutableStateListOf<Column>()
        private set

    var taskItems = mutableStateListOf<DragItem>()
        private set

    var cardItem = mutableStateOf<DragItem?>(null)
        private set

    val dropCount = mutableStateOf<Int>(0)

    init {
        columnsItems.add(Column.TO_DO)
        columnsItems.add(Column.IN_PROGRESS)
        columnsItems.add(Column.DEV_DONE)

        taskItems.add(
            DragItem(
                "Michael",
                id = "A1",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(
            DragItem(
                "Larissa",
                id = "B2",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Bruno", "D1", Color.DarkGray))

    }

    fun startDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        taskItems.firstOrNull { it == item }?.apply {
            isDraggable = true
        }

        Log.e("START DRAG", item.name)
    }

    fun endDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        taskItems.firstOrNull { it == item }?.apply {
            isDraggable = false
        }

        Log.e("END DRAG", item.name)
    }

    fun updateTasks(
        item: DragItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<Column>
    ) {
        taskItems.reOrderList(item)?.let {
            it.updateItem(columnPosition)
            it.backgroundColor = when (columnPosition.to) {
                Column.TO_DO -> {
                    Color.DarkGray
                }
                Column.IN_PROGRESS -> {
                    Color.Blue
                }
                Column.DEV_DONE -> {
                    Color.Green
                }
                else -> {
                    item.backgroundColor
                }
            }
        }
    }


    fun updateDroppedList(
        item: DragItem
    ) {
        dropCount.value++
    }


    fun startCardDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        cardItem.value?.apply {
            isDraggable = true
        }

        Log.e("START DRAG", item.name)
    }

    fun endCardDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        cardItem.value?.apply {
            isDraggable = false
        }

        Log.e("END DRAG", item.name)
    }

}