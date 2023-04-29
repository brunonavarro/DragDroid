/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.dragdroid_kt.components.*

/**
 * Clase CustomUIDragItem
 * del card de las tareas
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
inline fun <reified T : CustomerPerson, reified K> ColumnCard(
    params: CustomComposableParams<T, K>,
) {
    Column {

        // Encabezado de estado
        Text(
            text = params.getName(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(Dp((params.screenWidth ?: 0) / 2.1f))
                .padding(vertical = 8.dp)
        )
        Divider()



        LazyColumn(
            modifier = params.modifier
        ) {
            items(params.rowList ?: listOf(), key = {
                params.rowPosition(it)
            }) { personUIItem ->
                // Elemento de tarjeta de tarea
                val vibrator =
                    params.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                params.updateColumn(personUIItem, params.idColumn)
                DragTarget<T, K>(
                    rowIndex = params.rowPosition(personUIItem),
                    columnIndex = params.getColumn(personUIItem),
                    dataToDrop = personUIItem,
                    vibrator = vibrator,
                    onStart = params.onStart,
                    onEnd = params.onEnd
                ) { isDrag, data ->
                    if (isDrag && data as DragItem == personUIItem) {
                        Log.e("ABC", "isDrag $isDrag - data $data")
                        Box(
                            Modifier
                                .background(Color.White)
                                .width(Dp((params.screenWidth ?: 0) / 2.1f))
                                .height(Dp((params.screenHeight ?: 0) / 6f))
                                .padding(24.dp)
                                .shadow(0.dp, RoundedCornerShape(15.dp))
                        )
                    } else {
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
                                    text = params.nameRow(personUIItem),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Divider()
                                Spacer(params.modifier)
                                Text(
                                    text = params.nameColumn(personUIItem),
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

sealed class Params {
    class CustomParams(
        override val context: Context,
        override val screenWidth: Int? = null,
        override val screenHeight: Int? = null,
        override val elevation: Int = 0,
        override val modifier: Modifier = Modifier,
        override val idColumn: Column? = null,
        override val rowList: List<DragItem>? = null,
        override val onStart: ((item: DragItem, rowPosition: RowPosition, columnPosition: ColumnPosition<Column>) -> Unit),
        override val onEnd: ((item: DragItem, rowPosition: RowPosition, columnPosition: ColumnPosition<Column>) -> Unit)
    ) : CustomComposableParams<DragItem, Column> {
        override fun getName(): String {
            return idColumn?.name ?: ""
        }

        override fun rowPosition(it: DragItem): Int {
            return it.rowPosition.from ?: 0
        }

        override fun nameRow(it: DragItem): String {
            return it.name
        }

        override fun nameColumn(it: DragItem): String {
            return it.column.name
        }

        override fun getBackgroundColor(it: DragItem): Color {
            return Color.Blue
        }

        override fun updateColumn(it: DragItem, id: Column?) {
            it.columnPosition.from = id
        }

        override fun getColumn(it: DragItem): Column {
            return it.columnPosition.from as Column
        }

    }
}