package com.madrapps.plot.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.madrapps.plot.GraphXAxis
import com.madrapps.plot.GraphYAxis
import com.madrapps.plot.RowClip
import com.madrapps.plot.detectDragZoomGesture
import kotlin.math.ceil

@Composable
fun LineGraph(
    plot: LinePlot, modifier: Modifier = Modifier,
    onSelection: ((Float, List<DataPoint>) -> Unit)? = null,
    onSelectionStart: () -> Unit = {},
    onSelectionEnd: () -> Unit = {}
) {
    val paddingTop = plot.paddingTop
    val paddingRight = plot.paddingRight
    val horizontalGap = plot.horizontalExtraSpace
    val isZoomAllowed = plot.isZoomAllowed

    val globalXScale = 1f
    val globalYScale = 1f

    val offset = remember { mutableStateOf(0f) }
    val maxScrollOffset = remember { mutableStateOf(0f) }
    val dragOffset = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val xZoom = remember { mutableStateOf(globalXScale) }
    val rowHeight = remember { mutableStateOf(0f) }
    val columnWidth = remember { mutableStateOf(0f) }
    val bgColor = MaterialTheme.colors.surface

    val lines = plot.lines

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr,
    ) {
        Box(
            modifier = modifier.clipToBounds(),
        ) {
            val points = lines.flatMap { it.dataPoints }
            val (xMin, xMax, xAxisScale) = getXAxisScale(points, plot)
            val (yMin, yMax, yAxisScale) = getYAxisScale(points, plot)

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
                    }, Orientation.Horizontal, enabled = true
                )
                .pointerInput(Unit, Unit) {
                    detectDragZoomGesture(
                        isZoomAllowed = isZoomAllowed,
                        isDragAllowed = plot.selection.enabled,
                        detectDragTimeOut = plot.selection.detectionTime,
                        onDragStart = {
                            dragOffset.value = it.x
                            onSelectionStart()
                            isDragging.value = true
                        }, onDragEnd = {
                            isDragging.value = false
                            onSelectionEnd()
                        }, onZoom = { zoom ->
                            xZoom.value *= zoom
                        }
                    ) { change, _ ->
                        dragOffset.value = change.position.x
                    }
                },
                onDraw = {
                    val xLeft = columnWidth.value + horizontalGap.toPx()
                    val yBottom = size.height - rowHeight.value
                    val xOffset = 20.dp.toPx() * xZoom.value
                    val maxElementInYAxis =
                        getMaxElementInYAxis(yMin, yAxisScale, plot.column.steps)
                    val yOffset = ((yBottom - paddingTop.toPx()) / maxElementInYAxis) * globalYScale

                    val xLastPoint =
                        (xMax - xMin) * xOffset + xLeft + paddingRight.toPx() + horizontalGap.toPx()
                    maxScrollOffset.value = if (xLastPoint > size.width) {
                        xLastPoint - size.width
                    } else 0f

                    val dragLocks = mutableMapOf<LinePlot.Line, Pair<DataPoint, Offset>>()

                    // Draw Grid lines
                    val region =
                        Rect(xLeft, paddingTop.toPx(), size.width - paddingRight.toPx(), yBottom)
                    plot.grid?.draw?.invoke(this, region, xOffset, yOffset)

                    // Draw Lines and Points and AreaUnderLine
                    lines.forEach { line ->
                        val intersection = line.intersection
                        val connection = line.connection
                        val areaUnderLine = line.areaUnderLine

                        // Draw area under curve
                        if (areaUnderLine != null) {
                            val pts = line.dataPoints.map { (x, y) ->
                                val x1 = ((x - xMin) * xOffset) + xLeft - offset.value
                                val y1 = yBottom - (y * yOffset)
                                Offset(x1, y1)
                            }
                            val p = Path()
                            pts.forEachIndexed { index, offset ->
                                if (index == 0) {
                                    p.moveTo(offset.x, yBottom)
                                }
                                p.lineTo(offset.x, offset.y)
                            }
                            val last = pts.last()
                            val first = pts.first()
                            p.lineTo(last.x, yBottom)
                            p.lineTo(first.x, yBottom)
                            areaUnderLine.draw(this, p)
                        }

                        // Draw Lines and Points
                        var curOffset: Offset? = null
                        var nextOffset: Offset? = null
                        line.dataPoints.forEachIndexed { i, _ ->
                            if (i == 0) {
                                val (x, y) = line.dataPoints[i]
                                val x1 = ((x - xMin) * xOffset) + xLeft - offset.value
                                val y1 = yBottom - (y * yOffset)
                                curOffset = Offset(x1, y1)
                            }
                            if (line.dataPoints.indices.contains(i + 1)) {
                                val (x, y) = line.dataPoints[i + 1]
                                val x2 = ((x - xMin) * xOffset) + xLeft - offset.value
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
                                    dragLocks[line] = line.dataPoints[i] to it
                                } else {
                                    intersection?.draw?.invoke(this, it, line.dataPoints[i])
                                }
                            }
                            curOffset = nextOffset
                        }
                    }

                    // Draw column
                    drawRect(
                        bgColor,
                        Offset(0f, 0f),
                        Size(columnWidth.value, size.height)
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
                        dragLocks.values.firstOrNull()?.let { (_, location) ->
                            val (x, _) = location
                            if (x >= columnWidth.value && x <= size.width - paddingRight.toPx()) {
                                plot.selection.highlight?.draw?.invoke(
                                    this,
                                    Offset(x, yBottom),
                                    Offset(x, 0f)
                                )
                            }
                        }
                        // Draw Point Highlight
                        dragLocks.entries.forEach { (line, lock) ->
                            val highlight = line.highlight
                            val location = lock.second
                            val x = location.x
                            if (x >= columnWidth.value && x <= size.width - paddingRight.toPx()) {
                                highlight?.draw?.invoke(this, location)
                            }
                        }
                    }

                    // OnSelection
                    if (isDragging.value) {
                        val x = dragLocks.values.firstOrNull()?.second?.x
                        if (x != null) {
                            onSelection?.invoke(x, dragLocks.values.map { it.first })
                        }
                    }
                })

            GraphXAxis(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(
                        RowClip(
                            columnWidth.value,
                            paddingRight
                        )
                    )
                    .onGloballyPositioned {
                        rowHeight.value = it.size.height.toFloat()
                    }
                    .padding(bottom = plot.row.paddingBottom, top = plot.row.paddingTop),
                columnWidth.value + horizontalGap.value * LocalDensity.current.density,
                offset.value,
                xZoom.value * xAxisScale,
                stepSize = plot.row.stepSize,
            ) {
                plot.row.content(xMin, xAxisScale, xMax)
            }

            GraphYAxis(
                Modifier
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .onGloballyPositioned {
                        columnWidth.value = it.size.width.toFloat()
                    }
                    .padding(start = plot.column.paddingStart, end = plot.column.paddingEnd),
                paddingTop = paddingTop.value * LocalDensity.current.density,
                paddingBottom = rowHeight.value,
                scale = globalYScale,
            ) {
                plot.column.content(yMin, yAxisScale, yMax)
            }
        }
    }
}

private fun getXAxisScale(
    points: List<DataPoint>,
    plot: LinePlot
): Triple<Float, Float, Float> {
    val xMin = points.minOf { it.x }
    val xMax = points.maxOf { it.x }
    val totalSteps =
        (xMax - xMin) + 1 // FIXME what if min is 0.1 and max is 0.9 ? The Graph itself doesn't work, since our unit is 1.
    val temp = totalSteps / plot.row.steps
    val scale = if (plot.row.roundToInt) ceil(temp) else temp
    return Triple(xMin, xMax, scale)
}

private fun getYAxisScale(
    points: List<DataPoint>,
    plot: LinePlot
): Triple<Float, Float, Float> {
    val steps = plot.column.steps
    val yMin = points.minOf { it.y }
    val yMax = points.maxOf { it.y }

    val totalSteps = (yMax - yMin)
    val temp = totalSteps / if (steps > 1) (steps - 1) else 1

    val scale = if (plot.column.roundToInt) ceil(temp) else temp
    return Triple(yMin, yMax, scale)
}

private fun getMaxElementInYAxis(yMin: Float, offset: Float, steps: Int): Float {
    return yMin + (if (steps > 1) steps - 1 else 1) * offset
}