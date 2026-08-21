import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.intellij.platform")
}

group = prop("pluginGroup")
version = prop("pluginVersion")

sourceSets {
  main {
    java.srcDir("src")
    resources.srcDir("resources")
    resources.srcDir("remoteInterpreter/resources")
  }
  test {
    java.srcDir("tests")
    java.include(
      "com/jetbrains/php/psalm/quality/tools/PsalmConfigFileFromComposerTest.java",
      "com/jetbrains/php/psalm/quality/tools/PsalmVersionValidatorTest.java",
    )
    resources.srcDir("testData")
  }
}
kotlin {
  sourceSets {
    main {
      kotlin.srcDir("src")
    }
    test {
      kotlin.srcDir("tests")
      kotlin.include(
        "com/jetbrains/php/tools/quality/psalm/PsalmOptionsPanelParityTest.kt",
      )
    }
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation("junit:junit:4.13.2")
  testRuntimeOnly(project(":remoteInterpreter"))

  intellijPlatform {
    psalmPlatform(project)
    pluginModule(runtimeOnly(project(":remoteInterpreter")))
  }
}

configureJvm()

tasks {
  processResources {
    filesMatching("intellij.psalm.remoteInterpreter.xml") {
      filter {
        when {
          it == "<idea-plugin>" -> "<idea-plugin package=\"com.intellij.psalm.remoteInterpreter\">"
          it.contains("<module name=\"intellij.php.backend\"/>") -> ""
          else -> it
        }
      }
    }
  }

  test {
    systemProperty("idea.home.path", projectDir.absolutePath)
  }

  wrapper {
    gradleVersion = prop("gradleVersion")
  }

  patchPluginXml {
    version = prop("pluginVersion")
    sinceBuild.set(prop("pluginSinceBuild"))
    prop("pluginUntilBuild").takeIf(String::isNotEmpty)?.let(untilBuild::set)
    doLast {
      val descriptor = outputFile.get().asFile
      val compatibleDescriptor = descriptor.readText()
        .replace("    <module name=\"intellij.php.backend\" />\r\n", "")
        .replace("    <module name=\"intellij.php.backend\" />\n", "")
        .replace(
          "    <plugin id=\"com.intellij.modules.ultimate\" />",
          "    <plugin id=\"com.intellij.modules.ultimate\" />\n    <plugin id=\"com.jetbrains.php\" />",
        )
      descriptor.writeText(compatibleDescriptor)
    }
  }

  verifyPlugin {
    failureLevel.set(
      failureLevel.get().filterNot { it == VerifyPluginTask.FailureLevel.INTERNAL_API_USAGES },
    )
  }
}
