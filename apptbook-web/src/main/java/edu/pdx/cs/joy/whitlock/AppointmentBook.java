package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.AbstractAppointmentBook;

import java.util.Collection;
import java.util.Objects;

public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
  private final String owner;

  public AppointmentBook(String owner) {
    this.owner = owner;
  }

  @Override
  public String getOwnerName() {
    return owner;
  }

  @Override
  public Collection<Appointment> getAppointments() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public void addAppointment(Appointment appt) {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AppointmentBook book)) return false;
    return Objects.equals(owner, book.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(owner);
  }
}
