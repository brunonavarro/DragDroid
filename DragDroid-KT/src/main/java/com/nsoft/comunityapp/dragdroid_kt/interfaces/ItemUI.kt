/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.interfaces

import androidx.compose.ui.graphics.Color

abstract class ItemUIImpl<T, K>(
    override var isDraggable: Boolean,
    override var id: Any,
    override var column: K, override var backgroundColor: Color
) : ItemUI<T, K>, ItemPositionImpl<K>(
    rowPosition = RowPosition(from = id),
    columnPosition = ColumnPosition(from = column)
) {
    open fun updateItem(
        columnPosition: ColumnPosition<K>
    ) {
        columnPosition.to?.let {
            column = it
        }
        isDraggable = false
    }
}

sealed interface ItemUI<T, K> {
    var isDraggable: Boolean
    var id: Any
    var column: K
    var backgroundColor: Color
}