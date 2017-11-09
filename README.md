## Building the Project 

##### Install Bazel

Bazel is the primary build system for TensorFlow.

1. Install the latest version of Bazel as per the instructions on the Bazel website.

### Android Studio

Android Studio can be used to build the demo in conjunction with Bazel. First,
look at [build.gradle](build.gradle) and make sure that the path to Bazel
matches that of your system.

At this point you can add the tensorflow/examples/android directory as a new
Android Studio project. Click through installing all the Gradle extensions it
requests, and you should be able to have Android Studio build the demo like any
other application (it will call out to Bazel to build the native code with the
NDK).
