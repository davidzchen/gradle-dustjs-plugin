/*
 * Copyright 2012 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.linkedin.gradle.dustjs

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle dustjs plugin. Adds the <code>dustjs</code> task to the current
 * project.
 */
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
