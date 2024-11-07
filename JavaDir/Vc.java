package JavaDir;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class Vc extends Detail {
    private int capacity;
    private int totalVacCount;
    private LocalDate todayDate;

    ArrayList<VcDaily> dailys;
    ArrayList<Recipient> recipients;
    LinkedList<Vaccine> vaccines = new LinkedList<>();

    ////////////// constructor //////////////

    Vc() {
        setDailys(new ArrayList<>());
        setRecipients(new ArrayList<>());
    }
    
    Vc(int id, String name, String password, String phone) {
        super(id, name, password, phone);
        setDailys(new ArrayList<>());
        setRecipients(new ArrayList<>());
    }

    Vc(int id, String name, String password, String phone, int capacity, 
        int totalVacCount, ArrayList<VcDaily> dailys, ArrayList<Recipient> recipients) {
        super(id,name, password, phone);
        setCapacity(capacity);
        setTotalVacCount(totalVacCount);
        setDailys(dailys);
        setRecipients(recipients);
    }

    ////////////// getter //////////////
    /**
     * 
     * @param id
     * @return
     */
    public Recipient getRecipient(int id) {
        for (Recipient recipient : recipients)
            if (recipient.getId() == id)
                return recipient;
        return null;
    }

    public ArrayList<Recipient> getRecipients() {
        return recipients;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTotalVacCount() {
        return totalVacCount;
    }
   
    public boolean isAvailable(LocalDate date) { //to see if cap has been reached
        return (capacity>getDaily(date).getTotalRecipient());
    }

    public LocalDate getTodayDate() {
        return todayDate;
    }

    public VcDaily getDaily(LocalDate date){
        for(int i=0 ;i<dailys.size(); i+=1){
            if(dailys.get(i).getDate().equals(date))
                return dailys.get(i);
        }
        return null;
    }

    public ArrayList<VcDaily> getDailys(){
        return dailys;
    }

    public LinkedList<Vaccine> getVaccines() {
        return vaccines;
    }

    public int getCurrentVac() {
        return vaccines.size();
    }

    ////////////// setter //////////////

    public void setVaccines(LinkedList<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }

    public void setRecipients(ArrayList<Recipient> recipients){
        this.recipients = recipients;
    }

    public void setDailys(ArrayList<VcDaily> dailys){
        this.dailys = dailys;
    }

    public void addRecipient(Recipient recipient) {
        recipients.add(recipient);
    }

    public void addVaccine(Vaccine vaccine) {
        vaccines.add(vaccine);
    }

    public void setCapacity(int capacity) { // to update current capacity count
        if(capacity <= 0)
            throw new IllegalArgumentException("Invalid setCapacity(int capacity)");
        this.capacity = capacity;
    }

    public void setTotalVacCount(int totalVacCount) {
        if(totalVacCount < 0)
            throw new IllegalArgumentException("Invalid setTotalVacCount(int totalVacCount)");
        this.totalVacCount = totalVacCount;

    }

    public void addTotalVacCount(int quantity) {
        if(quantity < 0)
             throw new IllegalArgumentException("Invalid addTotalVacCount(int quantity)");
        totalVacCount += quantity;
    }

    public void addTotalVacCount() {
        addTotalVacCount(1);
    }

    /**
     * Set appointment date for recipient
     * @param appointment   - date of appointment
     * @param recipientID   - Recipient ID
     * @param doseCount     - 1st or 2nd dose
     */
    public void setAppointment(LocalDate appointment, int recipientID, int vcId, int doseCount) {
        VcDaily temp = getDaily(appointment);
        if (temp==null) {
            temp = new VcDaily();
            temp.setDate(appointment);
            temp.setVcId(vcId);
            dailys.add(temp);
        }
        if (temp.getTotalRecipient() >= capacity)
            throw new ArithmeticException("Vaccination Center is full on " + appointment);

        temp.addTotalRecipient();
        Recipient recipient = getRecipient(recipientID);
        recipient.getStatus().getDose(doseCount).setAppointment(appointment);
        recipient.getStatus().switchPending();
    }

    /**
     * Update Vaccination Status once dose received
     * @param recipientID   - Recipient ID
     * @param doseCount     - 1st or 2nd dose
     */
    public void updateStatus(int recipientID, int doseCount) throws Exception {
        addTotalVacCount();
        Recipient recipient = getRecipient(recipientID);
        recipient.getStatus().getDose(doseCount).switchComplete();
        if (doseCount==1)
            recipient.getStatus().switchPending();
    }

    /**
     * Update Vaccination Status once dose received
     * @param recipientID   - Recipient ID
     * @param doseCount     - 1st or 2nd dose
     * @param vaccine       - vaccine
     */
    public void updateStatus(int recipientID, int doseCount, Vaccine vaccine) throws Exception {
        updateStatus(recipientID, doseCount);
        getRecipient(recipientID).getStatus().getDose(doseCount).setVaccine(vaccine);
    }

    @Override
    public String toString() {
        return  getId()                 + ","
                + getName()             + ","
                + getPassword()         + ","
                + getPhone()            + ","
                + getCapacity()         + ","
                + getTotalVacCount()    + ","
                + getTodayDate();
    }
}