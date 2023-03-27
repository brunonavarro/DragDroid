package com.nsoft.comunityapp.draganddrop.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
import com.nsoft.comunityapp.draganddrop.ui.entities.PersonUIItem
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.RowPosition

class MainViewModel: ViewModel() {


    var columnsItems = mutableStateListOf<COLUMN>()
        private set

    var taskItems = mutableStateListOf<PersonUIItem>()
        private set

    // Estado para realizar un seguimiento de la tarea actualmente arrastrada
    var draggedTask = mutableStateOf<PersonUIItem?>(null)
        private set

    var isCurrentlyDragging: Boolean by mutableStateOf(false)
        private set


    init {
        columnsItems.add(COLUMN.TO_DO)
        columnsItems.add(COLUMN.IN_PROGRESS)
        columnsItems.add(COLUMN.DEV_DONE)

        taskItems.add(PersonUIItem("Michael", "0", Color.DarkGray, column = COLUMN.TO_DO))
        taskItems.add(PersonUIItem("Larissa", "1", Color.DarkGray, column = COLUMN.TO_DO))
//        taskItems.add(PersonUIItem("Bruno","2", Color.DarkGray, column = COLUMN.TO_DO))

    }

    fun startDragging(
        item: PersonUIItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition
    ) {
        columnPosition.from as COLUMN

        rowPosition.to as Int
        rowPosition.from as Int

        isCurrentlyDragging = true

        draggedTask.value = item

        Log.i("MainVM STAR MOVE FROM: ", "row $rowPosition - column $columnPosition")
    }

    fun endDragging(item: PersonUIItem, rowPosition: RowPosition, columnPosition: ColumnPosition) {
        columnPosition.from as COLUMN

        rowPosition.to as Int
        rowPosition.from as Int

        isCurrentlyDragging = false

        draggedTask.value = null

        Log.i("MainVM END MOVE TO: ", "row $rowPosition - column $columnPosition")
    }

    fun addPersons(item: PersonUIItem, rowPosition: RowPosition, columnPosition: ColumnPosition) {
        columnPosition.to as COLUMN
        columnPosition.from as COLUMN

        if (item.canAdd() && columnPosition.canAdd()) {
            taskItems.remove(item)

            item.column = columnPosition.to
            item.columnPosition = columnPosition
            item.rowPosition = rowPosition
            item.backgroundColor = when (columnPosition.to) {
                COLUMN.TO_DO -> {
                    Color.DarkGray
                }
                COLUMN.IN_PROGRESS -> {
                    Color.Blue
                }
                COLUMN.DEV_DONE -> {
                    Color.Green
                }
                else -> {
                    item.backgroundColor
                }
            }

            Log.e(
                "ADD",
                "item ${item.id} - ${item.column} - ${item.isDraggable} - ${item.rowPosition} - ${item.columnPosition}"
            )
            Log.e("ADD", "row $rowPosition")
            Log.e("ADD", "column $columnPosition")
            Log.e("ADD", "---------------------------------------------------------")

            taskItems.add(item)
        }
    }

}