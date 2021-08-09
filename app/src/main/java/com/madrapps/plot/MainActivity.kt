package com.madrapps.plot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.madrapps.plot.ui.theme.Grey50
import com.madrapps.plot.ui.theme.PlotTheme

private val dataPoints = listOf(
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
    DataPoint(16f, 0f),
    DataPoint(17f, 0f),
    DataPoint(18f, 35f),
    DataPoint(19f, 60f),
    DataPoint(20f, 20f),
    DataPoint(21f, 40f),
    DataPoint(22f, 75f),
    DataPoint(23f, 50f),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlotTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LineGraph(dataPoints)
                }
            }
        }
    }
}

@Composable
fun LineGraph(dataPoints: List<DataPoint>) {
    val pointRadius = 6.dp
    val lineWidth = 3.dp
    val offset = remember { mutableStateOf(0f) }
    val maxScrollOffset = remember { mutableStateOf(0f) }
    val dragOffset = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val xZoom = remember {
        mutableStateOf(1f)
    }
    val paddingRight = 16.dp
    val paddingLeft = 16.dp

    val globalYScale = 0.9f

    val rowOffset = 60.dp
    val columnOffset = 30.dp
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr,
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            Canvas(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Grey50)
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
                        onDragStart = {
                            dragOffset.value = it.x
                            isDragging.value = true
                        }, onDragEnd = {
                            isDragging.value = false
                        }, onZoom = { zoom ->
                            xZoom.value *= zoom
                        }) { change, _ ->
                        dragOffset.value = change.position.x
                    }
                },
                onDraw = {
                    val xStart = columnOffset.toPx() + paddingLeft.toPx()
                    val yStart = rowOffset.toPx()
                    val availableWidth = (size.width - xStart)
                    val availableHeight = size.height - yStart
                    val xScale = 1f * xZoom.value
                    val yScale = globalYScale
                    val xOffset = 20.dp.toPx()
                    val yOffset = availableHeight / dataPoints.maxOf { it.y }

                    var prevOffset: Offset? = null
                    val xLastPoint = dataPoints.last().x * xOffset * xScale + xStart
                    if (xLastPoint > availableWidth) {
                        maxScrollOffset.value = xLastPoint - availableWidth
                    }
                    var xLock = 0f

                    // Draw Grid (horizontal lines for every 25 points in Y
                    (0..4).forEach {
                        val y = it * 25f
                        val y1 = availableHeight - (y * yOffset * yScale)
                        drawLine(
                            Color.Black,
                            Offset(xStart, y1),
                            Offset(size.width, y1),
                            1.dp.toPx()
                        )
                    }

                    // Draw Points and Lines
                    dataPoints.forEach { (x, y) ->
                        val x1 = (x * xOffset * xScale) + xStart - offset.value
                        val y1 = availableHeight - (y * yOffset * yScale)
                        val curOffset = Offset(x1, y1)
                        val color =
                            if (isDragging.value && (dragOffset.value) > x1 - (xOffset * xScale) / 2 && (dragOffset.value) < x1 + (xOffset * xScale) / 2) {
                                xLock = x1
                                Color.Red
                            } else Color.Blue
                        drawCircle(color, pointRadius.toPx(), curOffset)
                        if (prevOffset != null) {
                            drawLine(Color.Blue, prevOffset!!, curOffset, lineWidth.toPx())
                        }
                        prevOffset = curOffset
                    }
                    if (isDragging.value) {
                        drawLine(
                            Color.Red, Offset(xLock, 0f),
                            Offset(xLock, availableHeight), lineWidth.toPx()
                        )
                    }

                    // Draw column
                    drawRect(Grey50, Offset(0f, 0f), Size(xStart - pointRadius.toPx(), size.height))

                    // Draw right padding
                    drawRect(
                        Grey50,
                        Offset(size.width - paddingRight.toPx(), 0f),
                        Size(paddingRight.toPx(), size.height)
                    )

                    // Draw area under curve
                    val points = dataPoints.map { (x, y) ->
                        val x1 = (x * xOffset * xScale) + xStart - offset.value
                        val y1 = availableHeight - (y * yOffset * yScale)
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
                    drawPath(p, Color.Blue, 0.1f)
                })
            GraphColumn(
                Modifier
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
                    .width(90.dp)
                    .padding(start = 16.dp), rowOffset, globalYScale,
                values = {
                    (0..4).map {
                        val v = it * 25f
                        Value(v.toInt().toString(), v)
                    }
                }
            )
            val values = {
                (0..23).map {
                    val v = it.toFloat()
                    Value(v.toInt().toString(), v)
                }
            }
            GraphRow(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RowClip(columnOffset + paddingLeft - pointRadius)),
                columnOffset + paddingLeft,
                offset.value,
                1f * xZoom.value,
                values = values,
                stepSize = 20.dp
            ) {
                values().forEach { (text, i) ->
                    val color = MaterialTheme.colors.onSurface
                    Column() {
                        val isMajor = i.toInt() % 4 == 0
                        val radius = if (isMajor) 20f else 10f
                        Canvas(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(20.dp),
                            onDraw = {
                                drawCircle(color = color, radius, Offset(0f, 40f))
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
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlotTheme {
        LineGraph(dataPoints)
    }
}

data class DataPoint(val x: Float, val y: Float)
