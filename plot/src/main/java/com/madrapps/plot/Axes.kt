package com.madrapps.plot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp

/**
 * Composable that Layouts the child composables in the Y Axis. This does the same thing as a Column
 * composable, but with customisation that takes care of the scale.
 *
 * @param modifier Modifier
 * @param paddingTop the top padding
 * @param paddingBottom the bottom padding
 * @param scale the scale in y axis
 * @param content the composable that draws the item in the Y axis
 */
@Composable
internal fun GraphYAxis(
    modifier: Modifier,
    paddingTop: Float,
    paddingBottom: Float,
    scale: Float,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val steps = if (measurables.size <= 1) 1 else measurables.size - 1
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minHeight = 0))
        }
        val width = placeables.maxOf { it.width }
        layout(width, constraints.maxHeight) {
            val yBottom = (constraints.maxHeight - paddingBottom)
            val availableHeight = yBottom - paddingTop
            var yPos = yBottom.toInt()

            placeables.forEach { placeable ->
                yPos -= (placeable.height / 2f).toInt()
                placeable.place(x = 0, y = yPos)
                yPos -= (availableHeight / steps * scale).toInt() - (placeable.height / 2f).toInt()
            }
        }
    }
}

/**
 * Composable that Layouts the child composables in the X Axis. This does the same thing as a Row
 * composable, but with customisation that takes care of the scale.
 *
 * @param modifier Modifier
 * @param xStart the left position where the first child is laid out
 * @param scrollOffset the offset value that varies based on the scroll
 * @param scale the scale in x axis
 * @param stepSize the distance between two adjacent data points
 * @param content the composable that draws the item in the X axis
 */
@Composable
internal fun GraphXAxis(
    modifier: Modifier,
    xStart: Float,
    scrollOffset: Float,
    scale: Float,
    stepSize: Dp,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0))
        }
        val height = placeables.maxOf { it.height }
        layout(constraints.maxWidth, height) {
            var xPos = (xStart - scrollOffset).toInt()
            val step = stepSize.toPx()
            placeables.forEach { placeable ->
                xPos -= (placeable.width / 2f).toInt()
                placeable.place(x = xPos, y = 0)
                xPos += ((step * scale) + (placeable.width / 2f)).toInt()
            }
        }
    }
}
