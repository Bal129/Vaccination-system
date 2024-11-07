package JavaDir;

import java.time.LocalDate;

public class VcDaily {
    private LocalDate date;
    private int totalRecipient;
    private int vcId;

    public VcDaily(){}

    public VcDaily(int vcId) {
        setVcId(vcId);
    }

    public VcDaily(int vcId, LocalDate date, int totalRecipient){
        setVcId(vcId);
        setDate(date);
        setTotalRecipient(totalRecipient);
    }

    ////////getter//////////

    public int getVcId() {
        return vcId;
    }

    public LocalDate getDate(){
        return date;
    }

    public int getTotalRecipient(){
        return totalRecipient;
    }

    ///////setter///////////
    public void setVcId(int vcId) {
        this.vcId = vcId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalRecipient(int totalRecipient) {
        if(totalRecipient < 0)
            throw new IllegalArgumentException("Invalid setTotalRecipient(int totalRecipient)");
        this.totalRecipient = totalRecipient;

    }

    public void addTotalRecipient(int amount) {
        if(amount < 0)
            throw new IllegalArgumentException("Invalid addTotalRecipient(int amount)");
        totalRecipient += amount;
    }

    public void addTotalRecipient(){
        addTotalRecipient(1);
    }

    @Override
    public String toString() {
        return  getTotalRecipient()      + ","
                + getDate();
    }

    public String toTxtString() {
        return  String.valueOf(vcId)                   + ","
                + getTotalRecipient()                  + ","
                + getDate().format(Keys.FORMATTER);
    }
}
