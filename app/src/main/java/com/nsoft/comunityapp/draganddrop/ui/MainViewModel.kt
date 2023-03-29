package com.nsoft.comunityapp.draganddrop.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        Log.i("MainVM END MOVE TO: ", "---------------------------------------------------")
    }

    fun addPersons(
        item: PersonUIItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition,
        index: Int
    ) {
        columnPosition.to as COLUMN
        columnPosition.from as COLUMN

        if (item.canAdd() && columnPosition.canAdd()) {
            Log.e("ADD $columnPosition VM", "before taskItems ${taskItems.toList().toString()}")
            taskItems.removeAt(index)
            item.column = columnPosition.to as COLUMN
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
                "ADD ${columnPosition.to} VM",
                "---------------------------------------------------------"
            )
            Log.e(
                "ADD ${columnPosition.to} VM",
                "item ${item.id} - ${item.column} - ${item.isDraggable} - ${item.rowPosition} - ${item.columnPosition}"
            )
            Log.e("ADD ${columnPosition.to} VM", "row $rowPosition")
            Log.e("ADD ${columnPosition.to} VM", "column $columnPosition")
            Log.e(
                "ADD ${columnPosition.to} VM",
                "---------------------------------------------------------"
            )

            taskItems.add(index, item)
            Log.e("ADD $columnPosition VM", "after taskItems ${taskItems.toList().toString()}")
            Log.e(
                "ADD $columnPosition VM",
                "after taskItems -----------------------------------------------------"
            )
        }
    }

}