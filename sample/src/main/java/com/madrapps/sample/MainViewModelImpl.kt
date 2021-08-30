package com.madrapps.sample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madrapps.plot.line.DataPoint
import com.madrapps.sample.linegraph.DataPoints.dataPoints1
import com.madrapps.sample.linegraph.DataPoints.dataPoints2
import com.madrapps.sample.linegraph.DataPoints.dataPoints3
import com.madrapps.sample.linegraph.DataPoints.dataPoints4
import com.madrapps.sample.linegraph.DataPoints.dataPoints6
import kotlin.random.Random

class MainViewModelImpl : MainViewModel, ViewModel() {

    override val line1: MutableState<List<DataPoint>> = mutableStateOf(dataPoints1)

    override fun change() {
        val nextInt = 1 + Random.Default.nextInt(5)
        val nextInt1 = 1 + Random.Default.nextInt(5)
        line1.value = dataPoints1.map { DataPoint(it.x * nextInt, it.y * nextInt1) }
    }

    override val lines: State<List<List<List<DataPoint>>>> = mutableStateOf(
        listOf(
            listOf(dataPoints1, dataPoints2),
            listOf(dataPoints3, dataPoints2),
            listOf(dataPoints1, dataPoints2),
            listOf(dataPoints1, dataPoints2),
            listOf(dataPoints6, dataPoints4),
        )
    )
}

interface MainViewModel {

    val line1: State<List<DataPoint>>
    val lines: State<List<List<List<DataPoint>>>>

    fun change()
}
