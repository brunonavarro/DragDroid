package com.nsoft.comunityapp.dragdroid_kt.components

//import com.nsoft.comunityapp.dragdroid_kt.entities.BoardParam
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

/*val LocalBoardParamInfo = localBoardParamInfo<Any, Any>()*/

/*inline fun <reified T, reified K : Any> localBoardParamInfo(): ProvidableCompositionLocal<BoardParam<T, K>> {
    return compositionLocalOf { BoardParam() }
}*/

/**
 * Clase exclusiva de Libreria
 * * Construye el Board estilo Jira
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
inline fun <reified T, reified K : Any> DragDropScreen(
    context: Context,
    columnsItems: List<K>,
    rowListByGroup: Map<K, List<T>>,
    crossinline callBackColumn: (style: ColumnParameters.StyleParams<T, K>) -> Unit,
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
        // Crear un LazyColumn para representar las columnas de estado
        LazyRow(Modifier.fillMaxWidth()) {
            items(columnList.keys.toList(), key = { it }) { column ->
                val rowList = rowListByGroup[column] ?: emptyList()
                // Crear un LazyColumn para representar las filas de tarjetas de tarea
                DropItemMain<T, K>(
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
                        callBackColumn.invoke(
                            ColumnParameters.StyleParams<T, K>(
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
                                BorderColorInBound = Color.DarkGray
                            )
                        )
                        customComposable()
                    } else {
                        callBackColumn.invoke(
                            ColumnParameters.StyleParams<T, K>(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                context = context,
                                idColumn = column,
                                elevation = 6,
                                screenWidth = screenWidth,
                                screenHeight = screenHeight,
                                rowList = rowList,
                                BorderColor = null
                            )
                        )
                        customComposable()
                    }
                }
            }
        }
    }
}