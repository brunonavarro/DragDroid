/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import android.content.Context
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.dragdroid_kt.components.DragTarget
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

@Composable
fun CustomDragCard(
    data: DragItem?,
    styleParams: ColumnParameters.StyleParams<DragItem, Column>,
    actionParams: ColumnParameters.ActionParams<DragItem, Column>
) {
    val vibrator =
        styleParams.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    actionParams.onStart.let { onStart ->
        actionParams.onEnd.let { onEnd ->
            if (data == null) {
                CustomEmptyDragCard(styleParams)
            } else {
                data.columnPosition.from = styleParams.idColumn
                DragTarget<DragItem, Column>(
                    rowIndex = data.rowPosition.from ?: 0,
                    columnIndex = data.columnPosition.from as Column,
                    dataToDrop = data,
                    vibrator = vibrator,
                    onStart = onStart,
                    onEnd = onEnd
                ) { isDrag, dataMoved ->
                    if (isDrag && data == dataMoved) {
                        Log.e("ABC", "isDrag $isDrag - data $data")
                        Box(
                            Modifier
                                .background(Color.White)
                                .width(Dp((styleParams.screenWidth ?: 0) / 2.1f))
                                .height(Dp((styleParams.screenHeight ?: 0) / 6f))
                                .padding(8.dp)
                                .shadow(0.dp, RoundedCornerShape(15.dp))
                        )
                    } else {
                        Card(
                            backgroundColor = data.backgroundColor,
                            modifier = Modifier
                                .width(Dp((styleParams.screenWidth ?: 0) / 2.1f))
                                .padding(8.dp)
                                .shadow(styleParams.elevation.dp, RoundedCornerShape(15.dp))
                        ) {
                            Column(
                                (Modifier
                                    .wrapContentSize()
                                    .padding(16.dp))
                            ) {
                                Text(
                                    text = data.name,
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


@Composable
fun IconIssue(
    icon: ImageVector? = null,
    resIcon: Int? = null,
    backGroundColor: Color = Color.Transparent,
    resBackgroundColor: Int? = null,
    tint: Color = Color.Black,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = backGroundColor,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .width(26.dp)
            .height(26.dp)
            .shadow(
                shape = RoundedCornerShape(5.dp),
                elevation = 0.dp
            ),
        elevation = 0.dp
    ) {
        IconButton(onClick = { onClick.invoke() }) {
            icon?.let {
                Icon(imageVector = it, contentDescription = "", tint = tint)
            }
            resIcon?.let {
                Icon(painter = painterResource(id = it), contentDescription = "", tint = tint)
            }
        }
    }
}


@Composable
fun CardText(
    backgroundColor: Color,
    textAlign: TextAlign,
    color: Color = Color.Black,
    modifierCard: Modifier,
    modifierText: Modifier,
    text: String? = null,
    resText: Int? = null
) {
    Card(
        backgroundColor = backgroundColor,
        modifier = modifierCard
    ) {
        text?.let {
            Text(
                text = it, color = color, textAlign = textAlign, modifier = modifierText
            )
        }

        resText?.let {
            Text(
                text = stringResource(id = it),
                color = color,
                textAlign = textAlign,
                modifier = modifierText
            )
        }
    }
}