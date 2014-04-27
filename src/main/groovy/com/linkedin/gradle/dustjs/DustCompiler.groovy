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

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class DustCompiler {
  Scriptable globalScope

  public DustCompiler(String fileName) {
    try {
      final ClassLoader classLoader = Thread.getCurrentThread.contextClassLoader
      final InputStream inputStream = classLoader.getResourceAsStream(path)

      Context context = Context.enter()
      context.setOptimizationLevel(9)
      try {
        globalScope = context.initStandardObjects()
        context.evaluateReader(globalScope, dustReader, filename, 0, null)
      } finally {
        Context.exit()
      }
    } catch (Exception e) {
      throw new Exception("Unable to load Dust engine: " + e)
    }
  }

  String compile(String templateName, String source) {
    Context context = Context.enter()
    try {
      Scriptable compileScope = context.newObject(globalScope)
      compileScope.setParentScope(globalScope)
      compileScope.put("source", compileScope, source)
      compileScope.put("templateName", compileScope, templateName)
      try {
        return (String)context.evaluateString(
            compileScope, "(dust.compile(source, templateName))",
            "Dust compile command", 0, null)
      } catch (Exception e) {
        throw new Exception("Failed to compile Dust template: " + e)
      }
    } finally {
      Context.exit()
    }
  }
}
