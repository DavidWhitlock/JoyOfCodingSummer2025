package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.AbstractAppointment;

public class Appointment extends AbstractAppointment {

    private final String description;

    public Appointment(String description) {
        this.description = description;
    }

    @Override
    public String getBeginTimeString() {
        return "BEGIN";
    }

    @Override
    public String getEndTimeString() {
        return "END";
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
