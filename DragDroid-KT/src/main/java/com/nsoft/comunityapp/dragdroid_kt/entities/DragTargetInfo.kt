/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.entities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.RowPosition

/**
 * [DragTargetInfo] class for Drag action current.
 * @property isDragging is true when initialize drag action.
 * @property dragPosition is position for drag action current.
 * @property dragOffset is the origin coordinates position view.
 * @property draggableComposable is the current composable drag view.
 * @property dataToDrop is the current data drag to drop.
 * @property columnPosition is the current drag column.
 * @property rowPosition is the current row index drag data.
 * */
class DragTargetInfo<T : Any, K> {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<((@Composable (isDrag: Boolean, data: T?) -> Unit)?)>(
        null
    )
    var dataToDrop by mutableStateOf<T?>(null)
    var columnPosition by mutableStateOf(ColumnPosition<K>())
    var rowPosition by mutableStateOf(RowPosition())
}