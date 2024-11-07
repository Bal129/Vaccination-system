package JavaDir;

import java.time.LocalDate;

public class Dose {
    private LocalDate appointment;
    private boolean complete;
    private Vaccine vaccine;

    ////////////// constructor //////////////
    
    public Dose() {}

    public Dose(LocalDate appointment, Boolean complete) {
        this.appointment = appointment;
        this.complete = complete;
    }

    ////////////// getter //////////////

    public LocalDate getAppointment() {
        return appointment;
    }

    public boolean isComplete() {
        return complete;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }
    ////////////// setter //////////////

    public void setAppointment(LocalDate appointment) {
        this.appointment = appointment;
    }

    public void switchComplete() {
        complete = !complete;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }
}
