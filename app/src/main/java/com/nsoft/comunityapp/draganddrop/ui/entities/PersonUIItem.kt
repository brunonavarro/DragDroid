package com.nsoft.comunityapp.draganddrop.ui.entities

import androidx.compose.ui.graphics.Color

data class PersonUIItem(
    val name: String,
    val id: String,
    val backgroundColor: Color,
    var isDraggable: Boolean = false,
    var column: COLUMN = COLUMN.TO_DO
)

enum class COLUMN{
    TO_DO,
    IN_PROGRESS,
    DEV_DONE
}