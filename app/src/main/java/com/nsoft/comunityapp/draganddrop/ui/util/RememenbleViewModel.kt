package com.nsoft.comunityapp.draganddrop.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.ItemUI

@Composable
inline fun <reified T:ItemUI<T>> rememberMainModel(): MainViewModel<T> {

    val state = remember {
        MainViewModel<T>()
    }

    DisposableEffect(Unit) {
        onDispose {
            state.onDestroy()
        }
    }

    return state
}