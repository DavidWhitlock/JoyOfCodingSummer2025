package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for the <code>Student</code> class's main method.
 * These tests extend <code>InvokeMainTestCase</code> which allows them
 * to easily invoke the <code>main</code> method of <code>Student</code>.
 */
class StudentIT extends InvokeMainTestCase {

  @Test
  void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Student.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing information about the Student"));
  }

  @Disabled
  @Test
  void canCreateDaveStudent() {
    MainMethodResult result = invokeMain(Student.class, "Dave", "male", "3.64", "Algorithms", "Operating Systems", "Java");
    String expected = "Dave has a GPA of 3.64 and is taking 3 classes: Algorithms, Operating Systems, and Java.  He says \"This class is too much work\".";
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString(expected));
  }

  @Test
  void missingGenderPrintsMessageToStandardError() {
    MainMethodResult result = invokeMain(Student.class, "Dave");
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing gender"));
  }

  @Test
  void nameIsPrintedToStandardOutput() {
    String name = "Dave";
    MainMethodResult result = invokeMain(Student.class, name, "male", "3.64", "Algorithms", "Operating Systems", "Java");
    assertThat(result.getTextWrittenToStandardOut(), containsString(name));
  }

  @Test
  void missingGpaPrintsMessageToStandardError() {
    MainMethodResult result = invokeMain(Student.class, "Dave", "other");
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing GPA"));
  }

  @Test
  void gpaIsPrintedToStandardOutput() {
    String gpa = "3.64";
    MainMethodResult result = invokeMain(Student.class, "Dave", "male", gpa, "Algorithms", "Operating Systems", "Java");
    assertThat(result.getTextWrittenToStandardOut(), containsString(gpa));
  }

  @Test
  void malformedGpaPrintsMessageToStandardError() {
    String gpa = "blah";
    MainMethodResult result = invokeMain(Student.class, "Dave", "male", gpa, "Algorithms", "Operating Systems", "Java");
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid GPA: " + gpa));
  }

}
