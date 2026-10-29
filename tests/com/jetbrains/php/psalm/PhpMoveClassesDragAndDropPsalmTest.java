package com.jetbrains.php.psalm;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import com.jetbrains.php.refactoring.PhpRefactoringTestFixture;
import org.junit.jupiter.api.Test;

import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;
import static com.jetbrains.php.refactoring.PhpRefactoringTestFixture.phpRefactoringFixture;
import static com.jetbrains.php.refactoring.move.dnd.PhpMoveClassesDragAndDropTest.doMove;

@TestApplication
public class PhpMoveClassesDragAndDropPsalmTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpRefactoringTestFixture> php =
    phpRefactoringFixture(projectFixture, new PhpTestDataLayout("moveDragAndDrop").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME));

  @Test
  public void testGenericsListType() throws Exception {
    doMove(php.get().getFixture(), php.get().getTestDataPath(), php.get().getTestName(), "/N1", "N1", "/N");
  }
}
