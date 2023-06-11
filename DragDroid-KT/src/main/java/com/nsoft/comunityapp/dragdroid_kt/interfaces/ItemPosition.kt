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

    open fun getRowPosition() = rowPosition.from ?: 0

    open fun canAdd(): Boolean {
        return columnPosition.canAdd()
    }

    fun initRowPosition(): Any {
        return rowPosition.from as Any
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
    var from: Any? = 0,
    var to: Any? = 0
) {
    fun canAdd() = from != to
}
