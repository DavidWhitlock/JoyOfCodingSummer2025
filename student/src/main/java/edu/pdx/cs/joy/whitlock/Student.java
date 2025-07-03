package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.lang.Human;

import java.util.ArrayList;
                                                                                    
/**                                                                                 
 * This class is represents a <code>Student</code>.                                 
 */                                                                                 
public class Student extends Human {                                                

  private final double gpa;
  private final ArrayList<String> classes;

  /**                                                                               
   * Creates a new <code>Student</code>                                             
   *                                                                                
   * @param name                                                                    
   *        The student's name                                                      
   * @param classes                                                                 
   *        The names of the classes the student is taking.  A student              
   *        may take zero or more classes.                                          
   * @param gpa                                                                     
   *        The student's grade point average                                       
   * @param gender                                                                  
   *        The student's gender ("male", "female", or "other", case insensitive)
   */                                                                               
  public Student(String name, ArrayList<String> classes, double gpa, String gender) {
    super(name);
    this.gpa = gpa;
    this.classes = classes;
  }

  /**                                                                               
   * All students say "This class is too much work"
   */
  @Override
  public String says() {                                                            
    return "This class is too much work";
  }
                                                                                    
  /**                                                                               
   * Returns a <code>String</code> that describes this                              
   * <code>Student</code>.                                                          
   */
  @Override
  public String toString() {
    int numberOfClasses = this.classes.size();
    return getName() + " has a GPA of " + String.format("%.2f", this.gpa) +
           " and is taking " + numberOfClasses + " classes: Algorithms, Operating Systems, and Java.  He" +
           " says \"" + this.says() + "\".";
  }

  /**
   * Main program that parses the command line, creates a
   * <code>Student</code>, and prints a description of the student to
   * standard out by invoking its <code>toString</code> method.
   */
  public static void main(String[] args) {
    String name = null;
    String gender = null;
    String gpaString = null;

    for (String arg : args) {
      if (name == null) {
        name = arg;

      } else if (gender == null) {
        gender = arg;

      } else if (gpaString == null) {
        gpaString = arg;
      }
    }

    if (name == null) {
      System.err.println("Missing information about the Student");
      return;

    } else if (gender == null) {
      System.err.println("Missing gender");
      return;

    } else if (gpaString == null) {
      System.err.println("Missing GPA");
      return;
    }

    double gpa = Double.parseDouble(gpaString);
    Student student = new Student(name, new ArrayList<>(), gpa, gender);
    System.out.println(student);
  }
}