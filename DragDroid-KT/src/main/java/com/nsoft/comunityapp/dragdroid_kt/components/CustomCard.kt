/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

/**
 * Clase CustomUIDragItem
 * del card de las tareas
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
inline fun <reified T, reified K> ColumnCard(
    params: ColumnParameters.StyleParams<T, K>,
    header: @Composable () -> Unit,
    noinline key: (T) -> Any,
    crossinline body: @Composable (data: T) -> Unit
) {
    Column {

        // Encabezado de estado
        header.invoke()

        Divider()

        LazyColumn(
            modifier = params.modifier
        ) {
            items(params.rowList ?: listOf(), key = key) { personUIItem ->
                // Elemento de tarjeta de tarea
                body.invoke(personUIItem)
            }
        }
    }
}