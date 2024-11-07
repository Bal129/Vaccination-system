package JavaDir;

public class VcStatus {
    public static final int PENDING              = 0;
    public static final int DOSE_1_APPOINTMENT   = 1;
    public static final int DOSE_1_COMPLETED     = 2;
    public static final int DOSE_2_APPOINTMENT   = 3;
    public static final int DOSE_2_COMPLETED     = 4;

    private int vcId = -1;
    private boolean pending = true;
    private Dose[] doses = new Dose[2];

    ////////////// constructor //////////////

    public VcStatus() {
        Dose[] newDose = { new Dose(), new Dose() };
        setDose(newDose);
    }

    public VcStatus(int vcId, Dose[] doses) {
        setVcId(vcId);
        setDose(doses);
        if (
            (getDose(1).getAppointment() != null && !getDose(1).isComplete()) 
            || getDose(2).getAppointment() != null 
            || getDose(2).isComplete()
        )
            switchPending();
    }

    ////////////// getter //////////////

    public boolean isPending() {
        return pending;
    }

    public int getVcId() {
        return vcId;
    }

    public Dose getDose(int doseCount) {
        return doses[doseCount - 1];
    }

    public int getCurrentStatus() {
        Dose dose1 = getDose(1);
        Dose dose2 = getDose(2);

        if (!dose1.isComplete() && dose1.getAppointment() != null)
            return DOSE_1_APPOINTMENT;
        else if (!dose2.isComplete() && dose2.getAppointment() != null)
            return DOSE_2_APPOINTMENT;
        else if (dose1.isComplete() && !dose2.isComplete())
            return DOSE_1_COMPLETED;
        else if (dose2.isComplete())
            return DOSE_2_COMPLETED;
        return PENDING;
    }

    ////////////// setter //////////////

    public void setVcId(int vcId) {
        this.vcId = vcId;
    }

    public void setDose(Dose[] doses) {
        this.doses[0] = doses[0];
        this.doses[1] = doses[1];
    }

    public void switchPending() {
        this.pending = !pending;
    }

    public void setVaccine(Vaccine vaccine) {
        if (getCurrentStatus() == VcStatus.DOSE_1_APPOINTMENT) {
            getDose(1).setVaccine(vaccine);
            vaccine.setRecipientDose(1);
        } else {
            getDose(2).setVaccine(vaccine);
            vaccine.setRecipientDose(2);
        }
    }

   
}
