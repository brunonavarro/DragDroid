package com.nsoft.comunityapp.draganddrop

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nsoft.comunityapp.draganddrop.ui.BoardScreen
import com.nsoft.comunityapp.draganddrop.ui.SingleDragDropScreen
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.RowPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.reOrderList

class MainActivity : ComponentActivity() {

    var columnsItems = mutableStateListOf<Column>()
        private set

    var taskItems = mutableStateListOf<DragItem>()
        private set

    val dropCount = mutableStateOf<Int>(0)


    /** Aqui se declara el tipo de data que procesará la libreria*/

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()

        val rowColumnData = RowColumnData(
            columnsItems = columnsItems,
            taskItems = taskItems,
            updateTasks = ::updateTasks,
            startDragging = ::startDragging,
            endDragging = ::endDragging
        )

        val simpleData = SimpleData(
            dropCount = dropCount,
            updateDroppedList = ::updateDroppedList,
            startCardDragging = ::startCardDragging,
            endCardDragging = ::endCardDragging
        )

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    DragAndDropTheme {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(modifier = Modifier.fillMaxWidth(80f),
                                onClick = {
                                    navController.navigate("board")
                                }) {
                                Text(text = "BOAR COLUMN DRAG AND DROP")
                            }
                            Button(modifier = Modifier.fillMaxWidth(80f),
                                onClick = {
                                    navController.navigate("singleDragDrop")
                                }) {
                                Text(text = "SINGLE DRAG AND DROP")
                            }
                        }
                    }
                }
                composable("board") {
                    DragAndDropTheme {
                        BoardScreen(
                            rowColumnData = rowColumnData
                        )
                    }
                }
                composable("singleDragDrop") {
                    DragAndDropTheme {
                        SingleDragDropScreen(
                            simpleData = simpleData
                        )
                    }
                }
            }
        }
    }

    private fun initData() {
        /**Columns Data*/
        columnsItems.add(Column.TO_DO)
        columnsItems.add(Column.IN_PROGRESS)
        columnsItems.add(Column.DEV_DONE)

        /**Task Data*/
        taskItems.add(
            DragItem(
                "Task",
                id = "A1",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(
            DragItem(
                "Task",
                id = "B2",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "D1", Color.DarkGray))
        taskItems.add(
            DragItem(
                "Task",
                id = "B3",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "B4", Color.DarkGray))
        taskItems.add(
            DragItem(
                "Task",
                id = "B5",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "B6", Color.DarkGray))
        taskItems.add(
            DragItem(
                "Task",
                id = "B7",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "B8", Color.DarkGray))
    }

    /**FUNCIONES PARA USAR en
     * @see BoardScreen*/
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


    /**FUNCIONES PARA USAR en
     * @see SimpleDragDropScreen*/
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

        Log.e("START DRAG", item.name)
    }

    fun endCardDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        Log.e("END DRAG", item.name)
    }

}

data class RowColumnData(
    val columnsItems: List<Column>,
    val taskItems: List<DragItem>,
    val updateTasks: (DragItem, RowPosition, ColumnPosition<Column>) -> Unit,
    val startDragging: (DragItem, RowPosition, ColumnPosition<Column>) -> Unit,
    val endDragging: (DragItem, RowPosition, ColumnPosition<Column>) -> Unit
)

data class SimpleData(
    val dropCount: MutableState<Int>,
    val updateDroppedList: (DragItem) -> Unit,
    val startCardDragging: (DragItem) -> Unit,
    val endCardDragging: (DragItem) -> Unit
)

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DragAndDropTheme {
        Greeting("Android")
    }
}