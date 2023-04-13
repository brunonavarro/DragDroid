package com.nsoft.comunityapp.draganddrop.ui.library

import android.content.Context
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

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }


/**
 * Clase exclusiva de Libreria
 * * Construye el DropComponent
 * * Construye el DragComponent
 * * Animation Drag and Drop Component
 * * Generic Entity Data Class
 * **/

/**Movimiento de componente**/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun <T> DragTarget(
    modifier: Modifier = Modifier,
    rowIndex: Int,
    columnIndex: Any,
    dataToDrop: T,
    vibrator: Vibrator?,
    onStart: (
        item: T,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition
    ) -> Unit,
    onEnd: (
        item: T,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition
    ) -> Unit,
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

                        currentState.columnPosition.from = columnIndex
                        currentState.rowPosition.from = rowIndex

                        currentState.draggableComposable = content

                        onStart(
                            dataToDrop,
                            currentState.rowPosition,
                            currentState.columnPosition
                        )
                    },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)

                        currentState.columnPosition.from = columnIndex
                        currentState.rowPosition.from = rowIndex
                    },
                    onDragEnd = {
                        currentState.dragOffset = Offset.Zero
                        currentState.columnPosition.from = columnIndex
                        currentState.rowPosition.from = rowIndex
                        currentState.isDragging = false

                        onEnd(
                            dataToDrop,
                            currentState.rowPosition,
                            currentState.columnPosition
                        )
                    },
                    onDragCancel = {
                        currentState.dragOffset = Offset.Zero
                        currentState.columnPosition.from = columnIndex
                        currentState.rowPosition.from = rowIndex
                        currentState.isDragging = false

                        onEnd(
                            dataToDrop,
                            currentState.rowPosition,
                            currentState.columnPosition
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
            .then(Modifier.onGloballyPositioned {
                it.boundsInWindow().let { rect ->
                    isCurrentDropTarget = if (dragInfo.isDragging) {
                        rect.contains(dragPosition + dragOffset)
                    } else false
                }
            })
    ) {
        val data = if (isCurrentDropTarget && !dragInfo.isDragging) {
            dragInfo.rowPosition.to = rowIndex
            dragInfo.columnPosition.to = columnIndex
            dragInfo.dataToDrop as T?
        } else {
            null
        }

        content(
            isCurrentDropTarget && dragInfo.columnPosition.canAdd() && data != null,
            data, dragInfo.rowPosition, dragInfo.columnPosition
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
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
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

    var columnPosition by mutableStateOf(ColumnPosition())
    var rowPosition by mutableStateOf(RowPosition())

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

data class CustomComposableParams(
    val context: Context,
    val screenWidth: Int? = null,
    val screenHeight: Int? = null,
    val elevation: Int = 0,
    val modifier: Modifier = Modifier,
    val idColumn: Any? = null,
    val rowList: List<Any>? = null,
    val onStart: ((item: Any, rowPosition: RowPosition, columnPosition: ColumnPosition) -> Unit)? = null,
    val onEnd: ((item: Any, rowPosition: RowPosition, columnPosition: ColumnPosition) -> Unit)? = null
)