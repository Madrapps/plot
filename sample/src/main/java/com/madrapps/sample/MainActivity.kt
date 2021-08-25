package com.madrapps.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.madrapps.plot.line.LinePlot.AreaUnderLine
import com.madrapps.plot.line.LinePlot.Connection
import com.madrapps.plot.line.LinePlot.Grid
import com.madrapps.plot.line.LinePlot.Highlight
import com.madrapps.plot.line.LinePlot.Intersection
import com.madrapps.plot.line.LinePlot.Line
import com.madrapps.sample.ui.theme.PlotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainViewModelImpl by viewModels()
        setContent {
            PlotTheme {
                Column {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = MaterialTheme.colors.background
                    ) {
                        val dataPoints1 = model.line1
                        LineGraph(
                            LinePlot(
                                listOf(
                                    Line(
                                        dataPoints1.value,
                                        Connection(Color.Blue, 3.dp),
                                        Intersection(Color.Blue, 6.dp),
                                        Highlight(Color.Red, 12.dp),
                                        AreaUnderLine(Color.Blue, 0.1f)
                                    ),
                                    Line(
                                        model.dataPoints2,
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
                    Button(
                        onClick = { model.change() },
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Text(text = "Change Data Points")
                    }
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
                        listOf(
                            DataPoint(6f, 75f),
                            DataPoint(7f, 100f),
                            DataPoint(8f, 80f),
                            DataPoint(9f, 75f),
                        ),
                        Connection(Color.Blue, 3.dp),
                        Intersection(Color.Blue, 6.dp),
                        Highlight(Color.Red, 4.dp),
                        AreaUnderLine(Color.Blue, 0.1f)
                    )
                )
            )
        )
    }
}
