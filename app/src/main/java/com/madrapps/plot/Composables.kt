package com.madrapps.plot

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun GraphColumn(
    modifier: Modifier,
    yStart: Float,
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
    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            val availableHeight = (constraints.maxHeight - yStart)
            var yPosition = availableHeight.toInt()

            placeables.forEach { placeable ->
                yPosition -= (placeable.height / 2f).toInt() + 1
                placeable.place(x = 0, y = yPosition)

                yPosition -= (25 * availableHeight / 100f * scale).toInt() - (placeable.height / 2f).toInt()
            }
        }
    }
}

data class Value(val text: String, val value: Float)
