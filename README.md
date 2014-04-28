Gradle Dust.js Plugin
=====================

A Gradle plugin that compiles [Dust.js](http://linkedin.github.io/dustjs/)
templates.

This plugin requires [LinkedIn Dust.js]

Usage
-----

### Add the plugin ###

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

### Activate the plugin ###

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

### Acknowledgements ###

This plugin makes use of code adapted from @obecker's [Gradle LESS plugin](https://github.com/obecker/gradle-lesscss-plugin)
and @jmparson's [Play Framework Dust.js plugin](https://github.com/jmparsons/play-dustjs).
