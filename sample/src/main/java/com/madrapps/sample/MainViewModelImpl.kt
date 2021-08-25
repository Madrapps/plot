package com.madrapps.sample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madrapps.plot.line.DataPoint
import kotlin.random.Random

class MainViewModelImpl : MainViewModel, ViewModel() {

    val dataPoints1 = listOf(
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

    val dataPoints2 = listOf(
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

    override val line1: MutableState<List<DataPoint>> = mutableStateOf(dataPoints1)

    override fun change() {
        val nextInt = 1 + Random.Default.nextInt(5)
        val nextInt1 = 1 + Random.Default.nextInt(5)
        line1.value = dataPoints1.map { DataPoint(it.x * nextInt, it.y * nextInt1) }
    }
}

interface MainViewModel {

    val line1: State<List<DataPoint>>

    fun change()
}
