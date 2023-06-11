package com.nsoft.comunityapp.dragdroid_kt.components

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
import com.nsoft.comunityapp.dragdroid_kt.entities.DragTargetInfo
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnPosition
import com.nsoft.comunityapp.dragdroid_kt.interfaces.RowPosition

val LocalDragTargetInfo = localDragTargetInfo<Any, Any>()

inline fun <reified T : Any, reified K> localDragTargetInfo(): ProvidableCompositionLocal<DragTargetInfo<T, K>> {
    return compositionLocalOf { DragTargetInfo() }
}

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
inline fun <reified T, reified K> DragTarget(
    modifier: Modifier = Modifier,
    rowIndex: Int,
    columnIndex: K,
    dataToDrop: T,
    vibrator: Vibrator?,
    crossinline onStart: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit?,
    crossinline onEnd: (item: T, rowPosition: RowPosition, columnPosition: ColumnPosition<K>) -> Unit?,
    noinline content: @Composable ((isDrag: Boolean, data: Any?) -> Unit)
) {
    var currentPosition by remember {
        mutableStateOf(Offset.Zero)
    }

    val currentState = (LocalDragTargetInfo).current

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

                        Log.e(
                            "CURRENT COLUMN",
                            " currentState.currentColumnDrag ${currentState.currentColumnDrag} columnIndex  ${columnIndex}"
                        )
                        currentState.currentColumnDrag = columnIndex
                        Log.e(
                            "CURRENT COLUMN",
                            " currentState.currentColumnDrag ${currentState.currentColumnDrag} columnIndex  ${columnIndex}"
                        )
                        currentState.dataToDrop = dataToDrop
                        currentState.isDragging = true
                        currentState.dragPosition = currentPosition + it

                        currentState.columnPosition.from = columnIndex
                        currentState.rowPosition.from = rowIndex

                        currentState.draggableComposable =
                            content //as @Composable ((Boolean, Any?) -> Unit)?


                        onStart(
                            dataToDrop,
                            currentState.rowPosition,
                            currentState.columnPosition as ColumnPosition<K>
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
                            currentState.columnPosition as ColumnPosition<K>
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
                            currentState.columnPosition as ColumnPosition<K>
                        )

                    }
                )
            }
    ) {
        content(currentState.isDragging, currentState.dataToDrop as T?)
    }
}


/**ITEM QUE SOPORTA EL SOLTAR ITEM EN SU INTERIOR**/
@Composable
inline fun <reified T, reified K> DropItem(
    modifier: Modifier,
    rowIndex: Int,
    columnIndex: K,
    content: @Composable() (BoxScope.(isInBound: Boolean, data: T?, rows: RowPosition, column: ColumnPosition<K>) -> Unit)
) {
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
            data, dragInfo.rowPosition, dragInfo.columnPosition as ColumnPosition<K>
        )
    }
}


/**ITEM QUE SOPORTA EL SOLTAR ITEM EN SU INTERIOR**/
@Composable
inline fun <reified T, reified K> DropItemMain(
    modifier: Modifier,
    columnIndex: K,
    content: @Composable() (BoxScope.(isInBound: Boolean, data: T?, rows: RowPosition, column: ColumnPosition<K>, isDrag: Boolean) -> Unit)
) {
    val dragInfo = LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset
    val column = columnIndex

    dragInfo.currentColumnDrog = columnIndex

    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }

    var bound by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .then(Modifier.onGloballyPositioned {
                it.boundsInWindow().let { rect ->
                    if (dragInfo.isDragging) {
                        bound = rect.contains(dragPosition + dragOffset)
                    }
                }
            })
    ) {

        val data =
            if (bound && dragInfo.currentColumnDrag != dragInfo.currentColumnDrog && !dragInfo.isDragging) {
                Log.e(
                    "CURRENT COLUMN",
                    " dragInfo.currentColumnDrag ${dragInfo.currentColumnDrag} dragInfo.currentColumnDrog  ${dragInfo.currentColumnDrog} - columnIndex $columnIndex"
                )
                dragInfo.currentColumnDrag = columnIndex
                Log.e(
                    "CURRENT COLUMN",
                    " dragInfo.currentColumnDrag ${dragInfo.currentColumnDrag} dragInfo.currentColumnDrog  ${dragInfo.currentColumnDrog} - columnIndex $columnIndex"
                )
                dragInfo.columnPosition.to = columnIndex
                dragInfo.dataToDrop as T?
            } else {
                null
            }

        isCurrentDropTarget = bound && dragInfo.columnPosition.from != columnIndex

        content(
            isCurrentDropTarget,
            data,
            dragInfo.rowPosition,
            dragInfo.columnPosition as ColumnPosition<K>,
            dragInfo.isDragging
        )
    }
}

/**DragableScreen no acepta <T,K>
 * para remember en su lugar se deja como Any
 * para acercarlo lo mas posible a generico: only cast to T or K
 * */
@Composable
fun DraggableScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember {
        /** Unicamente funciona con Any */
        DragTargetInfo<Any, Any>()
    }

    CompositionLocalProvider(
        LocalDragTargetInfo provides state
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            content()
            if (state.isDragging) {
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
                ) {
                    state.draggableComposable?.invoke(false, null)
                }
            }
        }
    }
}