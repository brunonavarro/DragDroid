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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.annotation.PostConstruct

class MainViewModel {


    private val exptionhandle = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error dectetado de en el $throwable ")
    }

    private val lyceviewmodel = CoroutineScope(SupervisorJob() + exptionhandle)

    private fun launch(onProcess: suspend () -> Unit): Job {
        return lyceviewmodel.launch(Dispatchers.IO + NonCancellable) { onProcess() }
    }
    var _columnsItems = MutableStateFlow<List<COLUMN>>(listOf())



    var columnsItems = mutableStateListOf<COLUMN>()
        private set

    var taskItems = mutableStateListOf<PersonUIItem>()
        private set

    // Estado para realizar un seguimiento de la tarea actualmente arrastrada
    var draggedTask = mutableStateOf<PersonUIItem?>(null)
        private set

    var isCurrentlyDragging: Boolean by mutableStateOf(false)
        private set


    @PostConstruct
    fun init() {
        columnsItems.add(COLUMN.TO_DO)
        columnsItems.add(COLUMN.IN_PROGRESS)
        columnsItems.add(COLUMN.DEV_DONE)

        taskItems.add(
            PersonUIItem(
                "Michael",
                id = 0,
                backgroundColor = Color.DarkGray,
                column = COLUMN.TO_DO
            )
        )
        taskItems.add(
            PersonUIItem(
                "Larissa",
                id = 1,
                backgroundColor = Color.DarkGray,
                column = COLUMN.TO_DO
            )
        )
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

        Log.e("START DRAG", item.name)
    }

    fun endDragging(item: PersonUIItem, rowPosition: RowPosition, columnPosition: ColumnPosition) {
        columnPosition.from as COLUMN

        rowPosition.to as Int
        rowPosition.from as Int

        isCurrentlyDragging = false

        draggedTask.value = null

        Log.e("END DRAG", item.name)
    }

    fun addPersons(
        item: PersonUIItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition
    ) {
        columnPosition.to as COLUMN
        columnPosition.from as COLUMN

        val newItems = mutableListOf<PersonUIItem>()

        taskItems.forEachIndexed { index, personUIItem ->
            if (
                personUIItem == item
            ) {
                Log.e("ADD personUIItem: ", personUIItem.toString())
                Log.e("ADD item: ", item.toString())
                Log.e("ADD : ", "----------------------------------")
                personUIItem.updateItem(item, index, columnPosition, rowPosition)
                personUIItem.backgroundColor = when (columnPosition.to) {
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
            }
            newItems.add(index, personUIItem)
        }
        Log.e("ADD NEW LIST: ", newItems.toString())
        Log.e("ADD : ", "----------------------------------")
        taskItems.clear()
        taskItems.addAll(newItems)
    }

    fun onDestroy() {
        lyceviewmodel.cancel("Se termino la vida de uso", Exception("Termino"))
    }

}