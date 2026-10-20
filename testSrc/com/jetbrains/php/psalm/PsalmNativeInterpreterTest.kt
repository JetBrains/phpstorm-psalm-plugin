// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.php.psalm

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.platform.testFramework.junit5.codeInsight.fixture.codeInsightProjectFixture
import com.intellij.testFramework.IndexingTestUtil
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.TestDisposable
import com.jetbrains.php.tools.quality.NativeQualityToolInterpreter
import com.jetbrains.php.tools.quality.psalm.PsalmConfiguration
import com.jetbrains.php.tools.quality.psalm.PsalmGlobalInspection
import com.jetbrains.php.tools.quality.psalm.PsalmOptionsConfiguration
import com.jetbrains.php.tools.quality.psalm.PsalmProjectConfiguration
import com.jetbrains.php.tools.quality.psalm.PsalmQualityToolType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@TestApplication
class PsalmNativeInterpreterTest {
  companion object {
    private val projectFixture = codeInsightProjectFixture()
  }

  private val project get() = projectFixture.get()

  @TestDisposable
  lateinit var testRootDisposable: Disposable

  @BeforeEach
  fun waitForIndexes() {
    IndexingTestUtil.waitUntilIndexesAreReady(project)
  }

  @Test
  fun testConfigAndFileArePathsInTheEnvironment() {
    val native = NativeQualityToolInterpreter.select(project, PsalmQualityToolType.INSTANCE, PsalmProjectConfiguration.getInstance(project), testRootDisposable) { id ->
      object : PsalmConfiguration() {
        override fun getId(): String = id
        override fun getInterpreterId(): String = id
      }
    }
    val options = PsalmOptionsConfiguration.getInstance(project)
    val savedConfig = options.config
    Disposer.register(testRootDisposable) { options.config = savedConfig }
    options.config = native.hostPath("/home/user/project/psalm.xml")

    val commandLine = PsalmGlobalInspection.getBaseCommandLineOptions(native.hostPath("/home/user/project/src/Foo.php"), project)

    assertEquals("/home/user/project/psalm.xml", commandLine[commandLine.indexOf("-c") + 1])
    assertEquals("/home/user/project/src/Foo.php", commandLine.last())
  }
}
