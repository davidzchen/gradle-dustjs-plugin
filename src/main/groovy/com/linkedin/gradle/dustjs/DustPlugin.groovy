package com.linkedin.gradle.dustjs

import org.gradle.api.Plugin
import org.gradle.api.Project

class DustPlugin implements Plugin<Project> {
  void apply(final Project project) {
    configureDependencies(project)
    project.task('dustjs',
                 type: DustTask,
                 group: 'Build',
                 description: 'Compile Dust .tl files into .js files.')
  }

  void configureDependencies(final Project project) {
    project.configurations {
      rhino
    }
    project.repositories {
      mavenCentral()
    }
    project.dependencies {
      rhino 'org.mozilla:rhino:1.7R4'
    }
  }
}
