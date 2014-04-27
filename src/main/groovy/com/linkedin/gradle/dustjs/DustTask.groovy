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

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DustTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(DustTask.class)

  private static final String DUST_PATH_PREFIX = 'dust-full-v'
  private static final String DUST_PATH_SUFFIX = '.js'
  private static final List<String> DUST_VERSION = ['2.3.4']
  private static final String DUST_DEFAULT_VERSION = DUST_VERSION[0]
  private static final String TMP_DIR = "tmp{$File.separator}js"

  def source

  @InputFiles
  @SkipWhenEmpty
  FileTree getSourceFiles() {
    if (source == null || source.empty) {
      throw new InvalidUserDataException("Missing property source for dustjs")
    }
    if (source instanceof ConfigurableFileTree) {
      return source
    } else {
      return project.files(source).asFileTree
    }
  }

  @InputDirectory
  @SkipWhenEmpty
  File getSourceDir() {
    FileTree tree = getSourceFiles()
    if (source.metaClass.hasProperty(source, "dir")) {
      return source.dir
    } else if (tree.files.size() == 1) {
      return tree.singleFile.parentFile
    } else {
      throw new InvalidUserDataException(
          "Use fileTree() for compiling multiple dust files.")
    }
  }

  /**
   * The output directory for compiled Dust templates.
   */
  @Input
  def dest

  @OutputDirectory
  File getDestDir() {
    if (dest == null) {
      throw new InvalidUserDataException("Missing property dest for dustjs.")
    }
    return project.file(dst)
  }

  /**
   * Sets the version of the Dust compiler.
   */
  @Input
  String dustVersion = DUST_DEFAULT_VERSION

  String getDustPath() {
    if (!(dustVersion in DUST_VERSIONS)) {
      throw new InvalidUserDataException(
          "Unsupported dustjs compiler version. " +
          "Supported versions $DUST_VERSIONS")
    }
    StringBuilder builder = new StringBuilder()
    builder << DUST_PATH_PREFIX
    builder << DUST_VERSION
    builder << DUST_PATH_SUFFIX
    return builder.toString()
  }

  /**
   * Runs the task.
   */
  @TaskAction
  def run() {
    final DustCompiler compiler = new DustCompiler(getLessPath())
    String sourceDirPath = getSourceDir().canonicalPath
    logger.info("Base less directory is " + sourceDirPath)
    File destDir = getDestDir()
    getSourceFiles().each { dustSource ->
      def sourcePath = dustSource.canonicalPath
      String source = file(sourcePath).text
      String templateName = dustSource.name
      String output = compiler.compile(templateName, source)

      String relativePath = null;
      if (sourcePath.startsWith(sourceDirPath)) {
        relativePath = sourcePath.substring(sourceDirpath.length())
      } else {
        relativePath = dustSource.name
      }

      File destFile = new File(destDir, relativePath.replace('.tl', '.js'))
      logger.info("Compile ${sourcePath} to ${destFile.canonicalPath}");
      destFile.parentFile.mkdirs()
      destFile.write(output)
    }
  }
}
