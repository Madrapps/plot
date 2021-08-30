package com.madrapps.sample.linegraph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.madrapps.sample.ui.theme.PlotTheme
import java.text.DecimalFormat

@Composable
internal fun LineGraph3(item: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    item[0],
                    LinePlot.Connection(Color.Blue, 2.dp),
                    LinePlot.Intersection(Color.Blue, 4.dp),
                    LinePlot.Highlight(Color.Red, 6.dp),
                    LinePlot.AreaUnderLine(Color.Blue, 0.1f)
                )
            ), LinePlot.Grid(Color.LightGray.copy(0.5f)),
            xAxis = LinePlot.XAxis(steps = 24) { min, offset, max ->
                for (it in 0 until 24) {
                    val value = it * offset + min
                    androidx.compose.foundation.layout.Column {
                        val isMajor = value % 4 == 0f
                        val radius = if (isMajor) 6f else 3f
                        val color = MaterialTheme.colors.onSurface
                        Canvas(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(20.dp),
                            onDraw = {
                                drawCircle(
                                    color = color,
                                    radius * density,
                                    Offset(0f, 10f * density)
                                )
                            })
                        if (isMajor) {
                            Text(
                                text = DecimalFormat("#.#").format(value),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.caption,
                                color = color
                            )
                        }
                    }
                    if (value > max) {
                        break
                    }
                }
            }, paddingRight = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LineGraph3Preview() {
    PlotTheme {
        LineGraph3(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2))
    }
}
