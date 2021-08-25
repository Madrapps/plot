package com.madrapps.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LinePlot
import com.madrapps.plot.line.LinePlot.AreaUnderLine
import com.madrapps.plot.line.LinePlot.Connection
import com.madrapps.plot.line.LinePlot.Grid
import com.madrapps.plot.line.LinePlot.Highlight
import com.madrapps.plot.line.LinePlot.Intersection
import com.madrapps.plot.line.LinePlot.Line
import com.madrapps.plot.line.LineGraph
import com.madrapps.sample.ui.theme.PlotTheme

private val dataPoints1 = listOf(
    DataPoint(0f, 0f),
    DataPoint(1f, 0f),
    DataPoint(2f, 0f),
    DataPoint(3f, 0f),
    DataPoint(4f, 0f),
    DataPoint(5f, 25f),
    DataPoint(6f, 75f),
    DataPoint(7f, 100f),
    DataPoint(8f, 80f),
    DataPoint(9f, 75f),
    DataPoint(10f, 55f),
    DataPoint(11f, 45f), // FIXME :Bug: Change this to -45f. Graph doesn't adapt.
    DataPoint(12f, 50f),
    DataPoint(13f, 80f),
    DataPoint(14f, 70f),
    DataPoint(15f, 25f),
    DataPoint(16f, 0f), // FIXME :Bug: Change this to 200f. Column doesn't adapt.
    DataPoint(17f, 0f),
    DataPoint(18f, 35f),
    DataPoint(19f, 60f),
    DataPoint(20f, 20f),
    DataPoint(21f, 40f),
    DataPoint(22f, 75f),
    DataPoint(23f, 50f),
//    DataPoint(27f, 20f), // FIXME :Bug: Add these. Row doesn't react to extra values
//    DataPoint(33f, 80f),
)

private val dataPoints2 = listOf(
    DataPoint(0f, 0f),
    DataPoint(1f, 0f),
    DataPoint(2f, 25f),
    DataPoint(3f, 75f),
    DataPoint(4f, 100f),
    DataPoint(5f, 80f),
    DataPoint(6f, 75f),
    DataPoint(7f, 50f),
    DataPoint(8f, 80f),
    DataPoint(9f, 70f),
    DataPoint(10f, 0f),
    DataPoint(11f, 0f),
    DataPoint(12f, 45f),
    DataPoint(13f, 20f),
    DataPoint(14f, 40f),
    DataPoint(15f, 75f),
    DataPoint(16f, 50f),
    DataPoint(17f, 75f),
    DataPoint(18f, 40f),
    DataPoint(19f, 20f),
    DataPoint(20f, 0f),
    DataPoint(21f, 0f),
    DataPoint(22f, 50f),
    DataPoint(23f, 25f),
//    DataPoint(33f, 25f),
//    DataPoint(43f, 25f),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlotTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = MaterialTheme.colors.background
                ) {
                    LineGraph(
                        LinePlot(
                            listOf(
                                Line(
                                    dataPoints1,
                                    Connection(Color.Blue, 3.dp),
                                    Intersection(Color.Blue, 6.dp),
                                    Highlight(Color.Red, 12.dp),
                                    AreaUnderLine(Color.Blue, 0.1f)
                                ),
                                Line(
                                    dataPoints2,
//                                    dataPoints2.map { DataPoint(it.x * 2, it.y) },
                                    Connection(Color.Gray, 2.dp),
                                    Intersection { center, _ ->
                                        val px = 4.dp.toPx()
                                        val topLeft = Offset(center.x - px, center.y - px)
                                        drawRect(Color.Gray, topLeft, Size(px * 2, px * 2))
                                    },
                                    Highlight { center ->
                                        val px = 4.dp.toPx()
                                        val topLeft = Offset(center.x - px, center.y - px)
                                        drawRect(Color.Red, topLeft, Size(px * 2, px * 2))
                                    },
                                ),
                            ), Grid(Color.Gray)
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlotTheme {
        LineGraph(
            LinePlot(
                listOf(
                    Line(
                        dataPoints1,
                        Connection(Color.Blue, 3.dp),
                        Intersection(Color.Blue, 6.dp),
                        Highlight(Color.Red, 4.dp),
                        AreaUnderLine(Color.Blue, 0.1f)
                    ),
                    Line(
                        dataPoints2,
                        Connection(Color.Gray, 2.dp),
                        Intersection(Color.Gray, 3.dp),
                        Highlight(Color.Gray, 3.dp)
                    ),
                )
            )
        )
    }
}
