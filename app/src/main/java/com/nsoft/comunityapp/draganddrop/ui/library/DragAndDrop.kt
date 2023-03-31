package com.nsoft.comunityapp.draganddrop.ui.library

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.PersonUIItem

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

/**Movimiento de componente**/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun <T> DragTarget(
    modifier: Modifier = Modifier,
    rowIndex: Int,
    columnIndex: Any,
    dataToDrop: T,
    vibrator: Vibrator?,
    mainViewModel: MainViewModel,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember {
        mutableStateOf(Offset.Zero)
    }

    val currentState = LocalDragTargetInfo.current

    Box(
        modifier = modifier
            .onGloballyPositioned {
                currentPosition = it.localToWindow(Offset.Zero)
                Log.e("DRAG: ", " onGloballyPositioned currentPosition $currentPosition")
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        if (vibrator != null) {
                            val vibrationEffect1: VibrationEffect =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    VibrationEffect.createOneShot(
                                        200,
                                        VibrationEffect.CONTENTS_FILE_DESCRIPTOR
                                    )
                                } else {
                                    Log.e("TAG", "Cannot vibrate device..")
                                    TODO("VERSION.SDK_INT < O")
                                }

                            // it is safe to cancel other
                            // vibrations currently taking place
                            vibrator.cancel()
                            vibrator.vibrate(vibrationEffect1)
                        }

                        currentState.dataToDrop = dataToDrop
                        currentState.isDragging = true
                        currentState.dragPosition = currentPosition + it
                        currentState.columnFromIndex = columnIndex
                        currentState.rowFromIndex = rowIndex
                        currentState.draggableComposable = content

                        Log.e(
                            "DRAG taskItems: ",
                            "START -------------------------------------------"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "START currentState row from ${currentState.rowFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "START currentState row to ${currentState.rowToIndex}"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "START currentState column from ${currentState.columnFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "START currentState column to ${currentState.columnToIndex}"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "START -------------------------------------------"
                        )

                        val rowPosition = RowPosition(from = rowIndex)
                        val columnPosition =
                            ColumnPosition(from = columnIndex)

                        mainViewModel.startDragging(
                            dataToDrop as PersonUIItem,
                            rowPosition,
                            columnPosition
                        )
                    },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                        Log.e(
                            "DRAG taskItems: ",
                            "CURRENT -------------------------------------------"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "CURRENT currentState row from ${currentState.rowFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "CURRENT currentState row to ${currentState.rowToIndex}"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "CURRENT currentState column from ${currentState.columnFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "CURRENT currentState column to ${currentState.columnToIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "CURRENT -------------------------------------------"
                        )

                    },
                    onDragEnd = {
                        currentState.dragOffset = Offset.Zero
                        currentState.columnFromIndex = columnIndex
                        currentState.rowFromIndex = rowIndex
                        currentState.isDragging = false

                        Log.e("DRAG taskItems: ", "END -------------------------------------------")

                        Log.e(
                            "DRAG taskItems: ",
                            "END currentState row from ${currentState.rowFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "END currentState row to ${currentState.rowToIndex}"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "END currentState column from ${currentState.columnFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "END currentState column to ${currentState.columnToIndex}"
                        )

                        Log.e("DRAG taskItems: ", "END -------------------------------------------")

                        val rowPosition = RowPosition(from = rowIndex)
                        val columnPosition =
                            ColumnPosition(from = columnIndex)

                        mainViewModel.endDragging(
                            dataToDrop as PersonUIItem,
                            rowPosition,
                            columnPosition
                        )
                    },
                    onDragCancel = {
                        currentState.dragOffset = Offset.Zero
                        currentState.columnFromIndex = columnIndex
                        currentState.rowFromIndex = rowIndex
                        currentState.isDragging = false
                        Log.e(
                            "DRAG taskItems: ",
                            "CANCEL currentState row from ${currentState.rowFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "CANCEL currentState row to ${currentState.rowToIndex}"
                        )

                        Log.e(
                            "DRAG taskItems: ",
                            "CANCEL currentState column from ${currentState.columnFromIndex}"
                        )
                        Log.e(
                            "DRAG taskItems: ",
                            "CANCEL currentState column to ${currentState.columnToIndex}"
                        )

                        val rowPosition = RowPosition(from = rowIndex)
                        val columnPosition =
                            ColumnPosition(from = columnIndex)

                        mainViewModel.endDragging(
                            dataToDrop as PersonUIItem,
                            rowPosition,
                            columnPosition
                        )
                    }
                )
            }
    ) {
        content()
    }
}


