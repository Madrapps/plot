package com.madrapps.plot

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun GraphColumn(
    modifier: Modifier,
    paddingTop: Float,
    paddingBottom: Float,
    scale: Float,
    color: Color = MaterialTheme.colors.onSurface,
    values: () -> List<Value> = { listOf(Value("0", 0f)) },
    content: @Composable () -> Unit = {
        values().forEach { (text, _) ->
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                color = color
            )
        }
    }
) {
    val valueList = values()
    val steps = valueList.size - 1
    val min = valueList.first().value
    val max = valueList.last().value
    val stepSize = if (steps != 0) {
        (max - min) / steps
    } else 0f

    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minHeight = 0))
        }
        val width = placeables.maxOf { it.width }
        layout(width, constraints.maxHeight) {
            val yBottom = (constraints.maxHeight - paddingBottom)
            val availableHeight = yBottom - paddingTop
            var yPos = yBottom.toInt()

            placeables.forEach { placeable ->
                yPos -= (placeable.height / 2f).toInt() + 1
                placeable.place(x = 0, y = yPos)
                yPos -= (stepSize * availableHeight / max * scale).toInt() - (placeable.height / 2f).toInt()
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
    color: Color = MaterialTheme.colors.onSurface,
    values: () -> List<Value> = { listOf(Value("0", 0f)) },
    stepSize: Dp,
    content: @Composable () -> Unit = {
        values().forEach { (text, _) ->
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                color = color
            )
        }
    }
) {
    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
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

data class Value(val text: String, val value: Float)
