/*
 * Copyright (c) 2023. Esto pertenece a codigo de Libreria Compose con objetivo a desarrollos de Arrastre y Soltar.
 * Los derechos de comercialización son reservados en discución con el equipo colaborador.
 * By BrunoNavarro
 */

package com.nsoft.comunityapp.draganddrop.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomHeaderColumn(
    params: Params.CustomParams
) {
    Text(
        text = params.idColumn?.name ?: "",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(Dp((params.screenWidth ?: 0) / 2.1f))
            .padding(vertical = 8.dp)
    )
}