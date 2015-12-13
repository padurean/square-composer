# [SquareComposer](http://purecore.ro/square-composer) - Scala solutions for [CubeComposer](http://david-peter.de/cube-composer) 

[![Join the chat at https://gitter.im/padurean/square-composer](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/padurean/square-composer?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Get started

To get started, run `sbt ~fastOptJS` in this example project.
This should download dependencies and prepare the relevant javascript files.

Just open `localhost:12345/target/scala-2.11/classes/index-dev.html` in your browser.

You can then edit the application and see the updates be sent live to the browser
without needing to refresh the page.

You can also take a look at this [published version](http://purecore.ro/square-composer).

Enjoy and feel free to propose new solutions!

## The optimized version

Run `sbt fullOptJS` and open up `index-opt.html` for an optimized (~200kb) version
of the final application, useful for final publication.

## Contribute!

Fork this repo, do your changes/additions and create a PullRequest to get them
also back here.

Your changes will also be published [here](http://purecore.ro/square-composer),
as an updated version of _SquareComposer_.

### Add a new solution to an existing transformation

Just add a new function to an existing `Transformation` from the `ro.purecore.squarecomposer.Transformations.definitions` list.

### Add a new transformation

Just add a new `Transformation` to the `ro.purecore.squarecomposer.Transformations.definitions` list.

_TIP_: Use an existing one as blueprint - code should be self-explanatory.
