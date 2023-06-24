/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsoft.comunityapp.draganddrop.SimpleData
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.dragdroid_kt.components.DragItem
import com.nsoft.comunityapp.dragdroid_kt.components.DraggableScreen
import com.nsoft.comunityapp.dragdroid_kt.components.DropItem

/**
 * Composable Screen for design functional personalize
 * @see updateDroppedList
 * @see startCardDragging
 * @see endCardDragging
 * */
@Composable
fun SingleDragDropScreen(
    simpleData: SimpleData
) {

    val count = remember(key1 = simpleData.dropCount.value) {
        simpleData.dropCount.value
    }

    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(bottom = 20.dp)
        ) {
            val data = DragItem("DRAG", id = "01", backgroundColor = Color.Cyan)

            /** Composable for contains your Drag item and drop
             * @see DraggableScreen
             * */
            DraggableScreen(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(0.8f))
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /** Composable for event Drag your item
                     * @see DragItem
                     * */
                    DragItem<DragItem>(
                        modifier = Modifier
                            .size(200.dp),
                        dataToDrop = data,
                        vibrator = null,
                        onStart = { simpleData.startCardDragging(it) },
                        onEnd = { simpleData.endCardDragging(it) }
                    ) { it, dataMoved ->
                        Box(
                            Modifier
                                .size(150.dp)
                                .shadow(6.dp, shape = RoundedCornerShape(10.dp))
                                .background(Color.Cyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = data.name)
                        }
                    }

                    /** Drop your item
                     * @see DropItem
                     * */
                    DropItem<DragItem>(
                        modifier = Modifier
                            .size(200.dp)
                    ) { isBound, data ->
                        LaunchedEffect(
                            key1 = data != null,
                            key2 = isBound
                        ) {
                            if (isBound && data != null) {
                                simpleData.updateDroppedList(
                                    data
                                )
                            }
                        }

                        if (isBound) {
                            Box(
                                Modifier
                                    .size(150.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(10.dp))
                                    .border(
                                        1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(Color.Cyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "DROP HEAR!!...")
                            }

                        } else {
                            Box(
                                Modifier
                                    .size(150.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(10.dp))
                                    .background(Color.Cyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "DROP HEAR!!...")
                            }
                        }
                    }
                }
            }

        }


        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

        Box(
            modifier = Modifier.align(CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Drop Counter", fontSize = 16.sp)
                Text(
                    text = "$count",
                    fontSize = 30.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
            }
        }

    }
}