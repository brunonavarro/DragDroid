package com.nsoft.comunityapp.draganddrop.ui.entities

import androidx.compose.ui.graphics.Color
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ItemUIImpl

data class DragItem(
    var name: String,
    override var id: Any = 0,
    override var backgroundColor: Color,
    override var isDraggable: Boolean = false,
    override var column: Column = Column.TO_DO
) : ItemUIImpl<DragItem, Column>(
    id = id,
    isDraggable = isDraggable,
    column = column, backgroundColor = backgroundColor
)