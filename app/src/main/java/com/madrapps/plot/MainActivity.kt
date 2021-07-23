package com.madrapps.plot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.ui.theme.Grey50
import com.madrapps.plot.ui.theme.PlotTheme
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException

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
            Log.d("RONNY", "scope = $this")
            detectDragGesturesAfterLongPress1(
                onDragEnd = {
                    isDragging.value = false
                }, onDragStart = {
                    dragOffset.value = it.x
                    isDragging.value = true
                }, onDragCancel = {
                    isDragging.value = false
                }) { change, _ ->
                dragOffset.value = change.position.x
            }
//            forEachGesture {
//                awaitPointerEventScope {
//                    awaitFirstDown(requireUnconsumed = false)
//                    do {
//                        val event = awaitPointerEvent()
//                        val canceled = event.changes.any { it.positionChangeConsumed() }
//                        if (!canceled) {
//                            val zoomChange = event.calculateZoom()
//                        }
//
//                        if (event.changes.size == 1) {
//                            Log.d("RONNY", "ONE")
//                        } else if (event.changes.size == 2) {
//                            Log.d("RONNY", "TWO")
//                        } else {
//                            Log.d("RONNY", "NONE")
//                            break
//                        }
//                    } while (!canceled && event.changes.any { it.pressed })
//                }
//                Log.d("RONNY", "DONE")
//            }

        },
        onDraw = {
            val xStart = 30.dp.toPx()
            val yStart = 30.dp.toPx()
            val availableWidth = (size.width - xStart)
            val availableHeight = size.height - yStart
            val xScale = 1f
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

suspend fun PointerInputScope.detectDragGesturesAfterLongPress1(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    forEachGesture {
        val down = awaitPointerEventScope {
            awaitFirstDown(requireUnconsumed = false)
        }
        try {
            val drag = awaitLongPressOrCancellation(down)
            if (drag != null) {
                onDragStart.invoke(drag.position)

                awaitPointerEventScope {
                    if (
                        drag(drag.id) {
                            onDrag(it, it.positionChange())
                            it.consumePositionChange()
                        }
                    ) {
                        // consume up if we quit drag gracefully with the up
                        currentEvent.changes.forEach {
                            if (it.changedToUp()) {
                                it.consumeDownChange()
                            }
                        }
                        onDragEnd()
                    } else {
                        onDragCancel()
                    }
                }
            }
        } catch (c: CancellationException) {
            onDragCancel()
            throw c
        }
    }
}

private suspend fun PointerInputScope.awaitLongPressOrCancellation(
    initialDown: PointerInputChange
): PointerInputChange? {
    var longPress: PointerInputChange? = null
    var currentDown = initialDown
    val longPressTimeout = 100L
    return try {
        // wait for first tap up or long press
        withTimeout(longPressTimeout) {
            awaitPointerEventScope {
                var finished = false
                while (!finished) {
                    val event = awaitPointerEvent(PointerEventPass.Main)
                    if (event.changes.all { it.changedToUpIgnoreConsumed() }) {
                        // All pointers are up
                        finished = true
                    }

                    if (
                        event.changes.any { it.consumed.downChange || it.isOutOfBounds(size) }
                    ) {
                        finished = true // Canceled
                    }

                    // Check for cancel by position consumption. We can look on the Final pass of
                    // the existing pointer event because it comes after the Main pass we checked
                    // above.
                    val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
                    if (consumeCheck.changes.any { it.positionChangeConsumed() }) {
                        finished = true
                    }
                    if (!event.isPointerUp(currentDown.id)) {
                        longPress = event.changes.firstOrNull { it.id == currentDown.id }
                    } else {
                        val newPressed = event.changes.firstOrNull { it.pressed }
                        if (newPressed != null) {
                            currentDown = newPressed
                            longPress = currentDown
                        } else {
                            // should technically never happen as we checked it above
                            finished = true
                        }
                    }
                }
            }
        }
        null
    } catch (_: TimeoutCancellationException) {
        longPress ?: initialDown
    }
}

private fun PointerEvent.isPointerUp(pointerId: PointerId): Boolean =
    changes.firstOrNull { it.id == pointerId }?.pressed != true
