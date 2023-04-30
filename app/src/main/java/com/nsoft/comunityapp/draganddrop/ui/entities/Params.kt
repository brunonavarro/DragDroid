/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui.entities

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nsoft.comunityapp.dragdroid_kt.components.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.components.CustomComposableParams
import com.nsoft.comunityapp.dragdroid_kt.components.RowPosition

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