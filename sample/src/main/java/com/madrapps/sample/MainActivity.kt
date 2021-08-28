package com.madrapps.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.madrapps.sample.linegraph.LineGraph1
import com.madrapps.sample.linegraph.LineGraph2
import com.madrapps.sample.linegraph.LineGraph3
import com.madrapps.sample.linegraph.LineGraph4
import com.madrapps.sample.linegraph.LineGraph5
import com.madrapps.sample.linegraph.LineGraph6
import com.madrapps.sample.linegraph.LineGraph7
import com.madrapps.sample.linegraph.LineGraph8
import com.madrapps.sample.linegraph.LineGraph9
import com.madrapps.sample.ui.theme.PlotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainViewModelImpl by viewModels()
        setContent {
            PlotTheme {
                LazyColumn(Modifier.fillMaxWidth()) {
                    itemsIndexed(model.lines.value) { i, item ->
                        when (i) {
                            0 -> LineGraph3(item)
                            1 -> LineGraph2(item)
                            2 -> LineGraph1(item)
                            3 -> LineGraph4(item)
                            4 -> Column(Modifier.fillMaxWidth()) {
                                LineGraph5(
                                    item,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                            5 -> Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)) {
                                LineGraph6(
                                    item,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                            6 -> LineGraph7(item)
                            7 -> LineGraph8(item)
                            8 -> LineGraph9(item)
                        }
                    }
                }
            }
        }
    }
}
