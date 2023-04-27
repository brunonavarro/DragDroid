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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.CustomComposableParams
import com.nsoft.comunityapp.draganddrop.ui.library.RowPosition
import com.nsoft.comunityapp.draganddrop.ui.library.*

/**
 * Clase CustomUIDragItem
 * del card de las tareas
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
inline fun <reified T : CustomerPerson, reified K> ColumnCard(
    params: CustomComposableParams<T, K>,
    header: @Composable (Params.CustomParams) -> Unit,
    body: @Composable (data: DragItem, Params.CustomParams) -> Unit
) {
    Column {

        // Encabezado de estado
        header.invoke(params)

        Divider()

        LazyColumn(
            modifier = params.modifier
        ) {
            items(params.rowList ?: listOf(),
                key = {
                    it.rowPosition.from ?: 0
                }
            ) { personUIItem ->
                // Elemento de tarjeta de tarea
                body.invoke(personUIItem, params)
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
        override val idColumn: COLUMN? = null,
        override val rowList: List<DragItem>? = null,
        override val onStart: ((item: DragItem, rowPosition: RowPosition, columnPosition: ColumnPosition<COLUMN>) -> Unit),
        override val onEnd: ((item: DragItem, rowPosition: RowPosition, columnPosition: ColumnPosition<COLUMN>) -> Unit)
    ) : CustomComposableParams<DragItem, COLUMN> {
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

        override fun updateColumn(it: DragItem, id: COLUMN?) {
            it.columnPosition.from = id
        }

        override fun getColumn(it: DragItem): COLUMN {
            return it.columnPosition.from as COLUMN
        }

    }
}