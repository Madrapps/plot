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
import com.madrapps.plot.line.LinePlot.Grid
import com.madrapps.plot.line.LinePlot.Highlight
import com.madrapps.plot.line.LinePlot.Intersection
import com.madrapps.plot.line.LinePlot.Selection
import com.madrapps.plot.line.LinePlot.XAxis
import com.madrapps.plot.line.LinePlot.YAxis
import java.text.DecimalFormat

/**
 * Denotes a point in the graph.
 *
 * @param x the x coordinate or the number in the x axis
 * @param y the y coordinate or the number in the y axis
 */
data class DataPoint(val x: Float, val y: Float)

/**
 * The configuration for the [LineGraph]
 *
 * @param lines list of lines to be represented
 * @param grid rendering logic on how the [Grid] should be drawn. If null, no grid is drawn.
 * @param selection controls the touch and drag selection behaviour using [Selection]
 * @param xAxis controls the behaviour, scale and drawing logic of the X Axis
 * @param column controls the behaviour, scale and drawing logic of the Y Axis
 * @param isZoomAllowed if true, the graph will zoom on pinch zoom. If false, no zoom action.
 * @param paddingTop adjusts the top padding of the graph. If you want to adjust the bottom padding, adjust
 * the [XAxis.paddingBottom]
 * @param paddingRight adjust the right padding of the graph. If you want to adjust the left padding, adjust
 * the [YAxis.paddingStart]
 * @param horizontalExtraSpace gives extra space to draw [Intersection] or [Highlight] at the left and right
 * extremes of the graph. Adjust this if your graph looks like cropped at the left edge or the right edge.
 */
