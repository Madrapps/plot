# plot
A compose library with different Graphs and Charts (**currently supports only Line graph, more types will
be added soon**)

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
- Includes `LineGraph`
- Full customization of the various parts of the graph (like the point, line between the points, highlight 
  when selected, the values in x-axis and y-axis, etc...)

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

*plot* by [Madrapps](http://madrapps.github.io/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
