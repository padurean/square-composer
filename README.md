# SquareComposer - Scala solutions for [CubeComposer](http://david-peter.de/cube-composer) 

## Get started

To get started, run `sbt ~fastOptJS` in this example project.
This should download dependencies and prepare the relevant javascript files.

Just open `localhost:12345/target/scala-2.11/classes/index-dev.html` in your browser.

You can then edit the application and see the updates be sent live to the browser
without needing to refresh the page.

You can also take a look at this [live demo](http://purecore.ro/square-composer).

Enjoy and feel free to propose new solutions!

## The optimized version

Run `sbt fullOptJS` and open up `index-opt.html` for an optimized (~200kb) version
of the final application, useful for final publication.

## Add a new solution to an existing transformation

Just add a new function to an existing `Transformation` from the `ro.purecore.squarecomposer.Transformations.definitions` list.

## Add a new transformation

Just add a new `Transformation` to the `ro.purecore.squarecomposer.Transformations.definitions` list.

TIP: Use an existing instantiation one as blueprint - code should be self-explanatory.
