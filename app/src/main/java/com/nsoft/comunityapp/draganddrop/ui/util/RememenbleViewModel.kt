package com.nsoft.comunityapp.draganddrop.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel

@Composable
fun rememberMainModel():MainViewModel{

    val state = remember {
        MainViewModel()
    }

    DisposableEffect(Unit){
        onDispose {
            state.onDestroy()
        }
    }

    return state
}