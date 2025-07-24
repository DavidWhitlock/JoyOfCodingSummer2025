package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
  private final String owner;
  private final Collection<Appointment> appointments;

  public AppointmentBook(String owner) {
    this.owner = owner;
    appointments = new ArrayList<>();
  }

  @Override
  public String getOwnerName() {
    return owner;
  }

  @Override
  public Collection<Appointment> getAppointments() {
    return appointments;
  }

  @Override
  public void addAppointment(Appointment appointment) {
    this.appointments.add(appointment);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AppointmentBook book)) return false;
    return Objects.equals(owner, book.owner) && Objects.equals(appointments, book.appointments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, appointments);
  }
}