data class LinePlot(
    val lines: List<Line>,
    val grid: Grid? = null,
    val selection: Selection = Selection(),
    val xAxis: XAxis = XAxis(),
    val column: YAxis = YAxis(),
    val isZoomAllowed: Boolean = true, // FIXME Change this to false
    val paddingTop: Dp = 16.dp,
    val paddingRight: Dp = 0.dp,
    val horizontalExtraSpace: Dp = 6.dp,
) {
    /**
     * Represent a Line in the [LineGraph]
     *
     * @param dataPoints list of points in the line. Note that this list should be sorted by x coordinate
     * from decreasing to increasing value, so that the graph can be drawn properly.
     * @param connection drawing logic for the line between two adjacent points. If null, no line is drawn.
     * @param intersection drawing logic to draw the point itself. If null, the point is not drawn.
     * @param highlight drawing logic to draw the highlight at the point when it is selected. If null, the point
     * won't be highlighted on selection
     * @param areaUnderLine drawing logic for the area under the line. This is the region that is formed by the
     * intersection of the line, x-axis and y-axis.
     */
    data class Line(
        val dataPoints: List<DataPoint>,
        val connection: Connection?,
        val intersection: Intersection?,
        val highlight: Highlight? = null,
        val areaUnderLine: AreaUnderLine? = null
    )

    /**
     * Represents a line between two data points
     *
     * @param color the color to be applied to the line
     * @param strokeWidth The stroke width to apply to the line
     * @param cap treatment applied to the ends of the line segment
     * @param pathEffect optional effect or pattern to apply to the line
     * @param alpha opacity to be applied to the [color] from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode the blending algorithm to apply to the [color]
     * @param draw override this to change the default drawLine implementation. You are provided with
     * the 'start' [Offset] and 'end' [Offset]
     */
    data class Connection(
        val color: Color = Color.Blue,
        val strokeWidth: Dp = 3.dp,
        val cap: StrokeCap = Stroke.DefaultCap,
        val pathEffect: PathEffect? = null,
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

    /**
     * Represents a data point on the graph
     *
     * @param color The color or fill to be applied to the circle
     * @param radius The radius of the circle
     * @param alpha Opacity to be applied to the circle from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param style Whether or not the circle is stroked or filled in
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode Blending algorithm to be applied to the brush
     * @param draw override this to change the default drawCircle implementation. You are provided
     * with the 'center' [Offset] and the actual [DataPoint] that represents the intersection.
     */
    data class Intersection(
        val color: Color = Color.Blue,
        val radius: Dp = 6.dp,
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

    /**
     * Represents the data point when it is selected
     *
     * @param color The color or fill to be applied to the circle
     * @param radius The radius of the circle
     * @param alpha Opacity to be applied to the circle from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param style Whether or not the circle is stroked or filled in
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode Blending algorithm to be applied to the brush
     * @param draw override this to change the default drawCircle implementation. You are provided
     * with the 'center' [Offset].
     */
    data class Highlight(
        val color: Color = Color.Black,
        val radius: Dp = 6.dp,
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

    /**
     * Configuration for the selection operation
     *
     * @param enabled if true, you can touch and drag to select the points. The point currently selected
     * is exposed via the [onSelection] param in the [LineGraph]. If false, the drag gesture is disabled.
     * @param highlight controls how the selection is represented in the graph. The default implementation
     * is a vertical dashed line. You can override this by supplying your own [Connection]
     * @param detectionTime the time taken for the touch to be recognised as a drag gesture
     */
    data class Selection(
        val enabled: Boolean = true,
        val highlight: Connection? = Connection(
            Color.Red,
            strokeWidth = 2.dp,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
        ),
        val detectionTime: Long = 100L,
    )

    /**
     * Controls the drawing behaviour of the area under the line. This is the region formed by intersection
     * of the Line, x-axis and y-axis.
     *
     * @param color Color to be applied to the path
     * @param alpha Opacity to be applied to the path from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param style Whether or not the path is stroked or filled in
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode Blending algorithm to be applied to the path when it is drawn
     * @param draw override this to change the default drawPath implementation. You are provided with
     * the [Path] of the line
     */
    data class AreaUnderLine(
        val color: Color = Color.Blue,
        val alpha: Float = 0.1f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Path) -> Unit = { path ->
            drawPath(path, color, alpha, style, colorFilter, blendMode)
        }
    )

    /**
     * Controls how the grid is drawn on the Graph
     *
     * @param color the color to be applied
     * @param steps the number of lines drawn in the grid. The default implementation considers this
     * as the horizontal lines
     * @param lineWidth the width of the lines
     * @param draw override this to change the default drawLine implementation (which is to draw multiple
     * horizontal lines based on the number of [steps]. You are provided with the [Rect] region available
     * to draw the grid, xOffset (the gap between two points in the x-axis) and the yOffset.
     */
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

    /**
     * Configuration of the X Axis
     *
     * @param stepSize the distance between two adjacent data points
     * @param steps the number of values to be drawn in the axis
     * @param unit Represent the range of values in the x axis. For example if this is 1, then the values in
     * x axis would be (0, 1, 2, 3, ..., steps-1). If this is 0.1, then the values in x axis would be (0, 0.1,
     * 0.2, 0.3, ...)
     * @param paddingTop the top padding of the X axis
     * @param paddingBottom the bottom padding of the X axis
     * @param roundToInt if true, the values is X axis are represented by Integers. If false, the values could
     * be decimal values, with 1 decimal precision in the default implementation
     * @param content A composable where you could provide how the values should be rendered. The default
     * implementation is to show a [Text] composable. You are provided with the min value in x axis, the offset
     * between two x coordinates and the max value in x axis
     */
    data class XAxis(
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

    /**
     * Configuration of the Y Axis
     *
     * @param steps the number of values to be drawn in the axis
     * @param roundToInt if true, the values is Y axis are represented by Integers. If false, the values could
     * be decimal values, with 1 decimal precision in the default implementation
     * @param paddingStart the start padding of the Y axis
     * @param paddingEnd the end padding of the Y axis
     * @param content A composable where you could provide how the values should be rendered. The default
     * implementation is to show a [Text] composable. You are provided with the min value in y axis, the offset
     * between two y coordinates and the max value in y axis
     */
    data class YAxis(
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
