# plot
An android compose library with different Graphs and Charts (*currently supports only Line graph, more types will
be added soon*)

<img src="/preview/line_graph_recording.gif" alt="Line Graph 1" width="600"/>

<img src="/preview/line_graph_1.png" alt="Line Graph 1" title="Line Graph 1" width="400"/>&nbsp;&nbsp;<img src="/preview/line_graph_2.png" alt="Line Graph 2" title="Line Graph 2" width="400"/>&nbsp;&nbsp;<img src="/preview/line_graph_3.png" alt="Line Graph 3" title="Line Graph 3" width="400" />&nbsp;&nbsp;<img src="/preview/line_graph_4.png" alt="Line Graph 4" title="Line Graph 4" width="400" />

Download
-----

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.github.madrapps:plot:0.1.0'
}
```

Features
-----
- Full customization of the various parts of the graph (like the point, line between the points, highlight 
  when selected, the values in x-axis and y-axis, etc...)
- Supports scrolling, zooming and touch drag selection

Usage
-----
Just add the `LineGraph` composable and pass it a `LinePlot` with all your configuration and customisation.
Please take a look at the [sample](https://github.com/Madrapps/plot/tree/main/sample) app to see the various
customisations available.

```kotlin
  val sample = "sample"
```

License
-----

**plot** by [Madrapps](http://madrapps.github.io/) is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
