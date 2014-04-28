Gradle Dust.js Plugin [![Build Status](https://travis-ci.org/davidzchen/gradle-dustjs-plugin.svg?branch=master)](https://travis-ci.org/davidzchen/gradle-dustjs-plugin)
=====================

A Gradle plugin that compiles templates. This plugin requires [LinkedIn Dust.js](http://linkedin.github.io/dustjs).

Usage
-----

### Add the plugin ###

Add a buildscript dependency on the plugin to pull the artifact for the plugin.

```groovy
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'com.linkedin:gradle-dustjs-plugin:1.0.0'
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