/**ITEM QUE SOPORTA EL SOLTAR ITEM EN SU INTERIOR**/
@Composable
fun <T> DropItem(
    modifier: Modifier,
    rowIndex: Int,
    columnIndex: Any,
    canBound: Boolean = true,
    content: @Composable() (BoxScope.(isInBound: Boolean, data: T?, rows: RowPosition, column: ColumnPosition) -> Unit)
){
    val dragInfo = LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset

    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                it.boundsInWindow().let { rect ->
                    isCurrentDropTarget = if (dragInfo.isDragging) {
                        rect.contains(dragPosition + dragOffset)
                    } else false
                }
            }
    ) {
        val data = if (isCurrentDropTarget && !dragInfo.isDragging) {
            dragInfo.dataToDrop as T?
        } else {
            null
        }

        dragInfo.rowToIndex = rowIndex
        dragInfo.columnToIndex = columnIndex

        Log.e(
            "ADD $columnIndex",
            "drop event -----------------------------------------------------"
        )

        Log.e("DROP $columnIndex: ", "dragInfo $dragInfo")
        Log.e("DROP $columnIndex: ", "data $data")
        Log.e("DROP $columnIndex: ", "isCurrentDropTarget $isCurrentDropTarget")
        Log.e("DROP $columnIndex: ", "index to $rowIndex - column to $columnIndex")
        val rowPosition = RowPosition(dragInfo.rowFromIndex, dragInfo.rowToIndex ?: (rowIndex))
        val columnPosition =
            ColumnPosition(dragInfo.columnFromIndex, dragInfo.columnToIndex ?: columnIndex)


        Log.e("ADD $columnIndex", "drop event data $data")
        Log.e("ADD $columnIndex", "drop event rowPosition $rowPosition - $rowIndex")
        Log.e("ADD $columnIndex", "drop event columnPosition $columnPosition - $columnIndex")
        Log.e(
            "ADD $columnIndex",
            "drop event -----------------------------------------------------"
        )
        content(
            isCurrentDropTarget &&
                    columnPosition.from != columnPosition.to,
            data, rowPosition, columnPosition
        )
    }
}

@Composable
fun DraggableScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
){
    val state = remember {
        DragTargetInfo()
    }

    CompositionLocalProvider(
        LocalDragTargetInfo provides state
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ){
            content()
            if (state.isDragging){
                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val offset = (state.dragPosition + state.dragOffset)
                            scaleX = 1.3f
                            scaleY = 1.3f
                            alpha = if (targetSize == IntSize.Zero) {
                                0f
                            } else .9f
                            translationX = offset.x.minus(targetSize.width / 3)
                            translationY = offset.y.minus(targetSize.height / 3)
                            Log.i("SCREEN DRAG: ", "graphicsLayer offset $offset")
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
                            Log.i("SCREEN DRAG: ", "onGloballyPositioned targetSize $targetSize")
                        }
                ){
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<((@Composable () -> Unit)?)>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
    var rowFromIndex by mutableStateOf<Int?>(null)
    var rowToIndex by mutableStateOf<Int?>(null)
    var columnFromIndex by mutableStateOf<Any?>(null)
    var columnToIndex by mutableStateOf<Any?>(null)
}

data class ColumnPosition(
    var from: Any? = null,
    var to: Any? = null
) {
    fun canAdd() = from != to
}

data class RowPosition(
    var from: Int? = 0,
    var to: Int? = 0
) {
    fun canAdd() = from != to
}

open class ItemPosition(
    var rowPosition: RowPosition,
    var columnPosition: ColumnPosition
) {
    fun canAdd() = columnPosition.canAdd() //&& rowPosition.canAdd()
}