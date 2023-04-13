package com.nsoft.comunityapp.draganddrop.ui

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
import com.nsoft.comunityapp.draganddrop.ui.entities.PersonUIItem
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.DragTarget
import com.nsoft.comunityapp.draganddrop.ui.library.RowPosition

/**
 * Clase para agregar un UI customizado del card de las tareas
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun ColumnCard(
    params: Params.CustomParams
) {
    Column {

        // Encabezado de estado
        Text(
            text = params.idColumn?.name ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(Dp((params.screenWidth ?: 0) / 2.1f))
                .padding(vertical = 8.dp)
        )

        Divider()

        params.onStart?.let {
            params.onEnd?.let {
                LazyColumn(
                    modifier = params.modifier
                ) {
                    items(params.rowList ?: listOf(), key = {
                        it.id
                    }) { personUIItem ->
                        // Elemento de tarjeta de tarea
                        val vibrator =
                            params.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        personUIItem.columnPosition.from = params.idColumn
                        DragTarget(
                            rowIndex = personUIItem.rowPosition.from ?: 0,
                            columnIndex = personUIItem.columnPosition.from as COLUMN,
                            dataToDrop = personUIItem,
                            vibrator = vibrator,
                            onStart = params.onStart,
                            onEnd = params.onEnd
                        ) {
                            Card(
                                backgroundColor = personUIItem.backgroundColor,
                                modifier = Modifier
                                    .width(Dp((params.screenWidth ?: 0) / 2.1f))
                                    .height(Dp((params.screenHeight ?: 0) / 6f))
                                    .padding(8.dp)
                                    .shadow(params.elevation.dp, RoundedCornerShape(15.dp))
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = personUIItem.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Divider()
                                    Spacer(params.modifier)
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
}

sealed class Params {
    data class CustomParams(
        val context: Context,
        val screenWidth: Int? = null,
        val screenHeight: Int? = null,
        val elevation: Int = 0,
        val modifier: Modifier = Modifier,
        val idColumn: COLUMN? = null,
        val rowList: List<PersonUIItem>? = null,
        val onStart: ((item: Any, rowPosition: RowPosition, columnPosition: ColumnPosition) -> Unit)? = null,
        val onEnd: ((item: Any, rowPosition: RowPosition, columnPosition: ColumnPosition) -> Unit)? = null
    )
}