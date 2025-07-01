package edu.pdx.cs.joy.whitlock;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Unit tests for the Student class.  In addition to the JUnit annotations,
 * they also make use of the <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a>
 * matchers for more readable assertion statements.
 */
public class StudentTest
{

  @Test
  void studentNamedPatIsNamedPat() {
    String name = "Pat";
    var pat = createStudentNamed(name);
    assertThat(pat.getName(), equalTo(name));
  }

  private static Student createStudentNamed(String name) {
    return new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
  }

  @Test
  void allStudentsSayThisClassIsTooMuchWork() {
    var pat = createStudentNamed("Pat");
    assertThat(pat.says(), equalTo("This class is too much work"));
  }

  @Disabled
  @Test
  void daveStudentHasExpectedToString() {
    ArrayList<String> classes = new ArrayList<>(List.of("Algorithms", "Operating Systems", "Java"));
    Student dave = new Student("Dave", classes, 3.64, "male");
    String expectedToString = "Dave has a GPA of 3.64 and is taking 3 classes: Algorithms, Operating Systems, and Java.  He says \"This class is too much work\".";
    assertThat(dave.toString(), equalTo(expectedToString));
  }

  @Test
  void toStringHasTwoDigitsOfPrecisionForGPA() {
    ArrayList<String> classes = new ArrayList<>(List.of("Java", "Rust", "Algorithms"));
    Student sarah = new Student("Sarah", classes, 3.5, "female");
    assertThat(sarah.toString(), containsString(" has a GPA of 3.50"));
  }

}
