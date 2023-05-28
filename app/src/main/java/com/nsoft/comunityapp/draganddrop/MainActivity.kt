package com.nsoft.comunityapp.draganddrop

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nsoft.comunityapp.draganddrop.ui.CustomDragCard
import com.nsoft.comunityapp.draganddrop.ui.CustomHeaderColumn
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme
import com.nsoft.comunityapp.dragdroid_kt.components.ColumnCard
import com.nsoft.comunityapp.dragdroid_kt.components.DragDropScreen
import com.nsoft.comunityapp.dragdroid_kt.components.DraggableScreen
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    /** Aqui se declara el tipo de data que procesará la libreria*/

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val rowListByGroup = mainViewModel.taskItems.groupBy { it.column }

            var columnStyleParams by remember {
                mutableStateOf<ColumnParameters.StyleParams<DragItem, Column>?>(null)
            }
            DragAndDropTheme {
                // A surface container using the 'background' color from the theme
                DraggableScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(0.8f))
                ) {

                    Column() {

                        /*val columnList = mainViewModel.columnsItems.groupBy { it }

                        LazyRow(Modifier.fillMaxWidth()){
                            items(
                                columnList.keys.toList(),
                                key = { it }
                            ) { column ->
                                val rowList = rowListByGroup[column] ?: emptyList()

                                DropItemMain<DragItem, Column>(
                                    modifier = Modifier.fillMaxWidth(),
                                    rowIndex = rowListByGroup.size,
                                    columnIndex = column
                                ) { isInBound, data, rows, column2, isDrag ->
                                    LaunchedEffect(
                                        key1 = data != null,
                                        key2 = isInBound && !isDrag && data?.column != column && column2.canAdd()
                                    ) {
                                        if (isInBound && !isDrag && data?.column != column && column2.canAdd() && data != null) {
                                            mainViewModel.addPersons(
                                                data,
                                                rows,
                                                column2
                                            )
                                        }
                                    }

                                    if (isInBound && isDrag) {
                                        if (rowList.isNullOrEmpty()){
                                            Box(
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .background(Color.Magenta.copy(0.5F))
                                            )

                                        }else {
                                            LazyColumn {
                                                items(
                                                    rowList ?: listOf(),
                                                    key = {
                                                        it.rowPosition.from as Any
                                                    }
                                                ) { d ->

                                                    DragTarget<DragItem, Column>(
                                                        rowIndex = 0,
                                                        columnIndex = column,
                                                        dataToDrop = d as DragItem,
                                                        vibrator = null,
                                                        onStart = { item, row, column2 ->
                                                            mainViewModel.startDragging(
                                                                item,
                                                                rowPosition = row,
                                                                columnPosition = column2
                                                            )
                                                        },
                                                        onEnd = { item, row, column2 ->
                                                            mainViewModel.endDragging(
                                                                item,
                                                                rowPosition = row,
                                                                columnPosition = column2
                                                            )
                                                        }
                                                    ) { isDrag, data ->
                                                        Box(
                                                            modifier = Modifier
                                                                .size(200.dp)
                                                                .background(Color.Magenta.copy(0.5F))
                                                        ) {
                                                            Text(
                                                                text = "DRAG HERE!! - ${d.name}",
                                                                modifier = Modifier.align(Alignment.Center)
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        if (rowList.isNullOrEmpty()){
                                            Box(
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .background(Color.White.copy(0.5F))
                                            )
                                        }else {
                                            LazyColumn {
                                                items(
                                                    rowList ?: listOf(),
                                                    key = {
                                                        it.rowPosition.from as Any
                                                    }
                                                ) { d ->
                                                    DragTarget<DragItem, Column>(
                                                        rowIndex = 0,
                                                        columnIndex = column,
                                                        dataToDrop = d,
                                                        vibrator = null,
                                                        onStart = { item, row, column2 ->
                                                            mainViewModel.startDragging(
                                                                item,
                                                                rowPosition = row,
                                                                columnPosition = column2
                                                            )
                                                        },
                                                        onEnd = { item, row, column2 ->
                                                            mainViewModel.endDragging(
                                                                item,
                                                                rowPosition = row,
                                                                columnPosition = column2
                                                            )
                                                        }
                                                    ) { isDrag, data ->

                                                        Box(
                                                            modifier = Modifier
                                                                .size(200.dp)
                                                                .background(Color.Blue.copy(0.5F))
                                                        ) {
                                                            Text(
                                                                text = "DRAG HERE!! - ${d.name}",
                                                                modifier = Modifier.align(Alignment.Center)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }*/

                        DragDropScreen(
                            context = applicationContext,
                            columnsItems = mainViewModel.columnsItems,
                            rowListByGroup = rowListByGroup,
                            updateBoard = { item, row, column ->
                                mainViewModel.addPersons(item, row, column)
                            },
                            callBackColumn = {
                                columnStyleParams = it
                            }
                        ) {
                            columnStyleParams?.let {
                                ColumnCard(
                                    params = it,
                                    header = {
                                        CustomHeaderColumn(
                                            params = it
                                        )
                                    },
                                    key = { item ->
                                        item.rowPosition.from as Any
                                    },
                                    body = { data ->
                                        CustomDragCard(
                                            data = data, styleParams = it,
                                            actionParams = ColumnParameters.ActionParams(
                                                onStart = { item, row, column ->
                                                    mainViewModel.startDragging(
                                                        item,
                                                        rowPosition = row,
                                                        columnPosition = column
                                                    )
                                                },
                                                onEnd = { item, row, column ->
                                                    mainViewModel.endDragging(
                                                        item,
                                                        rowPosition = row,
                                                        columnPosition = column
                                                    )
                                                }
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

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