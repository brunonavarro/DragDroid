/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

@Composable
fun CustomDragCard(
    data: DragItem?,
    styleParams: ColumnParameters.StyleParams<DragItem, Column>,
    actionParams: ColumnParameters.ActionParams<DragItem, Column>
) {
    if (data == null) {
        CustomEmptyDragCard(styleParams)
    } else {
        Card(
            backgroundColor = data.backgroundColor,
            modifier = Modifier
                .width(Dp((styleParams.screenWidth ?: 0) / 2.1f))
                .padding(8.dp)
                .shadow(styleParams.elevation.dp, RoundedCornerShape(15.dp))
                .clickable {
                    actionParams.onClick?.invoke(data)
                }
        ) {
            Column(
                (Modifier
                    .wrapContentSize()
                    .padding(16.dp))
            ) {
                Text(
                    text = data.name + " " + data.rowPosition.from,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Divider()
                Spacer(
                    Modifier
                        .height(1.dp)
                        .background(Color.White)
                        .then(styleParams.modifier)
                )
                Text(
                    text = data.column.name,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun CustomEmptyDragCard(
    styleParams: ColumnParameters.StyleParams<DragItem, Column>
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