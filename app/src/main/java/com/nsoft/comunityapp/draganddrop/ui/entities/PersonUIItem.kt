package com.nsoft.comunityapp.draganddrop.ui.entities

import androidx.compose.ui.graphics.Color
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.ItemPosition
import com.nsoft.comunityapp.draganddrop.ui.library.RowPosition


data class PersonUIItem(
    var name: String,
    var id: Int = 0,
    var backgroundColor: Color,
    var isDraggable: Boolean = false,
    var column: COLUMN = COLUMN.TO_DO
) : ItemPosition(
    rowPosition = RowPosition(),
    columnPosition = ColumnPosition(from = column)
) {

    fun updateItem(
        personUIItem: PersonUIItem,
        index: Int,
        columnPosition: ColumnPosition,
        rowPosition: RowPosition
    ) {
        personUIItem.id = index
        personUIItem.rowPosition.from = index
        personUIItem.rowPosition.to = rowPosition.to
        personUIItem.column = columnPosition.to as COLUMN
        personUIItem.columnPosition.from = columnPosition.from
        personUIItem.columnPosition.to = columnPosition.to
    }

}

enum class COLUMN{
    TO_DO,
    IN_PROGRESS,
    DEV_DONE
}