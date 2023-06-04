package com.nsoft.comunityapp.draganddrop.ui.entities

import androidx.compose.ui.graphics.Color
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ItemUIImpl
import com.nsoft.comunityapp.dragdroid_kt.interfaces.RowPosition

//interface ItemUI<T> {
//    var column: Column
//    var backgroundColor: Color
//
//    fun updateItem(
//        personUIItem: T,
//        index: Int,
//        columnPosition: ColumnPosition<Column>,
//        rowPosition: RowPosition
//    )
//}


//class PersonUIItemIMPl(override var column: Column, override var backgroundColor: Color) :
//    ItemUI<DragItem> {
//    override fun updateItem(
//        dragItem: DragItem,
//        index: Int,
//        columnPosition: ColumnPosition<Column>,
//        rowPosition: RowPosition
//    ) {
//        dragItem.id = index
//        dragItem.rowPosition.from = index
//        dragItem.rowPosition.to = rowPosition.to
//        columnPosition.to?.let {
//            dragItem.column = it
//        }
//        dragItem.columnPosition.from = columnPosition.from
//        dragItem.columnPosition.to = columnPosition.to
//        dragItem.isDraggable = false
//    }
//
//}

data class DragItem(
    var name: String,
    override var id: Int = 0,
    override var backgroundColor: Color,
    var isDraggable: Boolean = false,
    override var column: Column = Column.TO_DO
) : ItemUIImpl<DragItem, Column>(
    id = id,
    column = column, backgroundColor = backgroundColor
) {

    override fun updateItem(
        personUIItem: DragItem,
        index: Int,
        columnPosition: ColumnPosition<Column>,
        rowPosition: RowPosition
    ) {
        //id = index
//        rowPosition.from = index
        rowPosition.to = rowPosition.to
        columnPosition.to?.let {
            column = it
        }
        columnPosition.from = columnPosition.from
        columnPosition.to = columnPosition.to
        isDraggable = false
    }

    override fun canAdd(): Boolean = columnPosition.canAdd()

}