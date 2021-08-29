package com.madrapps.sample.linegraph

import com.madrapps.plot.line.DataPoint

class DataPoints {
    companion object {

        val dataPoints1 = listOf(
            DataPoint(0f, 0f),
            DataPoint(1f, 0f),
            DataPoint(2f, 0f),
            DataPoint(3f, 0f),
            DataPoint(4f, 0f),
            DataPoint(5f, 25f),
            DataPoint(
                6f,
                75f
            ),
            DataPoint(7f, 100f),
            DataPoint(8f, 80f),
            DataPoint(9f, 75f),
            DataPoint(10f, 55f),
            DataPoint(11f, 45f),
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

        val dataPoints3 = listOf(
            DataPoint(7f, 100f),
            DataPoint(8f, 80f),
            DataPoint(9f, 75f),
            DataPoint(10f, 55f),
            DataPoint(11f, 45f),
            DataPoint(12f, 50f),
            DataPoint(13f, 80f),
            DataPoint(14f, 70f),
            DataPoint(15f, 25f),
            DataPoint(16f, 0f), // FIXME :Bug: Change this to 200f. Column doesn't adapt.
            DataPoint(17f, 50f),
        )

        val dataPoints4 = listOf(
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
        )

        val dataPoints5 = listOf(
            DataPoint(-10f, 55f),
            DataPoint(-9f, 75f),
            DataPoint(-8f, 80f),
            DataPoint(-7f, 100f),
            DataPoint(11f, 45f),
            DataPoint(12f, 50f),
            DataPoint(13f, 80f),
            DataPoint(14f, 70f),
            DataPoint(15f, 25f),
            DataPoint(16f, 0f), // FIXME :Bug: Change this to 200f. Column doesn't adapt.
            DataPoint(17f, 0f),
        )

        val dataPoints6 = listOf(
            DataPoint(-0.3f, -1f),
            DataPoint(-0.2f, 2f),
            DataPoint(-0.1f, 0.5f),
            DataPoint(-0f, 0.0f),
            DataPoint(0.1f, 0.3f),
            DataPoint(0.3f, 0.25f),
            DataPoint(0.4f, 0.75f),
            DataPoint(0.5f, 0.80f),
            DataPoint(0.8f, 1f),
            DataPoint(0.9f, 1.45f),
            DataPoint(1.2f, 0.50f),
            DataPoint(1.3f, 1.50f),
            DataPoint(1.4f, 0.80f),
            DataPoint(1.5f, 1.70f),
            DataPoint(1.6f, 2f),
            DataPoint(1.7f, 0f),
        )
    }
}
