package com.nsoft.comunityapp.draganddrop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.PersonUIItem
import com.nsoft.comunityapp.draganddrop.ui.library.DragTarget
import com.nsoft.comunityapp.draganddrop.ui.library.DropItem


@Composable
fun DragDropScreen(
    mainViewModel: MainViewModel
){
    // Agrupar las tareas por estado
    val columnByStatus = mainViewModel.columnsItems.groupBy { it }

    val tasksByStatus = mainViewModel.taskItems.groupBy { it.column }

    // Estado para realizar un seguimiento de la tarea actualmente arrastrada
    var draggedTask by remember { mutableStateOf<PersonUIItem?>(null) }

    // Crear un LazyColumn para representar las columnas de estado
    LazyRow(Modifier.fillMaxWidth()) {
        items(columnByStatus.keys.toList()) { status ->
            val tasksInStatus = tasksByStatus[status] ?: emptyList()

            // Crear un LazyColumn para representar las filas de tarjetas de tarea
            DropItem<PersonUIItem>(
                modifier = Modifier
                .fillMaxWidth().fillMaxHeight(),
                rowToIndex= 0,
                columnToIndex= status
            ) { isInBound, personItem, rowToIndex, columnToIndex ->

                if(personItem != null){
                    LaunchedEffect(key1 = personItem){
                        mainViewModel.addPersons(personItem, rowToIndex, columnToIndex)
                    }
                }

                if(isInBound){
                    LazyColumn(
                        Modifier
                            .width(180.dp)
                            .padding(8.dp)
                            .border(
                                1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(Color.Transparent.copy(alpha = 0.2f))
                    ) {
                        item {
                            // Encabezado de estado
                            Text(
                                text = status.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(tasksInStatus) { task ->
                            // Elemento de tarjeta de tarea
                            DragTarget(
                                rowIndex = task.id.toInt(),
                                columnIndex = task.column,
                                dataToDrop = task,
                                mainViewModel = mainViewModel
                            ) {
                                Card(
                                    backgroundColor = task.backgroundColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            text = task.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Text(
                                            text = task.column.name,
                                            color = Color.White,
                                            modifier = Modifier.align(Alignment.End)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        Modifier
                            .width(180.dp)
                            .padding(8.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f))
                    ) {
                        item {
                            // Encabezado de estado
                            Text(
                                text = status.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(tasksInStatus) { task ->
                            // Elemento de tarjeta de tarea
                            DragTarget(
                                rowIndex = task.id.toInt(),
                                columnIndex = task.column,
                                dataToDrop = task,
                                mainViewModel = mainViewModel
                            ) {
                                Card(
                                    backgroundColor = task.backgroundColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            text = task.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Text(
                                            text = task.column.name,
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
    }
}