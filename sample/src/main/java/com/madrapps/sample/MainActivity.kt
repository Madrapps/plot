package com.madrapps.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.madrapps.sample.linegraph.LineGraph1
import com.madrapps.sample.linegraph.LineGraph2
import com.madrapps.sample.linegraph.LineGraph3
import com.madrapps.sample.ui.theme.PlotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainViewModelImpl by viewModels()
        setContent {
            PlotTheme {
                LazyColumn() {
                    itemsIndexed(model.lines.value) { i, item ->
                        when (i) {
                            0 -> LineGraph1(item)
                            1 -> LineGraph2(item)
                            2 -> LineGraph3(item)
                        }
                    }
                }
            }
        }
    }
}
