package com.nsoft.comunityapp.draganddrop

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.nsoft.comunityapp.draganddrop.ui.CustomDragCard
import com.nsoft.comunityapp.draganddrop.ui.CustomHeaderColumn
import com.nsoft.comunityapp.draganddrop.ui.MainViewModel
import com.nsoft.comunityapp.draganddrop.ui.entities.Column
import com.nsoft.comunityapp.draganddrop.ui.entities.DragItem
import com.nsoft.comunityapp.draganddrop.ui.theme.DragAndDropTheme
import com.nsoft.comunityapp.dragdroid_kt.components.ColumnDropCard
import com.nsoft.comunityapp.dragdroid_kt.components.DragDropScreen
import com.nsoft.comunityapp.dragdroid_kt.interfaces.ColumnParameters

class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    /** Aqui se declara el tipo de data que procesará la libreria*/

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val groupTasks =
                mainViewModel.taskItems.filter { !it.isDraggable }.groupBy { it.column }

            var stateParams by remember {
                mutableStateOf<ColumnParameters.StyleParams<DragItem, Column>?>(null)
            }
            DragAndDropTheme {
                // A surface container using the 'background' color from the theme
                Column() {

                    DragDropScreen(
                        context = applicationContext,
                        columnsItems = mainViewModel.columnsItems,
                        groupTasks = groupTasks,
                        updateBoard = mainViewModel::updateTasks,
                        stateListener = { stateParams = it }
                    ) {
                        stateParams?.let { styleParams ->
                            ColumnDropCard(
                                params = styleParams,
                                actionParams = ColumnParameters.ActionParams(
                                    onStart = mainViewModel::startDragging,
                                    onEnd = mainViewModel::endDragging
                                ),
                                key = { item -> item.getKey() },
                                header = {
                                    CustomHeaderColumn(
                                        params = styleParams
                                    )
                                },
                                body = { data ->
                                    CustomDragCard(
                                        data = data,
                                        styleParams = styleParams,
                                        actionParams = ColumnParameters.ActionParams(
                                            onClick = { item ->
                                                Toast.makeText(
                                                    stateParams?.context,
                                                    "CLICK CARD : ${item.name}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    )
                                }
                            )
                        }
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