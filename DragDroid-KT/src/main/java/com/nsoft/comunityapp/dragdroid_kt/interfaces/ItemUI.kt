/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.interfaces

import androidx.compose.ui.graphics.Color

abstract class ItemUIImpl<T, K>(
    override var id: Int,
    override var column: K, override var backgroundColor: Color
) : ItemUI<T, K>, ItemPositionImpl<K>(
    rowPosition = RowPosition(from = id),
    columnPosition = ColumnPosition(from = column)
) {
    abstract fun updateItem(
        personUIItem: T,
        index: Int,
        columnPosition: ColumnPosition<K>,
        rowPosition: RowPosition
    )
}

sealed interface ItemUI<T, K> {
    var id: Int
    var column: K
    var backgroundColor: Color
}