package JavaDir;

import java.time.LocalDate;

public class Recipient extends Detail {
    private VcStatus status;
    private int age;

    ////////////// constructor //////////////

    public Recipient(int id, String name, String password, String phone, int age) {
        super(id, name, password, phone);
        setStatus(new VcStatus());
        setAge(age);
    }

    public Recipient(int id, String name, String password, String phone, VcStatus status, int age) {
        super(id, name, password, phone);
        setStatus(status);
        setAge(age);
    }

    ////////////// getter //////////////

    public VcStatus getStatus() {
        return status;
    }

    public int getAge() {
        return age;
    }

    ////////////// setter //////////////

    public void setStatus(VcStatus status) {
        this.status = status;
    }

    public void setAge(int age) {
        if(age <= 0)
            throw new IllegalArgumentException("Invalid Age");
        this.age = age;
    }

    @Override
    public String toString() {
        String dose1String = "null";
        LocalDate dose1 = getStatus().getDose(1).getAppointment();
        String dose2String = "null";
        LocalDate dose2 = getStatus().getDose(2).getAppointment();
        if (dose1 != null)
            dose1String = dose1.format(Keys.FORMATTER);
        if (dose2 != null)
            dose2String = dose2.format(Keys.FORMATTER);

        return  getId()                                                             + ","
                + getName()                                                         + ","
                + getPassword()                                                     + ","
                + getPhone()                                                        + ","
                + getStatus().getVcId()                                             + ","
                + getStatus().isPending()                                           + ","
                + dose1String                                                       + ","
                + getStatus().getDose(1).isComplete()                               + ","
                + dose2String                                                       + ","
                + getStatus().getDose(2).isComplete()                               + ","
                + getAge();
    }
}
