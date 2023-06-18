## DragAndDropCompose 🚀 <img src="https://media.giphy.com/media/hvRJCLFzcasrR4ia7z/giphy.gif" width="30px">

Este es un proyecto de Libreria en desarrollo

![Descripción de la imagen](https://github.com/brunonavarro/DragDroid/BoardColumn.gif)
![Descripción de la imagen](https://github.com/brunonavarro/DragDroid/Single.gif)

## Implementacion de Dependencia
```gradle
implementation 'com.github.brunonavarro:DragDroid:1.0.0'
```
Agrega en build de proyecto:
```gradle
repositories {
    ....
    maven{
        url 'https://jitpack.io'
    }
}
```
##Eventos Drag y Drop con diseño de filas y columnas.

Para añadir el componente DragDropScreen y ColumnDropCard compatible con Jetpack Compose
debemos agregar el siguiente codigo en una funcion @Composable enlazado y lo invocamos en el Activity/Fragment.

```kotlin
@Composable
fun BoardScreen(
    applicationContext: Context,
    mainViewModel: MainViewModel
) {
    val groupTasks =
        mainViewModel.taskItems.filter { !it.isDraggable }.groupBy { it.column }


    val isLoading by remember {
        mutableStateOf(false)
    }

    var stateParams by remember {
        mutableStateOf<ColumnParameters.StyleParams<DragItem, Column>?>(null)
    }
    Column {
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
                    loadingParams = ColumnParameters.LoadingParams(
                        isLoading = isLoading,
                        colorStroke = Color.Black,
                        strokeWidth = 5.dp
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
```
```kotlin
class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ...
            DragAndDropTheme {
                BoardScreen(
                    applicationContext = applicationContext,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}
```
##Eventos Drag y Drop para uso independiente y/o personalizado.

Se debe añadir los componentes DraggableScreen, DragItem y DropItem compatible con Jetpack Compose
debemos agregar el siguiente codigo en una funcion @Composable enlazado y lo invocamos en el Activity/Fragment.
```kotlin
@Composable
fun SingleDragDropScreen(
    applicationContext: Context,
    mainViewModel: MainViewModel
) {
    val count = remember(key1 = mainViewModel.dropCount.value) {
        mainViewModel.dropCount.value
    }

    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(bottom = 20.dp)
        ) {
            val data = DragItem("DRAG", id = "01", backgroundColor = Color.Cyan)

            DraggableScreen(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(0.8f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DragItem<DragItem>(
                        modifier = Modifier
                            .size(200.dp),
                        dataToDrop = data,
                        vibrator = null,
                        onStart = { mainViewModel.startCardDragging(it) },
                        onEnd = { mainViewModel.endCardDragging(it) }
                    ) { it, dataMoved ->
                        Box(
                            Modifier
                                .size(150.dp)
                                .shadow(6.dp, shape = RoundedCornerShape(10.dp))
                                .background(Color.Cyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = data.name)
                        }
                    }

                    DropItem<DragItem>(
                        modifier = Modifier
                            .size(200.dp)
                    ) { isBound, data ->
                        LaunchedEffect(
                            key1 = data != null,
                            key2 = isBound
                        ) {
                            if (isBound && data != null) {
                                mainViewModel.updateDroppedList(
                                    data
                                )
                            }
                        }

                        if (isBound) {
                            Box(
                                Modifier
                                    .size(150.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(10.dp))
                                    .border(
                                        1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(Color.Cyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "DROP HEAR!!...")
                            }
                        } else {
                            Box(
                                Modifier
                                    .size(150.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(10.dp))
                                    .background(Color.Cyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "DROP HEAR!!...")
                            }
                        }
                    }
                }
            }

        }

        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

        Box(
            modifier = Modifier.align(CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Drop Counter", fontSize = 16.sp)
                Text(
                    text = "$count",
                    fontSize = 30.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
            }
        }
    }
}
```
```kotlin
class MainActivity : ComponentActivity() {

    val mainViewModel = MainViewModel()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ...
            DragAndDropTheme {
                SingleDragDropScreen(
                    applicationContext = applicationContext,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}
```
