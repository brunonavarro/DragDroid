package com.nsoft.comunityapp.draganddrop.ui.entities

import androidx.compose.ui.graphics.Color
import com.nsoft.comunityapp.draganddrop.ui.library.ColumnPosition
import com.nsoft.comunityapp.draganddrop.ui.library.CustomerPerson
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


class PersonUIItemIMPl(override var column: COLUMN, override var backgroundColor: Color) :
    ItemUI<DragItem> {
    override fun updateItem(
        dragItem: DragItem,
        index: Int,
        columnPosition: ColumnPosition<COLUMN>,
        rowPosition: RowPosition
    ) {
        dragItem.id = index
        dragItem.rowPosition.from = index
        dragItem.rowPosition.to = rowPosition.to
        columnPosition.to?.let {
            dragItem.column = it
        }
        dragItem.columnPosition.from = columnPosition.from
        dragItem.columnPosition.to = columnPosition.to
        dragItem.isDraggable = false
    }

}

data class DragItem(
    var name: String,
    var id: Int = 0,
    override var backgroundColor: Color,
    var isDraggable: Boolean = false,
    override var column: COLUMN = COLUMN.TO_DO
) : CustomerPerson, ItemUI<DragItem> by PersonUIItemIMPl(column, backgroundColor), ItemPosition<COLUMN>(
    rowPosition = RowPosition(from = id),
    columnPosition = ColumnPosition(from = column)
)

enum class COLUMN {
    TO_DO,
    IN_PROGRESS,
    DEV_DONE
}