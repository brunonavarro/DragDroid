/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.interfaces

import android.content.Context
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * [ColumnParameters] is the parent class of style and action parameters
 * @author [Bruno Daniel Navarro Nuñez.]
 * */
sealed class ColumnParameters<T, K> {
    /**
     * [StyleParams] is the class style parameters
     * @param context is the application view context.
     * @param idColumn is the column identification.
     * @param screenWidth is the screen view width.
     * @param screenHeight is the screen view height.
     * @param elevation is the elevation cardView.
     * @param modifier is the [Modifier] interface for Composable view.
     * @param borderColorInBound is the border color when cardView is in bound in column.
     * @param borderColor is the border color when cardView is not in bound in column.
     * @param rowList is the list of task in column.
     * @see com.nsoft.comunityapp.draganddrop.MainActivity
     * */
    data class StyleParams<T, K>(
        val context: Context,
        val idColumn: K? = null,
        val screenWidth: Int? = null,
        val screenHeight: Int? = null,
        val elevation: Int = 6,
        val modifier: Modifier = Modifier,
        val borderColorInBound: Color? = null,
        val borderColor: Color? = null,
        val rowList: List<T>
    ) : ColumnParameters<T, K>() {

        /**
         * function for customize any value of [StyleParams]
         * - for you use apply [custom] function
         * @see com.nsoft.comunityapp.draganddrop.MainActivity
         * **/
        fun custom(
            screenWidth: Int? = null,
            screenHeight: Int? = null,
            elevation: Int = 6,
            modifier: Modifier = Modifier,
            BorderColorInBound: Color? = null,
            BorderColor: Color? = null
        ) = this.copy(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            elevation = elevation,
            modifier = modifier,
            borderColorInBound = BorderColorInBound,
            borderColor = BorderColor
        )
    }

    /**
     * [ActionParams] is the action parameters class
     * @param onStart is the start action of drag  cardView.
     * @param onEnd is the end action of drag cardView.
     * @param onClick is the  click action for open cardView.
     * @see com.nsoft.communityapp.draganddrop.MainActivity
     * */
    data class ActionParams<T, K>(
        val onStart: ((item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit)? = null,
        val onEnd: ((item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit)? = null,
        val onClick: ((T) -> Unit)? = null
    ) : ColumnParameters<T, K>()

    /**
     * [ActionParams] is the action parameters class
     * @param onStart is the start action of drag  cardView.
     * @param onEnd is the end action of drag cardView.
     * @param onClick is the  click action for open cardView.
     * @see com.nsoft.communityapp.draganddrop.MainActivity
     * */
    data class LoadingParams<T, K>(
        val isLoading: Boolean = false,
        val colorStroke: Color = Color.Black,
        val strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    ) : ColumnParameters<T, K>()
}