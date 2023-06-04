/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.interfaces


abstract class ItemPositionImpl<K>(
    override var rowPosition: RowPosition,
    override var columnPosition: ColumnPosition<K>
) : ItemPosition<K> {
    abstract fun canAdd(): Boolean

    fun initRowPosition(): Any {
        return rowPosition.from as Any
    }

    fun initListenerColumn(rowIndex: Int, columnIndex: K?): ListenersColumn<K> {
        columnPosition.from = columnIndex
        return ListenersColumn(
            rowIndex = rowIndex,
            columnIndex = columnIndex
        )
    }
}

interface ItemPosition<K> {
    var rowPosition: RowPosition
    var columnPosition: ColumnPosition<K>
}

data class ColumnPosition<K>(
    var from: K? = null,
    var to: K? = null
) {
    fun canAdd() = from != to
}

data class RowPosition(
    var from: Int? = 0,
    var to: Int? = 0
) {
    fun canAdd() = from != to
}
