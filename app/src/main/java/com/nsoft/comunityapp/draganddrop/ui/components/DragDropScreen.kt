package com.nsoft.comunityapp.draganddrop.ui.components

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
import com.nsoft.comunityapp.draganddrop.ui.entities.PersonUIItem
import com.nsoft.comunityapp.draganddrop.ui.library.DragTarget
import com.nsoft.comunityapp.draganddrop.ui.library.DropItem


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun DragDropScreen(
    context: Context,
    mainViewModel: MainViewModel
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    val screenHeight = LocalConfiguration.current.screenHeightDp

    // Agrupar las tareas por estado
    val columnByStatus = mainViewModel.columnsItems.groupBy { it }
    val tasksByStatus = mainViewModel.taskItems.groupBy { it.column }

    Log.e("COLUMN LIST: ", "map list $columnByStatus")
    Log.e("TASK LIST: ", "map list $tasksByStatus")
    // Crear un LazyColumn para representar las columnas de estado
    LazyRow(Modifier.fillMaxWidth()) {
        items(columnByStatus.keys.toList(), key = { it }) { column ->
            val tasksInStatus = tasksByStatus[column] ?: emptyList()
            Log.e("TASK LIST: ", "list $tasksInStatus")
            // Crear un LazyColumn para representar las filas de tarjetas de tarea
            DropItem<PersonUIItem>(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                rowIndex = 0,
                columnIndex = column
            ) { isInBound, personItem, rowPosition, columnPosition ->

                Log.e("COLUMN: ", "personItem $personItem")
                Log.e("COLUMN: ", "isInBound $isInBound")
                Log.e("COLUMN: ", "columnToIndex $columnPosition")
                Log.e("COLUMN: ", "rowToIndex $rowPosition")
                Log.e("COLUMN: ", "------------------------------------------------------------")

                if (isInBound) {
                    if (personItem != null) {
                        //personItem.id = rowPosition.to.toString()
                        personItem.columnPosition.to = columnPosition
                        LaunchedEffect(key1 = personItem) {
                            mainViewModel.addPersons(
                                personItem,
                                rowPosition,
                                columnPosition,
                                index = personItem.id.toInt()
                            )
                        }
                    }

                    ColumnCard(
                        context = context,
                        mainViewModel = mainViewModel,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        idColumn = column,
                        elevation = 6,
                        taskItems = tasksInStatus,
                        modifier = Modifier
                            .width(180.dp)
                            .padding(8.dp)
                            .border(
                                1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(Color.Transparent.copy(alpha = 0.2f))
                    )
                } else {
                    ColumnCard(
                        context = context,
                        mainViewModel = mainViewModel,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        idColumn = column,
                        taskItems = tasksInStatus,
                        modifier = Modifier
                            .width(180.dp)
                            .padding(8.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun ColumnCard(
    context: Context,
    screenWidth: Int,
    screenHeight: Int,
    elevation: Int = 0,
    modifier: Modifier = Modifier,
    idColumn: COLUMN,
    taskItems: List<PersonUIItem>,
    mainViewModel: MainViewModel
) {

    Column {

        // Encabezado de estado
        Text(
            text = idColumn.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Divider()

        LazyColumn(
            modifier = modifier
        ) {
            Log.i("TASK LIST", "list $taskItems")
            taskItems.forEachIndexed { index, personUIItem ->
                item {
                    // Elemento de tarjeta de tarea
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    personUIItem.id = index.toString()
                    personUIItem.columnPosition.from = idColumn
                    Log.i("TASK", "task $personUIItem")
                    Log.i("TASK", "idColumn $idColumn")
                    DragTarget(
                        rowIndex = personUIItem.rowPosition.from ?: 0,
                        columnIndex = personUIItem.columnPosition.from as COLUMN,
                        dataToDrop = personUIItem,
                        vibrator = vibrator,
                        mainViewModel = mainViewModel
                    ) {
                        Card(
                            backgroundColor = personUIItem.backgroundColor,
                            modifier = Modifier
                                .width(Dp(screenWidth / 2f))
                                .height(Dp(screenHeight / 5f))
                                .padding(8.dp)
                                .shadow(elevation.dp, RoundedCornerShape(15.dp))
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = personUIItem.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = personUIItem.column.name,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}