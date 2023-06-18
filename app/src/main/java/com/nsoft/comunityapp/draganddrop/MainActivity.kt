package com.nsoft.comunityapp.draganddrop

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nsoft.comunityapp.draganddrop.ui.BoardScreen
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.SingleDragDropScreen
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme

class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    /** Aqui se declara el tipo de data que procesará la libreria*/

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    DragAndDropTheme {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(modifier = Modifier.fillMaxWidth(80f),
                                onClick = {
                                    navController.navigate("board")
                                }) {
                                Text(text = "BOAR COLUMN DRAG AND DROP")
                            }
                            Button(modifier = Modifier.fillMaxWidth(80f),
                                onClick = {
                                    navController.navigate("singleDragDrop")
                                }) {
                                Text(text = "SINGLE DRAG AND DROP")
                            }
                        }
                    }
                }
                composable("board") {
                    DragAndDropTheme {
                        BoardScreen(
                            applicationContext = applicationContext,
                            mainViewModel = mainViewModel
                        )
                    }
                }
                composable("singleDragDrop") {
                    DragAndDropTheme {
                        SingleDragDropScreen(
                            applicationContext = applicationContext,
                            mainViewModel = mainViewModel
                        )
                    }
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