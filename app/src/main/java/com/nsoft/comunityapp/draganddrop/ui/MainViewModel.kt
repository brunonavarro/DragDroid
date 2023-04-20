package com.nsoft.comunityapp.draganddrop.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
import com.nsoft.comunityapp.draganddrop.ui.entities.ItemUI
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.RowPosition
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.annotation.PostConstruct


class MainViewModel<T : ItemUI<T>> {


    private val exptionhandle = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error dectetado de en el $throwable ")
    }

    private val lyceviewmodel = CoroutineScope(SupervisorJob() + exptionhandle)

    private fun launch(onProcess: suspend () -> Unit): Job {
        return lyceviewmodel.launch(Dispatchers.IO + NonCancellable) { onProcess() }
    }

    var columnsItems = MutableStateFlow<List<COLUMN>>(listOf())
        private set


    var taskItems = MutableStateFlow<List<T>>(listOf())
        private set

    // Estado para realizar un seguimiento de la tarea actualmente arrastrada
    var draggedTask = MutableStateFlow<T?>(null)
        private set

    var isCurrentlyDragging: Boolean by mutableStateOf(false)
        private set


    init {
        columnsItems.value = listOf(COLUMN.TO_DO, COLUMN.IN_PROGRESS, COLUMN.DEV_DONE)
    }

    fun startDragging(
        item: T,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<COLUMN>
    ) {

        rowPosition.to as Int
        rowPosition.from as Int

        isCurrentlyDragging = true

        draggedTask.value = item


    }

    fun  endDragging(item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<COLUMN>) {
        columnPosition.from

        rowPosition.to as Int
        rowPosition.from as Int

        isCurrentlyDragging = false

        draggedTask.value = null

    }

    fun addPersons(
        item: T,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<COLUMN>
    ) {


        val newItems = mutableListOf<T>()

        taskItems.value.forEachIndexed { index, personUIItem ->
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
        taskItems.value = newItems

    }

    fun onDestroy() {
        lyceviewmodel.cancel("Se termino la vida de uso", Exception("Termino"))
    }

}
