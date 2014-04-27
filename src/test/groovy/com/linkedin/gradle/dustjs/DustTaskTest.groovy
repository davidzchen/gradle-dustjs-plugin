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

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

/**
 * Unit tests for DustTask.
 */
class DustTaskTest extends Specification {
  class FileTreeMock implements Iterable<File> {
    def dir
    def files

    def Iterator<File> iterator() {
      return files.iterator()
    }

    def boolean isEmpty() {
      return files.empty
    }
  }

  @Rule TemporaryFolder dir = new TemporaryFolder()

  Project project = ProjectBuilder.builder.build()
  DustTask dustjs

  def setup() {
    project.apply(plugin: DustPlugin)
    dustjs = project.tasks.dustjs
    dustjs.dest = dir.newFolder()
  }

  def getProvided(String name) {
    new File(Thread.currentThread().contextClassLoader.getResource(name).toURI())
  }

  def getGenerated(String name) {
    new File(dustjs.destDir, name)
  }

  def 'set source as a string'() {
    given:
    def provided = getProvided("template.tl")

    when:
    dustjs.source = provided.absolutePath

    then:
    dustjs.getSourceFiles().files.size() == 1
    dustjs.getSourceFiles.singleFile == provided
  }

  def 'sample run of dust'() {
    given:
    File sourceFile = getProvided("template.tl")
    dustjs.source = new FileTreeMock(dir: sourceFile.parentFile,
                                     files: [sourceFile])

    when:
    dustjs.run()

    then:
    def actual = getGenerated("template.js").readLines()
    def expected = getProvided("template.js").readLines()
    actual == expected
    dustjs.getLessPath() == 'dust-full-v2.3.4.js'
  }
}
