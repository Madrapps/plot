package com.madrapps.sample

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val RoundRectangle: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rounded {
        val radius = 8.dp.value * density.density
        return Outline.Rounded(RoundRect(size.toRect(), CornerRadius(radius, radius)))
    }

    override fun toString(): String = "RoundRectangleShape"
}

internal fun Dp.toPx(density: Density) = value * density.density
