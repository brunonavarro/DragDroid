/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

/**
 * Clase CustomUIDragItem
 * del card de las tareas
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun ColumnCard(
    params: Params.CustomParams,
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
        override val onStart: ((item: DragItem, rowPosition: RowPosition, columnPosition: ColumnPosition<COLUMN>) -> Unit)? = null,
        override val onEnd: ((item: DragItem, rowPosition: RowPosition, columnPosition: ColumnPosition<COLUMN>) -> Unit)? = null
    ) : CustomComposableParams<DragItem, COLUMN>
}