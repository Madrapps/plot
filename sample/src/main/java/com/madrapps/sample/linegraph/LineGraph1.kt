package com.madrapps.sample.linegraph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.madrapps.sample.ui.theme.Green900
import com.madrapps.sample.ui.theme.LightGreen600
import com.madrapps.sample.ui.theme.PlotTheme

@Composable
internal fun LineGraph1(item: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    item[0],
                    LinePlot.Connection(LightGreen600, 2.dp),
                    LinePlot.Intersection(LightGreen600, 5.dp),
                    LinePlot.Highlight(Green900, 5.dp),
                    LinePlot.AreaUnderLine(LightGreen600, 0.3f)
                ),
                LinePlot.Line(
                    item[1],
                    LinePlot.Connection(Color.Gray, 2.dp),
                    LinePlot.Intersection { center, _ ->
                        val px = 2.dp.toPx()
                        val topLeft = Offset(center.x - px, center.y - px)
                        drawRect(Color.Gray, topLeft, Size(px * 2, px * 2))
                    },
                ),
            ),
            LinePlot.Grid(Color.Gray),
            selection = LinePlot.Selection(
                highlight = LinePlot.Connection(
                    Green900,
                    strokeWidth = 2.dp,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
                )
            ),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph1Preview() {
    PlotTheme {
        LineGraph1(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}