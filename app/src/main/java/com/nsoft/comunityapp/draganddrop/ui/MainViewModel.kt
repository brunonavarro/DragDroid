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

class MainViewModel: ViewModel() {


    var columnsItems = mutableStateListOf<COLUMN>()
        private set

    var taskItems = mutableStateListOf<PersonUIItem>()
        private set


    var isCurrentlyDragging: Boolean by mutableStateOf(false)
        private set


    init {
        columnsItems.add(COLUMN.TO_DO)
        columnsItems.add(COLUMN.IN_PROGRESS)
        columnsItems.add(COLUMN.DEV_DONE)

        taskItems.add(PersonUIItem("Michael","0", Color.Blue, column = COLUMN.TO_DO))
        taskItems.add(PersonUIItem("Larissa","1", Color.Blue, column = COLUMN.TO_DO))
        taskItems.add(PersonUIItem("Bruno","2", Color.Blue, column = COLUMN.TO_DO))

    }

    fun startDragging(rowFromIndex: Int, columnFromIndex: Any?){
        columnFromIndex as COLUMN?
        Log.i("FROM:", "row: $rowFromIndex - column: $columnFromIndex")
        isCurrentlyDragging = true
    }

    fun endDragging(rowFrom: Int, columnFrom: Any?) {
        isCurrentlyDragging = false
    }

    fun addPersons(item: PersonUIItem, rowToIndex: Int, columnToIndex: Any?) {
        columnToIndex as COLUMN?
        Log.i("TO:", "row: $rowToIndex - column: $columnToIndex")
        println("Added Person")
        item.copy().apply {
            column = if (columnToIndex == COLUMN.TO_DO) {
                taskItems.remove(item)
                COLUMN.TO_DO
            } else if (columnToIndex == COLUMN.IN_PROGRESS) {
                taskItems.remove(item)
                COLUMN.IN_PROGRESS
            } else if (columnToIndex == COLUMN.DEV_DONE) {
                taskItems.remove(item)
                COLUMN.DEV_DONE
            } else {
                item.column
            }
            if (item != this) {
                taskItems.add(this)
            }
        }
    }

}