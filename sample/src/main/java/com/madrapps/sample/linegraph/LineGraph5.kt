package com.madrapps.sample.linegraph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.madrapps.sample.ui.theme.PlotTheme
import com.madrapps.sample.ui.theme.Red100
import com.madrapps.sample.ui.theme.Red300
import com.madrapps.sample.ui.theme.Red500
import com.madrapps.sample.ui.theme.Yellow700

@Composable
internal fun LineGraph5(lines: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(color = Red300),
                    LinePlot.Intersection(color = Red500),
                    LinePlot.Highlight(color = Yellow700),
                )
            ),
            horizontalExtraSpace = 12.dp,
            xAxis = LinePlot.XAxis(unit = 0.1f, roundToInt = false),
            yAxis = LinePlot.YAxis(steps = 4, roundToInt = false),
            grid = LinePlot.Grid(Red100, steps = 4),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph5Preview() {
    PlotTheme {
        LineGraph5(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}
