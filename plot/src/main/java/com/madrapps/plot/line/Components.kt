package com.madrapps.plot.line

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

data class DataPoint(val x: Float, val y: Float)

data class LinePlot(
    val lines: List<Line>,
    val grid: Grid? = null,
    val selection: Selection = Selection(),
    val row: Row = Row(),
    val column: Column = Column(),
    val isZoomAllowed: Boolean = true, // FIXME Change this to false
    val paddingTop: Dp = 16.dp,
    val paddingRight: Dp = 0.dp,
    val horizontalExtraSpace: Dp = 6.dp,
) {
    data class Line(
        val dataPoints: List<DataPoint>,
        val connection: Connection?,
        val intersection: Intersection?,
        val highlight: Highlight? = null,
        val areaUnderLine: AreaUnderLine? = null
    )

    data class Connection(
        val color: Color = Color.Blue,
        val strokeWidth: Dp = 3.dp,
        val cap: StrokeCap = Stroke.DefaultCap,
        val pathEffect: PathEffect? = null,
        /*FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 1.0f,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset, Offset) -> Unit = { start, end ->
            drawLine(
                color,
                start,
                end,
                strokeWidth.toPx(),
                cap,
                pathEffect,
                alpha,
                colorFilter,
                blendMode
            )
        }
    )

    data class Intersection(
        val color: Color = Color.Blue,
        val radius: Dp = 6.dp,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 1.0f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset, DataPoint) -> Unit = { center, _ ->
            drawCircle(
                color,
                radius.toPx(),
                center,
                alpha,
                style,
                colorFilter,
                blendMode
            )
        }
    )

    data class Highlight(
        val color: Color = Color.Black,
        val radius: Dp = 6.dp,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 1.0f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset) -> Unit = { center ->
            drawCircle(
                color,
                radius.toPx(),
                center,
                alpha,
                style,
                colorFilter,
                blendMode
            )
        }
    )

    data class Selection(
        val enabled: Boolean = true,
        val highlight: Connection? = Connection(
            Color.Red,
            strokeWidth = 2.dp,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
        ),
        val detectionTime: Long = 100L,
    )

    data class AreaUnderLine(
        val color: Color = Color.Blue,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 0.1f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Path) -> Unit = { path ->
            drawPath(path, color, alpha, style, colorFilter, blendMode)
        }
    )

    data class Grid(
        val color: Color,
        val steps: Int = 5,
        val lineWidth: Dp = 1.dp,
        val draw: DrawScope.(Rect, Float, Float) -> Unit = { region, _, yOffset ->
            val (left, top, right, bottom) = region
            (0 until steps).forEach {
                val y = it * 25f
                val y1 = bottom - (y * yOffset)
                if (y1 >= top) {
                    drawLine(
                        color,
                        Offset(left, y1),
                        Offset(right, y1),
                        lineWidth.toPx()
                    )
                }
            }
        }
    )

    data class Row(
        val stepSize: Dp = 20.dp,
        val steps: Int = 10,
        val unit: Float = 1f,
        val paddingTop: Dp = 8.dp,
        val paddingBottom: Dp = 8.dp,
        val roundToInt: Boolean = true,
        val content: @Composable (Float, Float, Float) -> Unit = { min, offset, max ->
            for (it in 0 until steps) {
                val value = it * offset + min
                Text(
                    text = value.string(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface
                )
                if (value > max) {
                    break
                }
            }
        }
    )

    data class Column(
        val steps: Int = 5,
        val roundToInt: Boolean = true,
        val paddingStart: Dp = 16.dp,
        val paddingEnd: Dp = 8.dp,
        val content: @Composable (Float, Float, Float) -> Unit = { min, offset, _ ->
            for (it in 0 until steps) {
                val value = it * offset + min
                Text(
                    text = value.string(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}

private fun Float.string() = DecimalFormat("#.#").format(this)
