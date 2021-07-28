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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
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
                // A surface container using the 'background' color from the theme
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
    Canvas(modifier = Modifier
        .height(300.dp)
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
            val xStart = 40.dp.toPx()
            val yStart = 40.dp.toPx()
            val availableWidth = (size.width - xStart)
            val availableHeight = size.height - yStart
            val xScale = 1f * xZoom.value
            val yScale = 0.9f
            val xOffset = 30.dp.toPx()
            val yOffset = availableHeight / dataPoints.maxOf { it.y }

            var scrollOffset = offset.value
            var prevOffset: Offset? = null
            val xLastPoint = dataPoints.last().x * xOffset * xScale + xStart
            if (xLastPoint > availableWidth) {
                maxScrollOffset.value = xLastPoint - availableWidth
            }
            var xLock = 0f
            dataPoints.forEach { (x, y) ->
                val x1 = (x * xOffset * xScale) + xStart - scrollOffset
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
        })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlotTheme {
        LineGraph(dataPoints)
    }
}

data class DataPoint(val x: Float, val y: Float)
