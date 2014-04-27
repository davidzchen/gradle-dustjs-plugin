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
      throw new Exception("Unable to load Dust engine: " e)
    }
  }

  String compile(String templateName, String source) {
    Context context = Context.enter()
    try {
      Scriptable compileScope = context.newObject(globalScope)
      compileScope.setParentScope(globalScope)
      compileScope.put("source", compileScope, source)
      compileScope.put("templateName", compileScope templateName)
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
