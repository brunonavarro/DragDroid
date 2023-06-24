## DragAndDropCompose 🚀 <img src="https://media.giphy.com/media/hvRJCLFzcasrR4ia7z/giphy.gif" width="30px">

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![](https://jitpack.io/v/brunonavarro/DragDroid.svg)](https://jitpack.io/#brunonavarro/DragDroid)
![GitHub all releases](https://img.shields.io/github/downloads/brunonavarro/DragDroid/total)
![GitHub release (by tag)](https://img.shields.io/github/downloads/brunonavarro/DragDroid/1.0.0/total)


[![issues](https://img.shields.io/github/issues/brunonavarro/DragDroid?style=for-the-badge)](https://github.com/brunonavarro/DragDroid/issues)
[![pull requests](https://img.shields.io/github/issues-pr/brunonavarro/DragDroid?style=for-the-badge)](https://github.com/brunonavarro/DragDroid/pulls)
[![contributors](https://img.shields.io/github/contributors/brunonavarro/DragDroid?style=for-the-badge)](https://github.com/brunonavarro/DragDroid/graphs/contributors)


##### Este es un proyecto de Libreria enfocado a soluciones de arrastre y soltar.
##### Inspirado en: https://github.com/MatthiasKerat/DragAndDropYT

<table>
    <tr>
<td><img src="https://github.com/brunonavarro/DragDroid/blob/master/BoardColumn.gif" width="300" height="500" /></td>

<td><img src="https://github.com/brunonavarro/DragDroid/blob/master/Single.gif" width="300" height="500" /></td>
    </tr>
</table>
    
## Implementacion de Dependencia
```gradle
/**Last Version*/
def version = '1.0.0'
```
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
##### Eventos Drag y Drop con diseño de filas y columnas.

Para añadir el componente DragDropScreen y ColumnDropCard compatible con Jetpack Compose
debemos agregar el siguiente codigo en una funcion @Composable enlazado y lo invocamos en el Activity/Fragment.

```kotlin
/**
 * Composable Screen of dashboard for administration of tasks.
 * @see updateTasks
 * @see startDragging
 * @see endDragging
 * */
@Composable
fun BoardScreen(
    rowColumnData: RowColumnData
) {
    val groupTasks =
        rowColumnData.taskItems.filter { !it.isDraggable }.groupBy { it.column }


    val isLoading by remember {
        mutableStateOf(false)
    }

    var stateParams by remember {
        mutableStateOf<ColumnParameters.StyleParams<DragItem, Column>?>(null)
    }
    Column {
        DragDropScreen(
            context = LocalContext.current,
            columnsItems = rowColumnData.columnsItems,
            groupTasks = groupTasks,
            updateBoard = rowColumnData.updateTasks,
            stateListener = { stateParams = it }
        ) {
            stateParams?.let { styleParams ->
                ColumnDropCard(
                    params = styleParams,
                    actionParams = ColumnParameters.ActionParams(
                        onStart = rowColumnData.startDragging,
                        onEnd = rowColumnData.endDragging
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

    var columnsItems = mutableStateListOf<Column>()
        private set

    var taskItems = mutableStateListOf<DragItem>()
        private set

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        val rowColumnData = RowColumnData(
            columnsItems = columnsItems,
            taskItems = taskItems,
            updateTasks = ::updateTasks,
            startDragging = ::startDragging,
            endDragging = ::endDragging
        )
        setContent {
            ...
            DragAndDropTheme {
                BoardScreen(
                    rowColumnData = rowColumnData
                )
            }
        }
    }

    private fun initData(){
        /**Columns Data*/
        columnsItems.add(Column.TO_DO)
        columnsItems.add(Column.IN_PROGRESS)
        columnsItems.add(Column.DEV_DONE)

        /**Task Data*/
        taskItems.add(
            DragItem(
                "Task",
                id = "A1",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(
            DragItem(
                "Task",
                id = "B2",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "D1", Color.DarkGray))
        taskItems.add(
            DragItem(
                "Task",
                id = "B3",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "B4", Color.DarkGray))
        taskItems.add(
            DragItem(
                "Task",
                id = "B5",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "B6", Color.DarkGray))
        taskItems.add(
            DragItem(
                "Task",
                id = "B7",
                backgroundColor = Color.DarkGray,
            )
        )
        taskItems.add(DragItem("Task", "B8", Color.DarkGray))
    }

    /**FUNCIONES PARA USAR en
     * @see BoardScreen*/
    fun startDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        taskItems.firstOrNull { it == item }?.apply {
            isDraggable = true
        }

        Log.e("START DRAG", item.name)
    }

    fun endDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        taskItems.firstOrNull { it == item }?.apply {
            isDraggable = false
        }

        Log.e("END DRAG", item.name)
    }

    fun updateTasks(
        item: DragItem,
        rowPosition: RowPosition,
        columnPosition: ColumnPosition<Column>
    ) {
        taskItems.reOrderList(item)?.let {
            it.updateItem(columnPosition)
            it.backgroundColor = when (columnPosition.to) {
                Column.TO_DO -> {
                    Color.DarkGray
                }
                Column.IN_PROGRESS -> {
                    Color.Blue
                }
                Column.DEV_DONE -> {
                    Color.Green
                }
                else -> {
                    item.backgroundColor
                }
            }
        }
    }
}
```
```kotlin
data class RowColumnData(
    val columnsItems: List<Column>,
    val taskItems: List<DragItem>,
    val updateTasks: (DragItem, RowPosition, ColumnPosition<Column>) -> Unit,
    val startDragging: (DragItem, RowPosition, ColumnPosition<Column>) -> Unit,
    val endDragging: (DragItem, RowPosition, ColumnPosition<Column>) -> Unit
)
```
##### Eventos Drag y Drop para uso independiente y/o personalizado.

Se debe añadir los componentes DraggableScreen, DragItem y DropItem compatible con Jetpack Compose
debemos agregar el siguiente codigo en una funcion @Composable enlazado y lo invocamos en el Activity/Fragment.
```kotlin
/**
 * composable for custom functional design.
 * @see updateDroppedList
 * @see startCardDragging
 * @see endCardDragging
 * */
@Composable
fun SingleDragDropScreen(
    simpleData: SimpleData
) {

    val count = remember(key1 = simpleData.dropCount.value) {
        simpleData.dropCount.value
    }

    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(bottom = 20.dp)
        ) {
            val data = DragItem("DRAG", id = "01", backgroundColor = Color.Cyan)

            /** Composable for contains your Drag item and drop
             * @see DraggableScreen
             * */
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
                    /** Composable for event Drag your item
                     * @see DragItem
                     * */
                    DragItem<DragItem>(
                        modifier = Modifier
                            .size(200.dp),
                        dataToDrop = data,
                        vibrator = null,
                        onStart = { simpleData.startCardDragging(it) },
                        onEnd = { simpleData.endCardDragging(it) }
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

                    /** Drop your item
                     * @see DropItem
                     * */
                    DropItem<DragItem>(
                        modifier = Modifier
                            .size(200.dp)
                    ) { isBound, data ->
                        LaunchedEffect(
                            key1 = data != null,
                            key2 = isBound
                        ) {
                            if (isBound && data != null) {
                                simpleData.updateDroppedList(
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

    val dropCount = mutableStateOf<Int>(0)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val simpleData = SimpleData(
            dropCount = dropCount,
            updateDroppedList = ::updateDroppedList,
            startCardDragging = ::startCardDragging,
            endCardDragging = ::endCardDragging
        )
        setContent {
            ...
            DragAndDropTheme {
                SingleDragDropScreen(
                    simpleData = simpleData
                )
            }
        }
    }

    /**FUNCIONES PARA USAR en
     * @see SimpleDragDropScreen*/
    fun updateDroppedList(
        item: DragItem
    ) {
        dropCount.value++
    }

    fun startCardDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {

        Log.e("START DRAG", item.name)
    }

    fun endCardDragging(
        item: DragItem,
        rowPosition: RowPosition? = null,
        columnPosition: ColumnPosition<Column>? = null
    ) {
        Log.e("END DRAG", item.name)
    }
}
```
```kotlin
data class SimpleData(
    val dropCount: MutableState<Int>,
    val updateDroppedList: (DragItem) -> Unit,
    val startCardDragging: (DragItem) -> Unit,
    val endCardDragging: (DragItem) -> Unit
)
```
