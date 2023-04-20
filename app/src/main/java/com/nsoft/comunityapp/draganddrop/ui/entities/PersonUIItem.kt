package com.nsoft.comunityapp.draganddrop.ui.entities

import androidx.compose.ui.graphics.Color
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.ItemPosition
import com.nsoft.comunityapp.draganddrop.ui.library.RowPosition

interface ItemUI<T> {
    var column: COLUMN
    var backgroundColor: Color
    fun updateItem(
        personUIItem: T,
        index: Int,
        columnPosition: ColumnPosition<COLUMN>,
        rowPosition: RowPosition
    )
}


class PersonUIItemIMPl(override var column: COLUMN, override var backgroundColor: Color):ItemUI<PersonUIItem>
{
    override fun updateItem(
        personUIItem: PersonUIItem,
        index: Int,
        columnPosition: ColumnPosition<COLUMN>,
        rowPosition: RowPosition
    ) {
        personUIItem.id = index
        personUIItem.rowPosition.from = index
        personUIItem.rowPosition.to = rowPosition.to
        columnPosition.to?.let {
            personUIItem.column = it
        }
        personUIItem.columnPosition.from = columnPosition.from
        personUIItem.columnPosition.to = columnPosition.to
    }

}

data class PersonUIItem(
    var name: String,
    var id: Int = 0,
    override var backgroundColor: Color,
    var isDraggable: Boolean = false,
    override var column: COLUMN = COLUMN.TO_DO
) : ItemUI<PersonUIItem> by PersonUIItemIMPl(column,backgroundColor) , ItemPosition<COLUMN>(
    rowPosition = RowPosition(),
    columnPosition = ColumnPosition(from = column)
)

enum class COLUMN {
    TO_DO,
    IN_PROGRESS,
    DEV_DONE
}