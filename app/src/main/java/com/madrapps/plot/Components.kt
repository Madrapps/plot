package com.madrapps.plot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DataPoint(val x: Float, val y: Float)

data class Line(
    val dataPoints: List<DataPoint>,
    val connection: Connection?,
    val intersection: Intersection?,
    val highlight: Intersection? = null,
    val areaUnderLine: AreaUnderLine? = null
)

data class Connection(
    val color: Color = Color.Black,
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

data class AreaUnderLine(
    val color: Color = Color.LightGray,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    val alpha: Float = 1.0f,
    val style: DrawStyle = Fill,
    val colorFilter: ColorFilter? = null,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode
)