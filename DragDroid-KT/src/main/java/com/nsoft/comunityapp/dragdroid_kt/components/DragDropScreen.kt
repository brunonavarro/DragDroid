package com.nsoft.comunityapp.dragdroid_kt.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.RowPosition

/**
 * [DragDropScreen] composable in charge of containing the rows and columns.
 * @param context is the application view context.
 * @param columnsItems is the list of columns.
 * @param groupTasks is the map of tasks grouped by columns.
 * @param stateListener is the listener for column state changes (in Bound or not in bound)
 * @param updateBoard is the parameter in charge of notifying when it is necessary to update a row item due to a column change.
 * @param customComposable is the column container composable parameter in charge of allowing you to customize some column parameters. (header, body and other layout parameters)
 * @see com.nsoft.comunityapp.draganddrop.MainActivity
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
inline fun <reified T, reified K : Any> DragDropScreen(
    context: Context,
    columnsItems: List<K>,
    groupTasks: Map<K, List<T>>,
    crossinline stateListener: (style: ColumnParameters.StyleParams<T, K>) -> Unit,
    crossinline updateBoard: (
        item: T,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<K>
    ) -> Unit,
    crossinline customComposable: @Composable () -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    // Agrupar las tareas por estado
    val columnList = columnsItems.groupBy { it }

    DraggableScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(0.8f))
    ) {
        LazyRow(Modifier.fillMaxWidth()) {
            items(columnList.keys.toList(), key = { it }) { column ->
                val rowList = groupTasks[column] ?: emptyList()
                DropItem<T, K>(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    columnIndex = column
                ) { isInBound, personItem, rowPosition, columnPosition, isDrag ->
                    LaunchedEffect(
                        key1 = personItem != null,
                        key2 = isInBound && !isDrag && columnPosition.canAdd()
                    ) {
                        if (isInBound && !isDrag && columnPosition.canAdd() && personItem != null) {
                            updateBoard(
                                personItem,
                                rowPosition,
                                columnPosition
                            )
                        }
                    }
                    if (isInBound && isDrag) {
                        stateListener.invoke(
                            ColumnParameters.StyleParams(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(
                                        1.dp,
                                        color = Color.DarkGray,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                context = context,
                                idColumn = column,
                                elevation = 6,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                rowList = rowList,
                                borderColorInBound = Color.DarkGray
                            )
                        )
                        customComposable()
                    } else {
                        stateListener.invoke(
                            ColumnParameters.StyleParams(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                context = context,
                                idColumn = column,
                                elevation = 6,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                rowList = rowList,
                                borderColor = null
                            )
                        )
                        customComposable()
                    }
                }
            }
        }
    }
}