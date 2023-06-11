/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.dragdroid_kt.components

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

/**
 * [ColumnDropCard] composable in charge of containing columns and notify row items to composable.
 * @param params is the style params [ColumnParameters StyleParams][ColumnParameters.StyleParams].
 * @param actionParams is the action params [ColumnParameters ActionParams][ColumnParameters.ActionParams].
 * @param header is the body row container composable parameter in charge of allowing you to customize some column parameters. (column header).
 * @param key is the listener for column state changes (around bound or not).
 * @param emptyItem is the body row container composable parameter when row list is empty in charge of allowing you to customize some column parameters. (item data Drop). this parameter have a default value [EmptyDropCard].
 * @param body is the body row container composable parameter in charge of allowing you to customize some column parameters. (item data Drag).
 * @see com.nsoft.communityapp.draganddrop.MainActivity
 * **/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
inline fun <reified T, reified K> ColumnDropCard(
    params: ColumnParameters.StyleParams<T, K>,
    actionParams: ColumnParameters.ActionParams<T, K>,
    header: @Composable () -> Unit,
    noinline key: (T) -> Any,
    crossinline emptyItem: @Composable () -> Unit = { EmptyDropCard(params) },
    crossinline body: @Composable ((data: T?) -> Unit)
) {
    Column {

        // Encabezado de estado
        header.invoke()
        Divider()
        if (params.rowList.isNullOrEmpty()) {
            emptyItem.invoke()
        } else {
            val vibrator =
                params.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            LazyColumn(
                modifier = params.modifier
            ) {
                items(items = params.rowList, key = key) { data ->
                    // Elemento de tarjeta de tarea
                    val index = params.rowList.indexOf(data)
                    actionParams.onStart?.let { onStart ->
                        actionParams.onEnd?.let { onEnd ->
                            params.idColumn?.let { idColumn ->
                                DragItem(
                                    rowIndex = index,
                                    columnIndex = idColumn,
                                    dataToDrop = data,
                                    vibrator = vibrator,
                                    onStart = onStart,
                                    onEnd = onEnd
                                ) { isDrag, dataMoved ->
                                    if (isDrag && data == dataMoved) {
                                        Box(
                                            Modifier
                                                .background(Color.White)
                                                .width(Dp((params.screenWidth ?: 0) / 2.1f))
                                                .height(Dp((params.screenHeight ?: 0) / 6f))
                                                .padding(8.dp)
                                                .shadow(0.dp, RoundedCornerShape(15.dp))
                                        )
                                    } else {
                                        body.invoke(data)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

/**
 * [EmptyDropCard] composable in charge of containing columns and notify row items to composable.
 * @param styleParams is the style params [ColumnParameters StyleParams][ColumnParameters.StyleParams].
 * @see com.nsoft.comunityapp.dragdroid_kt.components.EmptyDropCard
 * **/
@Composable
fun <T, K> EmptyDropCard(
    styleParams: ColumnParameters.StyleParams<T, K>
) {
    Box(
        Modifier
            .background(Color.White)
            .width(Dp((styleParams.screenWidth ?: 0) / 2.1f))
            .height(Dp((styleParams.screenHeight ?: 0) / 6f))
            .padding(8.dp)
            .then(styleParams.modifier)
    )
}