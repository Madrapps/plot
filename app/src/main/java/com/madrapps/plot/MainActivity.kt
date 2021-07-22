package com.madrapps.plot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.ui.theme.PlotTheme

private val dataPoints = listOf(
    DataPoint(0f,0f),
    DataPoint(1f,0f),
    DataPoint(2f,0f),
    DataPoint(3f,0f),
    DataPoint(4f,0f),
    DataPoint(5f,25f),
    DataPoint(6f,75f),
    DataPoint(7f,100f),
    DataPoint(8f,80f),
    DataPoint(9f,75f),
    DataPoint(10f,55f),
    DataPoint(11f,45f),
    DataPoint(12f,50f),
    DataPoint(13f,100f),
    DataPoint(14f,75f),
    DataPoint(15f,25f),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlotTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LineGraph(dataPoints)
                }
            }
        }
    }
}

@Composable
fun LineGraph(dataPoints: List<DataPoint>) {
    val color = remember { mutableStateOf(Color.Gray) }
    val offset = remember {
        mutableStateOf(Offset(0f, 0f))
    }
    Canvas(modifier = Modifier
        .height(300.dp)
        .fillMaxWidth()
        .pointerInput(Unit) {
//            detectTapGestures(
//                onDoubleTap = {
//                    color.value = Color.Red
//                },
//                onLongPress = { color.value = Color.Green },
//                onTap = { color.value = Color.Yellow },
//                onPress = {
//                    color.value = Color.Magenta
//                }
//            )
            detectDragGestures { change, _ ->
                change.consumeAllChanges()
                offset.value = change.position
            }
        }, onDraw = {
        drawCircle(color.value, radius = 100f, center = offset.value)
        val fX = size.width / dataPoints.size
        val fY = size.height / dataPoints.maxOf { it.y }

        var prevOffset: Offset? = null
        dataPoints.forEach { (x,y) ->
            val x1 = x * fX
            val y1 = size.height - (y * fY)
            drawCircle(Color.Blue, 10f, Offset(x1, y1))
        }
    })

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlotTheme {
        LineGraph(dataPoints)
    }
}

data class DataPoint(val x: Float, val y: Float)