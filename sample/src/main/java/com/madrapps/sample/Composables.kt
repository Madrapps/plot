package com.madrapps.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp

@Composable
fun GraphColumn(
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


@Composable
fun GraphRow(
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
