package com.madrapps.sample.linegraph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.madrapps.sample.ui.theme.PlotTheme

@Composable
internal fun LineGraph2(item: List<List<DataPoint>>) {
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    item[0],
                    LinePlot.Connection(Color.Blue, 3.dp),
                    LinePlot.Intersection(Color.Blue, 6.dp) { center, point ->
                        val x = point.x
                        val rad = if (x % 4f == 0f) 6.dp else 3.dp
                        drawCircle(
                            Color.Blue,
                            rad.toPx(),
                            center,
                        )
                    },
                    LinePlot.Highlight(Color.Red, 6.dp),
                    LinePlot.AreaUnderLine(Color.Blue, 0.1f)
                ),
                LinePlot.Line(
                    item[1],
                    LinePlot.Connection(Color.Gray, 2.dp),
                    null,
                    LinePlot.Highlight { center ->
                        val px = 4.dp.toPx()
                        val topLeft = Offset(center.x - px, center.y - px)
                        drawRect(Color.Red, topLeft, Size(px * 2, px * 2))
                    },
                ),
            ), LinePlot.Grid(Color.Gray)
        )
    )
}


@Preview(showBackground = true)
@Composable
fun LineGraph2Preview() {
    PlotTheme {
        LineGraph2(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}
