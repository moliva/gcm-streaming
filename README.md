# GCM Streaming
*GCM Streaming* is a framework for developing Streaming applications based on [GCM/Proactive](https://github.com/scale-proactive/scale-proactive) platform for component-oriented programming in computing grids. The framework provides abstractions for the most common kind of operations in a streaming applications as well as a standard way to execute an application built on top of it.

## Features
* Base abstractions for operations
  * InTap
  * Operator
    * Combinator
  * OutTap
* Common operations for each kind and extensibility for the application developer to build its own abstractions enabling best practices (e.g. easily testable behavior)
* Window configuration management
  * Tumbling strategies based on count and time
  * Sliding
    * Eviction policies based on count and time
    * Trigger policies based on count and time
* Basic runner
* Dynamic adaptability
  * Reconfiguration of components attributes

## Future development
* Dynamic adaptability
  * Adaptation of the operation graph
  * Autonomic adaptation
* DSL
* Improve type system
