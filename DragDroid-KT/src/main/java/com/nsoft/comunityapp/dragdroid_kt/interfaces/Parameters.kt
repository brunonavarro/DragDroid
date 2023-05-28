/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.interfaces

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

sealed class ColumnParameters<T, K> {
    data class StyleParams<T, K>(
        val context: Context,
        val idColumn: K? = null,
        val screenWidth: Int? = null,
        val screenHeight: Int? = null,
        val elevation: Int = 6,
        val modifier: Modifier = Modifier,
        val BorderColorInBound: Color? = null,
        val BorderColor: Color? = null,
        val rowList: List<T>,
        var keyColumn: Int? = null
    ) : ColumnParameters<T, K>()

    data class ActionParams<T, K>(
        val onStart: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit,
        val onEnd: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit
    ) : ColumnParameters<T, K>()
}