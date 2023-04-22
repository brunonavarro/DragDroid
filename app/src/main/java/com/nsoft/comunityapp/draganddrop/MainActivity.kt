package com.nsoft.comunityapp.draganddrop

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nsoft.comunityapp.draganddrop.ui.ColumnCard
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.Params
import com.nsoft.comunityapp.draganddrop.ui.components.DragDropScreen
import com.nsoft.comunityapp.draganddrop.ui.entities.COLUMN
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.draganddrop.ui.library.DraggableScreen
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme

class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val rowListByGroup = mainViewModel.taskItems.groupBy { it.column }

            DragAndDropTheme {
                // A surface container using the 'background' color from the theme
                DraggableScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(0.8f))
                ) {
                    DragDropScreen<DragItem>(
                        context = applicationContext,
                        columnsItems = mainViewModel.columnsItems,
                        rowListByGroup = rowListByGroup,
                        onStart = { item, row, column ->
                            mainViewModel.startDragging(
                                item,
                                rowPosition = row,
                                columnPosition = column
                            )
                        },
                        onEnd = { item, row, column ->
                            mainViewModel.endDragging(
                                item,
                                rowPosition = row,
                                columnPosition = column
                            )
                        },
                        updateBoard = { item, row, column ->
                            mainViewModel.addPersons(item, row, column)
                        }
                    ) { params ->
                        ColumnCard(
                            Params.CustomParams(
                                context = params.context, screenHeight = params.screenHeight,
                                screenWidth = params.screenWidth, elevation = params.elevation,
                                modifier = params.modifier, idColumn = params.idColumn as COLUMN,
                                rowList = params.rowList,
                                onStart = { item, row, column ->
                                    params.onStart
                                },
                                onEnd = { item, row, column ->
                                    params.onEnd
                                }
                            )
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