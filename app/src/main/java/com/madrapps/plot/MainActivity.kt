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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
                        listOf(
                            Line(
                                dataPoints1,
                                Connection(Color.Blue, 3.dp),
                                Intersection(Color.Blue, 6.dp),
                                Intersection(Color.Red, 4.dp)
                            ),
                            Line(
                                dataPoints2,
                                Connection(Color.Gray, 2.dp),
                                Intersection(Color.Gray, 4.dp, draw = { config, center ->
                                    val px = config.radius.toPx()
                                    val topLeft = Offset(center.x - px, center.y - px)
                                    drawRect(config.color, topLeft, Size(px*2, px*2))
                                })
                            ),
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LineGraph(lines: List<Line>) {
    // Graph Line properties
    val pointRadius = 6.dp
    val lineWidth = 3.dp

    // Overall Graph properties
    val paddingLeft = 16.dp
    val paddingRight = 16.dp
    val globalXScale = 1f
    val globalYScale = 0.9f
    val rowOffset = 60.dp
    val columnOffset = 30.dp
    val rowHeight = 50.dp
    val columnWidth = 90.dp
    val bgColor = MaterialTheme.colors.surface

    val offset = remember { mutableStateOf(0f) }
    val maxScrollOffset = remember { mutableStateOf(0f) }
    val dragOffset = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val xZoom = remember { mutableStateOf(globalXScale) }

    val isZoomAllowed = false
    val isDragAllowed = true

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
                        isDragAllowed = isDragAllowed,
                        isZoomAllowed = isZoomAllowed,
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
                    val xOffset = 20.dp.toPx()
                    val allDataPoints = lines.flatMap { it.dataPoints }
                    val yOffset = availableHeight / allDataPoints.maxOf { it.y }

                    val xLastPoint = allDataPoints.maxOf { it.x } * xOffset * xScale + xStart
                    if (xLastPoint > availableWidth) {
                        maxScrollOffset.value = xLastPoint - availableWidth
                    }
                    var xLock = 0f

                    // Draw Grid (horizontal lines for every 25 points in Y
                    (0..4).forEach {
                        val y = it * 25f
                        val y1 = availableHeight - (y * yOffset * globalYScale)
                        drawLine(
                            Color.Black,
                            Offset(xStart, y1),
                            Offset(size.width, y1),
                            1.dp.toPx()
                        )
                    }

                    // Draw Points and Lines
                    lines.forEach { line ->
                        var prevOffset: Offset? = null
                        val intersection = line.intersection
                        val co = line.connection
                        val hl = line.highlight
                        line.dataPoints.forEach { (x, y) ->
                            val x1 = (x * xOffset * xScale) + xStart - offset.value
                            val y1 = availableHeight - (y * yOffset * globalYScale)
                            val curOffset = Offset(x1, y1)
                            if (isDragging.value && (dragOffset.value) > x1 - (xOffset * xScale) / 2 && (dragOffset.value) < x1 + (xOffset * xScale) / 2) {
                                xLock = x1
                                if (hl != null) {
                                    drawCircle(
                                        hl.color,
                                        hl.radius.toPx(),
                                        curOffset,
                                        hl.alpha,
                                        hl.style,
                                        hl.colorFilter,
                                        hl.blendMode
                                    )
                                }
                            } else {
                                intersection?.draw?.invoke(this, intersection, curOffset)
                            }
                            if (prevOffset != null && co != null) {
                                drawLine(
                                    co.color,
                                    prevOffset!!,
                                    curOffset,
                                    co.strokeWidth.toPx(),
                                    co.cap,
                                    co.pathEffect,
                                    co.alpha,
                                    co.colorFilter,
                                    co.blendMode
                                )
                            }
                            prevOffset = curOffset
                        }
                    }

                    if (isDragging.value) {
                        drawLine(
                            Color.Red, Offset(xLock, 0f),
                            Offset(xLock, availableHeight), lineWidth.toPx()
                        )
                    }

                    // Draw column
                    drawRect(
                        bgColor,
                        Offset(0f, 0f),
                        Size(xStart - pointRadius.toPx(), size.height)
                    )

                    // Draw right padding
                    drawRect(
                        bgColor,
                        Offset(size.width - paddingRight.toPx(), 0f),
                        Size(paddingRight.toPx(), size.height)
                    )

                    // Draw area under curve
                    val points = dataPoints1.map { (x, y) ->
                        val x1 = (x * xOffset * xScale) + xStart - offset.value
                        val y1 = availableHeight - (y * yOffset * globalYScale)
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
                    .width(columnWidth)
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
                    .height(rowHeight)
                    .clip(RowClip(columnOffset + paddingLeft - pointRadius)),
                columnOffset + paddingLeft,
                offset.value,
                1f * xZoom.value,
                values = values,
                stepSize = 20.dp
            ) {
                values().forEach { (text, i) ->
                    val color = MaterialTheme.colors.onSurface
                    Column {
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
        LineGraph(
            listOf(
                Line(
                    dataPoints1,
                    Connection(Color.Blue, 3.dp),
                    Intersection(Color.Blue, 6.dp),
                    Intersection(Color.Red, 4.dp)
                ),
                Line(
                    dataPoints2,
                    Connection(Color.Gray, 2.dp),
                    Intersection(Color.Gray, 3.dp),
                    Intersection(Color.Gray, 3.dp)
                ),
            )
        )
    }
}

data class DataPoint(val x: Float, val y: Float)

data class Line(
    val dataPoints: List<DataPoint>,
    val connection: Connection?,
    val intersection: Intersection?,
    val highlight: Intersection? = null,
)

data class Connection(
    val color: Color,
    val strokeWidth: Dp,
    val cap: StrokeCap = Stroke.DefaultCap,
    val pathEffect: PathEffect? = null,
    /*FloatRange(from = 0.0, to = 1.0)*/
    val alpha: Float = 1.0f,
    val colorFilter: ColorFilter? = null,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode,
)

data class Intersection(
    val color: Color,
    val radius: Dp,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    val alpha: Float = 1.0f,
    val style: DrawStyle = Fill,
    val colorFilter: ColorFilter? = null,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode,
    val draw: DrawScope.(Intersection, Offset) -> Unit = { config, center ->
        drawCircle(
            config.color,
            config.radius.toPx(),
            center,
            config.alpha,
            config.style,
            config.colorFilter,
            config.blendMode
        )
    }
)
