package JavaDir;

import java.util.ArrayList;

public class MOH extends Detail {

    private ArrayList<Vc> vcs;
    private ArrayList<Recipient> recipients;

    ////////////// constructor //////////////

    public MOH() {
        vcs = new ArrayList<>();
        recipients = new ArrayList<>();
    }

    public MOH(int id, String name, String password, String phone) {
        super(id, name, password, phone);
        vcs = new ArrayList<>();
        recipients = new ArrayList<>();
    }

    public MOH(int id, String name, String password, String phone, ArrayList<Recipient> recipients, ArrayList<Vc> vcs) {
        super(id, name, password, phone);
        this.vcs = vcs;
        this.recipients = recipients;
    }

    ////////////// getter //////////////

    public Vc getVc( int vcId ) {
        for (int i=0; i<vcs.size(); i++) {
            Vc check = vcs.get(i);
            if (check.getId() == vcId)
                return check;
        }
        return null;
    }

    public Recipient getRecipient( int recipientID ) {
        for (int i=0; i<recipients.size(); i++) {
            Recipient check = recipients.get(i);
            if (check.getId() == recipientID) 
                return check;
        }
        return null;
    }

    ////////////// setter //////////////

    public void addRecipient(Recipient recipient) {
        recipients.add(recipient);
    }

    public void addVC(Vc vc) {
        vcs.add(vc);
    }

    ////////////// functions //////////////

    /**
     * Add Vaccine stocks at the Vaccination Center
     * @param vcId      -   Vaccination Center ID
     * @param quantity  -   Vaccine received
     */
    public void distributeVaccine(int vcId, int quantity) throws Exception {
        for(int i=0; i<quantity; i++) {
            Vaccine vaccine = new Vaccine(vcId);
			getVc(vcId).addVaccine(vaccine);
            Finder.loadDatabase().addVaccine(vaccine);
        }
    }

    /**
     * Set Recipient to available Vaccination Center
     * @param vcId          -   Vaccination Center ID
     * @param RecipientID   -   Recipient ID
     */
    public void distributeRecipient(int vcId, int RecipientID) throws Exception {
        Recipient checkRecipient = getRecipient(RecipientID);
        Vc checkVc = getVc(vcId);
        checkRecipient.getStatus().setVcId(vcId);
        checkVc.addRecipient(getRecipient(RecipientID));
    }

    @Override
    public String toString() {
        return  getId()  + ","
                + getName()     + ","
                + getPassword() + ","
                + getPhone();
    }
}
