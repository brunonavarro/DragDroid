/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.interfaces

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


open class ParamsImplementation<T, K>(
    override val context: Context,
    override val elevation: Int,
    override val idColumn: K?,
    override val modifier: Modifier,
    override val screenHeight: Int?,
    override val screenWidth: Int?,
    override val rowList: List<T>?,
    override val onStart: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit,
    override val onEnd: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit
) : ParamsImpl<T, K>(
    context, elevation, idColumn, modifier, screenHeight, screenWidth, rowList, onStart, onEnd
) {
    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun rowPosition(it: T): Int {
        TODO("Not yet implemented")
    }

    override fun nameRow(it: T): String {
        TODO("Not yet implemented")
    }

    override fun nameColumn(it: T): String {
        TODO("Not yet implemented")
    }

    override fun getBackgroundColor(it: T): Color {
        TODO("Not yet implemented")
    }

    override fun updateColumn(it: T, id: K?) {
        TODO("Not yet implemented")
    }

    override fun getColumn(it: T): K {
        TODO("Not yet implemented")
    }

}


abstract class ParamsImpl<T, K>(
    override val context: Context,
    override val elevation: Int,
    override val idColumn: K?,
    override val modifier: Modifier,
    override val screenHeight: Int?,
    override val screenWidth: Int?,
    override val rowList: List<T>?,
    override val onStart: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit,
    override val onEnd: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit
) : Params<T, K> {
    abstract fun getName(): String

    abstract fun rowPosition(it: T): Int

    abstract fun nameRow(it: T): String
    abstract fun nameColumn(it: T): String

    abstract fun getBackgroundColor(it: T): Color


    abstract fun updateColumn(it: T, id: K?)

    abstract fun getColumn(it: T): K
}

interface Params<T, K> {
    val context: Context
    val screenWidth: Int?
    val screenHeight: Int?
    val elevation: Int
    val modifier: Modifier
    val idColumn: K?
    val rowList: List<T>?

    val onStart: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit
    val onEnd: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit
}