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

@Composable
internal fun LineGraph4(item: List<List<DataPoint>>) {
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    item[0],
                    LinePlot.Connection(),
                    LinePlot.Intersection(),
                    LinePlot.Highlight(),
                    LinePlot.AreaUnderLine()
                ),
                LinePlot.Line(
                    item[1],
                    null,
                    null
                ),
            ),
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph4Preview() {
    PlotTheme {
        LineGraph4(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}