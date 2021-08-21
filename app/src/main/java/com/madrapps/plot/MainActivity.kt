package com.madrapps.plot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.madrapps.plot.LinePlot.AreaUnderLine
import com.madrapps.plot.LinePlot.Connection
import com.madrapps.plot.LinePlot.Grid
import com.madrapps.plot.LinePlot.Intersection
import com.madrapps.plot.LinePlot.Line
import com.madrapps.plot.ui.theme.PlotTheme

private val dataPoints1 = listOf(
    DataPoint(0f, 0f),
    DataPoint(1f, 0f),
    DataPoint(2f, 0f),
    DataPoint(3f, 0f),
    DataPoint(4f, 0f),
    DataPoint(5f, 25f),
    DataPoint(6f, 75f),
    DataPoint(7f, 100f),
    DataPoint(8f, 80f),
    DataPoint(9f, 75f),
    DataPoint(10f, 55f),
    DataPoint(11f, 45f),
    DataPoint(12f, 50f),
    DataPoint(13f, 80f),
    DataPoint(14f, 70f),
    DataPoint(15f, 25f),
    DataPoint(16f, 0f), // FIXME :Bug: Change this to 200f. Column doesn't adapt.
    DataPoint(17f, 0f),
    DataPoint(18f, 35f),
    DataPoint(19f, 60f),
    DataPoint(20f, 20f),
    DataPoint(21f, 40f),
    DataPoint(22f, 75f),
    DataPoint(23f, 50f),
//    DataPoint(27f, 20f), // FIXME :Bug: Add these. Row doesn't react to extra values
//    DataPoint(33f, 80f),
)

