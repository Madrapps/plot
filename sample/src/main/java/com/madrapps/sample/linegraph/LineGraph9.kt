package com.madrapps.sample.linegraph

import android.util.Log
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
internal fun LineGraph9(item: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    item[0],
                    LinePlot.Connection(),
                    LinePlot.Intersection(),
                    LinePlot.Highlight(),
                    LinePlot.AreaUnderLine()
                )
            ),
            horizontalExtraSpace = 12.dp,
            row = LinePlot.Row(unit = 0.1f, roundToInt = false),
            column = LinePlot.Column(steps = 4, roundToInt = false)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onSelection = { x, points ->
            Log.d("GRAPH9", "x|points = $x|$points")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph9Preview() {
    PlotTheme {
        LineGraph4(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}
