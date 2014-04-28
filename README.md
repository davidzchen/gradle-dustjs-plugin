Gradle Dust.js Plugin [![Build Status](https://travis-ci.org/davidzchen/gradle-dustjs-plugin.svg?branch=master)](https://travis-ci.org/davidzchen/gradle-dustjs-plugin)
=====================

A Gradle plugin that compiles templates. This plugin requires [LinkedIn Dust.js](http://linkedin.github.io/dustjs) and uses [Rhino](http://mozilla.org/rhino) to run the Dust compiler.

Usage
-----

### Add the plugin ###

**Note: We are still in the process of publishing the artifact. For now, please build from source and [include the JAR](http://www.gradle.org/docs/current/userguide/custom_plugins.html). Instructions on pulling the artifact will be aded once it is published.**

First, build this plugin from source:

```
$ gradle build
```

The jar will be in `build/libs/gradle-dustjs-plugin-1.0.jar`. Then, create a directory `gradle/plugin` in the root of your source tree, and copy the jar into the directory.

Finally, add a buildscript dependency in your project's `build.gradle`:

```groovy
buildscript {
  dependencies {
    classpath 'org.mozilla:rhino:1.7R4'
    classpath files('gradle/plugin/gradle-dustjs-plugin-1.0.jar'
  }
}
```

### Apply the plugin ###

```groovy
apply plugin: 'dustjs'
```

### Configure the Plugin ###

```groovy
dustjs {
  source = fileTree('src/main/tl') {
    include 'template.tl'
  }
  dest = 'src/main/webapp/assets/js'
}
```

Tasks
-----

This plugin adds the `dustjs` task and the implicit `cleanDustjs` task.

### dustjs ###

The `dustjs` task compiles the specified Dust .tl files from the `source` directory into .js files into the `dest` directory.

The basename of the template file is used as the template name. For example, if one of the files to be compiled is `template.tl`, then running the plugin is equivalent to the following `dustc` invocation:

```
$ dustc --name=template source/template.tl dest/template.js
```

### cleanDustjs ###

The `cleanDustjs` task completely deletes the `dest` directory.

Acknowledgements
----------------

This plugin makes use of code adapted from Oliver Becker's [Gradle LESS plugin](https://github.com/obecker/gradle-lesscss-plugin)
and Jonathan Parsons' [Play Framework Dust.js plugin](https://github.com/jmparsons/play-dustjs).
