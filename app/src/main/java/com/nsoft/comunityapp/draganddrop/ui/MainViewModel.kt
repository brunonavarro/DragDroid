package com.nsoft.comunityapp.draganddrop.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.dragdroid_kt.components.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.components.RowPosition

class MainViewModel: ViewModel() {


    var columnsItems = mutableStateListOf<Column>()
        private set

    var taskItems = mutableStateListOf<DragItem>()
        private set

    // Estado para realizar un seguimiento de la tarea actualmente arrastrada
    var draggedTask = mutableStateOf<DragItem?>(null)
        private set

    var isCurrentlyDragging = mutableStateOf(false)
        private set


    init {
        columnsItems.add(Column.TO_DO)
        columnsItems.add(Column.IN_PROGRESS)
        columnsItems.add(Column.DEV_DONE)

        taskItems.add(
            DragItem(
                "Michael",
                id = 0,
                backgroundColor = Color.DarkGray,
//                column = _root_ide_package_.com.nsoft.comunityapp.draganddrop.ui.entities.Column.TO_DO
            )
        )
        taskItems.add(
            DragItem(
                "Larissa",
                id = 1,
                backgroundColor = Color.DarkGray,
//                column = _root_ide_package_.com.nsoft.comunityapp.draganddrop.ui.entities.Column.TO_DO
            )
        )
        taskItems.add(DragItem("Bruno", 2, Color.DarkGray, column = Column.TO_DO))

    }

    fun startDragging(
        item: DragItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<Column>
    ) {
        columnPosition.from

        rowPosition.to
        rowPosition.from

        taskItems.firstOrNull { it == item }?.apply {
            isDraggable = true
        }

        isCurrentlyDragging.value = true

        draggedTask.value = item

        Log.e("START DRAG", item.name)
    }

    fun endDragging(
        item: DragItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<Column>
    ) {
        columnPosition.from

        rowPosition.to
        rowPosition.from

        taskItems.firstOrNull { it == item }?.apply {
            isDraggable = false
        }

        isCurrentlyDragging.value = false

        draggedTask.value = null

        Log.e("END DRAG", item.name)
    }

    fun addPersons(
        item: DragItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<Column>
    ) {
        columnPosition.to
        columnPosition.from

        val newItems = mutableListOf<DragItem>()

        taskItems.forEachIndexed { index, personUIItem ->
            if (
                personUIItem == item
            ) {
                Log.e("ADD personUIItem: ", personUIItem.toString())
                Log.e("ADD item: ", item.toString())
                Log.e("ADD : ", "----------------------------------")
                personUIItem.updateItem(item, index, columnPosition, rowPosition)
                personUIItem.backgroundColor = when (columnPosition.to) {
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
            newItems.add(index, personUIItem)
        }
        Log.e("ADD NEW LIST: ", newItems.toString())
        Log.e("ADD : ", "----------------------------------")
        taskItems.clear()
        taskItems.addAll(newItems)
    }

}