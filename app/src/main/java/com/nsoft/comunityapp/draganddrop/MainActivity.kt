package com.nsoft.comunityapp.draganddrop

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.components.DragDropScreen
import com.nsoft.comunityapp.draganddrop.ui.library.DraggableScreen
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme

class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragAndDropTheme {
                // A surface container using the 'background' color from the theme
                DraggableScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(0.8f))
                ) {
                    DragDropScreen(mainViewModel = mainViewModel, context = applicationContext)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DragAndDropTheme {
        Greeting("Android")
    }
}