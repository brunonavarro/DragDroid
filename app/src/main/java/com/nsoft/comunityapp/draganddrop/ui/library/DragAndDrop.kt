package com.nsoft.comunityapp.draganddrop.ui.library

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
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
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
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
                        currentState.columnFromIndex = currentState.columnToIndex ?: columnIndex
                        currentState.rowFromIndex = currentState.rowToIndex ?: rowIndex
                        currentState.draggableComposable = content
                        Log.e("DRAG: ", "START currentState $currentState")

                        val rowPosition = RowPosition(from = currentState.rowFromIndex ?: rowIndex)
                        val columnPosition =
                            ColumnPosition(from = currentState.columnFromIndex ?: columnIndex)

                        mainViewModel.startDragging(
                            dataToDrop as PersonUIItem,
                            rowPosition,
                            columnPosition
                        )
                    },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                        Log.e("DRAG: ", "CURRENT currentState $currentState")
                    },
                    onDragEnd = {
                        currentState.dragOffset = Offset.Zero
                        currentState.columnFromIndex = currentState.columnToIndex ?: columnIndex
                        currentState.rowFromIndex = currentState.rowToIndex ?: rowIndex
                        currentState.isDragging = false
                        Log.e("DRAG: ", "END currentState $currentState")

                        val rowPosition = RowPosition(from = currentState.rowFromIndex ?: rowIndex)
                        val columnPosition =
                            ColumnPosition(from = currentState.columnFromIndex ?: columnIndex)

                        mainViewModel.endDragging(
                            dataToDrop as PersonUIItem,
                            rowPosition,
                            columnPosition
                        )
                    },
                    onDragCancel = {
                        currentState.dragOffset = Offset.Zero
                        currentState.columnFromIndex = currentState.columnToIndex ?: columnIndex
                        currentState.rowFromIndex = currentState.rowToIndex ?: rowIndex
                        currentState.isDragging = false
                        Log.e("DRAG: ", "CANCEL currentState $currentState")

                        val rowPosition = RowPosition(from = currentState.rowFromIndex ?: rowIndex)
                        val columnPosition =
                            ColumnPosition(from = currentState.columnFromIndex ?: columnIndex)

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

                    Log.e("ADD $columnIndex: ", "rect $rect")

                    Log.e("ADD $columnIndex: ", "dragPosition $dragPosition")
                    Log.e("ADD $columnIndex: ", "dragOffset $dragOffset")


                    isCurrentDropTarget = if (dragInfo.isDragging) {
                        rect.contains(dragPosition + dragOffset)
                    } else false

                    Log.e(
                        "ADD $columnIndex: ",
                        "onGloballyPositioned boundsInWindow $isCurrentDropTarget"
                    )
                }
            },

    ) {
        val data = if (isCurrentDropTarget && !dragInfo.isDragging) {
            dragInfo.rowToIndex = rowIndex
            dragInfo.columnToIndex = columnIndex
            dragInfo.dataToDrop as T?
        } else {
            null
        }

        Log.e("DROP $columnIndex: ", "dragInfo $dragInfo")
        Log.e("DROP $columnIndex: ", "data $data")
        Log.e("DROP $columnIndex: ", "isCurrentDropTarget $isCurrentDropTarget")
        Log.e("DROP $columnIndex: ", "index to $rowIndex - column to $columnIndex")
        val rowPosition = RowPosition(dragInfo.rowFromIndex, dragInfo.rowToIndex ?: rowIndex)
        val columnPosition = ColumnPosition(dragInfo.columnFromIndex, dragInfo.columnToIndex)

        Log.e(
            "ADD $columnIndex",
            "drop event -----------------------------------------------------"
        )
        Log.e("ADD $columnIndex", "drop event data $data")
        Log.e("ADD $columnIndex", "drop event rowPosition $rowPosition - $rowIndex")
        Log.e("ADD $columnIndex", "drop event columnPosition $columnPosition - $columnIndex")
        Log.e("ADD $columnIndex", "drop event isCurrentDropTarget $isCurrentDropTarget")

        content(
            isCurrentDropTarget && columnIndex != columnPosition.from,
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
    val from: Any? = null,
    val to: Any? = null
) {
    fun canAdd() = from != to
}

data class RowPosition(
    val from: Int? = 0,
    val to: Int? = 0
) {
    fun canAdd() = from != to
}

open class ItemPosition(
    var rowPosition: RowPosition,
    var columnPosition: ColumnPosition
) {
    fun canAdd() = columnPosition.canAdd()
}