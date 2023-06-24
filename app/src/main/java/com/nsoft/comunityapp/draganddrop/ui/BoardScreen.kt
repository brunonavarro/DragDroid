/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nsoft.comunityapp.draganddrop.RowColumnData
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme
import com.nsoft.comunityapp.dragdroid_kt.components.ColumnDropCard
import com.nsoft.comunityapp.dragdroid_kt.components.DragDropScreen
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

/**
 * Composable Screen of dashboard for administration of tasks.
 * @see updateTasks
 * @see startDragging
 * @see endDragging
 * */
@Composable
fun BoardScreen(
    rowColumnData: RowColumnData
) {
    val groupTasks =
        rowColumnData.taskItems.filter { !it.isDraggable }.groupBy { it.column }


    val isLoading by remember {
        mutableStateOf(false)
    }

    var stateParams by remember {
        mutableStateOf<ColumnParameters.StyleParams<DragItem, Column>?>(null)
    }
    Column {
        DragDropScreen(
            context = LocalContext.current,
            columnsItems = rowColumnData.columnsItems,
            groupTasks = groupTasks,
            updateBoard = rowColumnData.updateTasks,
            stateListener = { stateParams = it }
        ) {
            stateParams?.let { styleParams ->
                ColumnDropCard(
                    params = styleParams,
                    actionParams = ColumnParameters.ActionParams(
                        onStart = rowColumnData.startDragging,
                        onEnd = rowColumnData.endDragging
                    ),
                    loadingParams = ColumnParameters.LoadingParams(
                        isLoading = isLoading,
                        colorStroke = Color.Black,
                        strokeWidth = 5.dp
                    ),
                    key = { item -> item.getKey() },
                    header = {
                        CustomHeaderColumn(
                            params = styleParams
                        )
                    },
                    body = { data ->
                        CustomDragCard(
                            data = data,
                            styleParams = styleParams,
                            actionParams = ColumnParameters.ActionParams(
                                onClick = { item ->
                                    Toast.makeText(
                                        stateParams?.context,
                                        "CLICK CARD : ${item.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        )
                    }
                )
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