private val dataPoints2 = listOf(
    DataPoint(0f, 0f),
    DataPoint(1f, 0f),
    DataPoint(2f, 25f),
    DataPoint(3f, 75f),
    DataPoint(4f, 100f),
    DataPoint(5f, 80f),
    DataPoint(6f, 75f),
    DataPoint(7f, 50f),
    DataPoint(8f, 80f),
    DataPoint(9f, 70f),
    DataPoint(10f, 0f),
    DataPoint(11f, 0f),
    DataPoint(12f, 45f),
    DataPoint(13f, 20f),
    DataPoint(14f, 40f),
    DataPoint(15f, 75f),
    DataPoint(16f, 50f),
    DataPoint(17f, 75f),
    DataPoint(18f, 40f),
    DataPoint(19f, 20f),
    DataPoint(20f, 0f),
    DataPoint(21f, 0f),
    DataPoint(22f, 50f),
    DataPoint(23f, 25f),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlotTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = MaterialTheme.colors.background
                ) {
                    LineGraph(
                        LinePlot(
                            listOf(
                                Line(
                                    dataPoints1,
                                    Connection(Color.Blue, 3.dp),
                                    Intersection(Color.Blue, 6.dp),
                                    Intersection(Color.Red, 12.dp),
                                    AreaUnderLine(Color.Blue, 0.1f)
                                ),
                                Line(
                                    dataPoints2,
                                    Connection(Color.Gray, 2.dp),
                                    Intersection { center ->
                                        val px = 4.dp.toPx()
                                        val topLeft = Offset(center.x - px, center.y - px)
                                        drawRect(Color.Gray, topLeft, Size(px * 2, px * 2))
                                    },
                                    Intersection { center ->
                                        val px = 4.dp.toPx()
                                        val topLeft = Offset(center.x - px, center.y - px)
                                        drawRect(Color.Red, topLeft, Size(px * 2, px * 2))
                                    },
                                ),
                            ), Grid(Color.Gray)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LineGraph(plot: LinePlot) {
    val lines = plot.lines

    // Graph Line properties
    val pointRadius = 6.dp

    // Overall Graph properties
    val paddingRight = 16.dp
    val globalXScale = 1f
    val globalYScale = 1f
    val xAxisText: String? = "Time (in hours)"

    val isZoomAllowed = true
    val isDragAllowed = true
    val detectDragTimeOut = 100L

    val offset = remember { mutableStateOf(0f) }
    val maxScrollOffset = remember { mutableStateOf(0f) }
    val dragOffset = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val xZoom = remember { mutableStateOf(globalXScale) }
    val rowHeight = remember { mutableStateOf(0f) }
    val columnWidth = remember { mutableStateOf(0f) }
    val bgColor = MaterialTheme.colors.surface

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Canvas(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(bgColor)
                .scrollable(
                    state = rememberScrollableState { delta ->
                        offset.value -= delta
                        if (offset.value < 0f) offset.value = 0f
                        if (offset.value > maxScrollOffset.value) {
                            offset.value = maxScrollOffset.value
                        }
                        delta
                    }, Orientation.Horizontal, enabled = true,
                    interactionSource = MutableInteractionSource()
                )
                .pointerInput(Unit, Unit) {
                    detectDragZoomGesture(
                        isZoomAllowed = isZoomAllowed,
                        isDragAllowed = isDragAllowed,
                        detectDragTimeOut = detectDragTimeOut,
                        onDragStart = {
                            dragOffset.value = it.x
                            isDragging.value = true
                            Log.d("RONNY", "DragStart = $it")
                        }, onDragEnd = {
                            isDragging.value = false
                        }, onZoom = { zoom ->
                            xZoom.value *= zoom
                        }
                    ) { change, _ ->
                        dragOffset.value = change.position.x
                        Log.d("RONNY", "DragMove = ${change.position}")
                    }
                },
                onDraw = {
                    val xLeft = columnWidth.value
                    val yBottom = size.height - rowHeight.value
                    val xOffset = 20.dp.toPx() * xZoom.value
                    val allDataPoints = lines.flatMap { it.dataPoints }
                    val yOffset =
                        ((yBottom - pointRadius.toPx()) / allDataPoints.maxOf { it.y }) * globalYScale

                    val xLastPoint = allDataPoints.maxOf { it.x } * xOffset + xLeft
                    maxScrollOffset.value = if (xLastPoint > size.width) {
                        xLastPoint - size.width + paddingRight.toPx() + pointRadius.toPx()
                    } else 0f

                    val dragLocks = mutableMapOf<Line, Offset>()

                    // Draw Grid lines
                    val region =
                        Rect(xLeft, pointRadius.toPx(), size.width - paddingRight.toPx(), yBottom)
                    plot.grid?.draw?.invoke(this, region, xOffset, yOffset)

                    // Draw Lines and Points and AreaUnderLine
                    lines.forEach { line ->
                        val intersection = line.intersection
                        val connection = line.connection
                        val areaUnderLine = line.areaUnderLine

                        // Draw area under curve
                        if (areaUnderLine != null) {
                            val points = line.dataPoints.map { (x, y) ->
                                val x1 = (x * xOffset) + xLeft - offset.value
                                val y1 = yBottom - (y * yOffset)
                                Offset(x1, y1)
                            }
                            val p = Path()
                            points.forEachIndexed { index, offset ->
                                if (index == 0) {
                                    p.moveTo(offset.x, offset.y)
                                } else {
                                    p.lineTo(offset.x, offset.y)
                                }
                            }
                            val last = points.last()
                            val first = points.first()
                            p.lineTo(last.x, first.y)
                            areaUnderLine.draw(this, p)
                        }

                        // Draw Lines and Points
                        var curOffset: Offset? = null
                        var nextOffset: Offset? = null
                        line.dataPoints.forEachIndexed { i, _ ->
                            if (i == 0) {
                                val (x, y) = line.dataPoints[i]
                                val x1 = (x * xOffset) + xLeft - offset.value
                                val y1 = yBottom - (y * yOffset)
                                curOffset = Offset(x1, y1)
                            }
                            if (line.dataPoints.indices.contains(i + 1)) {
                                val (x, y) = line.dataPoints[i + 1]
                                val x2 = (x * xOffset) + xLeft - offset.value
                                val y2 = yBottom - (y * yOffset)
                                nextOffset = Offset(x2, y2)
                            }
                            if (nextOffset != null && curOffset != null) {
                                connection?.draw?.invoke(
                                    this,
                                    curOffset!!,
                                    nextOffset!!
                                )
                            }
                            curOffset?.let {
                                if (isDragging.value && (dragOffset.value) > it.x - xOffset / 2 && (dragOffset.value) < it.x + xOffset / 2) {
                                    dragLocks[line] = it
                                } else {
                                    intersection?.draw?.invoke(this, it)
                                }
                            }
                            curOffset = nextOffset
                        }
                    }

                    // Draw column
                    drawRect(
                        bgColor,
                        Offset(0f, 0f),
                        Size(xLeft - pointRadius.toPx(), size.height)
                    )

                    // Draw right padding
                    drawRect(
                        bgColor,
                        Offset(size.width - paddingRight.toPx(), 0f),
                        Size(paddingRight.toPx(), size.height)
                    )

                    // Draw drag selection Highlight
                    if (isDragging.value) {
                        // Draw Drag Line highlight
                        dragLocks.values.firstOrNull()?.let { (x, _) ->
                            if (x >= xLeft - pointRadius.toPx() && x <= size.width - paddingRight.toPx()) {
                                plot.dragSelection?.draw?.invoke(
                                    this,
                                    Offset(x, yBottom),
                                    Offset(x, 0f)
                                )
                            }
                        }
                        // Draw Point Highlight
                        dragLocks.entries.forEach { (line, lock) ->
                            val highlight = line.highlight
                            val x = lock.x
                            if (x >= xLeft - pointRadius.toPx() && x <= size.width - paddingRight.toPx()) {
                                highlight?.draw?.invoke(this, lock)
                            }
                        }
                    }
                })
            GraphColumn(Modifier
                .align(Alignment.TopStart)
                .fillMaxHeight()
                .wrapContentWidth()
                .onGloballyPositioned {
                    columnWidth.value = it.size.width.toFloat()
                }
                .padding(start = 16.dp, end = 8.dp),
                paddingTop = pointRadius.value * LocalDensity.current.density,
                paddingBottom = rowHeight.value,
                globalYScale,
                values = {
                    (0..4).map {
                        val v = it * 25f
                        Value(v.toInt().toString(), v)
                    }
                }
            )

            val values = {
                (0..2300).map {
                    val v = it.toFloat()
                    Value(v.toInt().toString(), v)
                }
            }
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(
                        RowClip(
                            columnWidth.value - pointRadius.value * LocalDensity.current.density,
                            paddingRight
                        )
                    )
                    .onGloballyPositioned {
                        rowHeight.value = it.size.height.toFloat()
                    }
                    .padding(bottom = 8.dp, top = 8.dp)
            ) {
                GraphRow(
                    Modifier,
                    columnWidth.value,
                    offset.value,
                    xZoom.value,
                    values = values,
                    stepSize = 20.dp
                ) {
                    values().forEach { (text, i) ->
                        val color = MaterialTheme.colors.onSurface
                        val density = LocalDensity.current.density

                        // FIXME Only create composable that can be rendered on screen
                        if (i >= 0f && i < 50f) {
                            Column {
                                val isMajor = i.toInt() % 4 == 0
                                val radius = if (isMajor) 6f else 3f
                                Canvas(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .height(20.dp),
                                    onDraw = {
                                        drawCircle(
                                            color = color,
                                            radius * density,
                                            Offset(0f, 10f * density)
                                        )
                                    })
                                if (isMajor) {
                                    Text(
                                        text = text,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                }
                            }

                        }
                    }
                }
                if (xAxisText != null) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp),
                        text = xAxisText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlotTheme {
        LineGraph(
            LinePlot(
                listOf(
                    Line(
                        dataPoints1,
                        Connection(Color.Blue, 3.dp),
                        Intersection(Color.Blue, 6.dp),
                        Intersection(Color.Red, 4.dp),
                        AreaUnderLine(Color.Blue, 0.1f)
                    ),
                    Line(
                        dataPoints2,
                        Connection(Color.Gray, 2.dp),
                        Intersection(Color.Gray, 3.dp),
                        Intersection(Color.Gray, 3.dp)
                    ),
                )
            )
        )
    }
}
