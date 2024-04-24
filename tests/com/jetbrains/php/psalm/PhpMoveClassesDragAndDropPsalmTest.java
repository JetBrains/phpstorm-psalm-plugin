package com.jetbrains.php.psalm;

import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import com.jetbrains.php.refactoring.PhpRefactoringBaseTest;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.refactoring.move.dnd.PhpMoveClassesDragAndDropTest.doMove;

public class PhpMoveClassesDragAndDropPsalmTest extends PhpRefactoringBaseTest {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "moveDragAndDrop";
  }

  public void testGenericsListType() throws Exception {
    doMove(myFixture, getTestDataPath(), getTestName(), "/N1", "N1", "/N");
  }
}
