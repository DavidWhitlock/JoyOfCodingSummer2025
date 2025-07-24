package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.AbstractAppointment;

import java.util.Objects;

public class Appointment extends AbstractAppointment {
  private final String description;

  public Appointment(String description) {
    this.description = description;
  }

  @Override
  public String getBeginTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getEndTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Appointment that)) return false;
    return Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(description);
  }
}
