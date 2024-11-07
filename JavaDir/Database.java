package JavaDir;

import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.nio.file.Files;
import java.util.List;

public class Database {

    private MOH moh;
    private int vcSize;
    private ArrayList<Vc> vcs                               = new ArrayList<>();
    private ArrayList<Vaccine> vaccines                     = new ArrayList<>();
    private ArrayList<Recipient> recipients                 = new ArrayList<>();
    private ArrayList<ArrayList<VcDaily>> dailyRecords      = new ArrayList<>();
    private ArrayList<ArrayList<Recipient>> recipients_vcs  = new ArrayList<>();

    ////////////// constructor //////////////

    Database() throws IOException {
        vcSize = Files.readAllLines(Keys.VCS_PATH).size();
        readDailyRecords();
        readRecipients();
        readVC();
        readMOH();
        readVaccine();
    }

    ////////////// getter //////////////

    public ArrayList<Recipient> loadRecipients() throws IOException {
        return recipients;
    }

    public ArrayList<Recipient> loadRecipients(int vcId) throws IOException {
        return recipients_vcs.get(vcId);
    }

    public Recipient loadRecipient(int recipientId) throws IOException {
        return recipients.get(recipientId);
    }

    public ArrayList<Vc> loadVCs() {
        return vcs;
    }

    public Vc loadVC(int vcId) {
        return vcs.get(vcId);
    }

    public MOH loadMOH() {
        return moh;
    }

    public ArrayList<Vaccine> getVaccines() {
        return vaccines;
    }

    private void readRecipients() throws IOException {
        List<String> lines = Files.readAllLines(Keys.RECIPIENTS_PATH);
        
        while (recipients_vcs.size() < vcSize)
            recipients_vcs.add(new ArrayList<Recipient>());

        for (String line : lines) {
            String[] row = line.split(",");   // row
            String  temp1 = row[Keys.RECIPIENT_STATUS_DOSE_1_APPOINMENT],
                    temp2 = row[Keys.RECIPIENT_STATUS_DOSE_2_APPOINMENT];

            Dose[] doses = {
                new Dose(
                    temp1.equals("null") ? null : LocalDate.parse(temp1, Keys.FORMATTER),
                    Boolean.parseBoolean(row[Keys.RECIPIENT_STATUS_DOSE_1_COMPLETE])
                ), 
                new Dose(
                    temp2.equals("null") ? null : LocalDate.parse(temp2, Keys.FORMATTER),
                    Boolean.parseBoolean(row[Keys.RECIPIENT_STATUS_DOSE_2_COMPLETE])
                )
            };
            int curVcId = Integer.parseInt(row[Keys.RECIPIENT_STATUS_VCID]);
            
            VcStatus status     = new VcStatus(curVcId, doses);
            Recipient recipient = new Recipient(
                                    Integer.parseInt(row[Keys.ID]), 
                                    row[Keys.NAME], 
                                    row[Keys.PASSWORD], 
                                    row[Keys.PHONE], 
                                    status, 
                                    Integer.parseInt(row[Keys.RECIPIENT_AGE])
                                );

            recipients.add(recipient);
            if (curVcId != -1)
                recipients_vcs.get(curVcId).add(recipient);
        }
    }

    private void readDailyRecords() throws IOException {
        List<String> lines = Files.readAllLines(Keys.DAILYRECORDS_PATH);

        while (dailyRecords.size() < vcSize)
            dailyRecords.add(new ArrayList<VcDaily>());

        for(String line : lines) {
            String[] row = line.split(",");
            int vcId = Integer.parseInt(row[Keys.ID]);
            VcDaily currentRecord = new VcDaily(
                                        vcId, 
                                        LocalDate.parse(
                                            row[Keys.VC_DAILY_DATE], 
                                            Keys.FORMATTER
                                        ),
                                        Integer.parseInt(row[Keys.VC_DAILY_TOTAL_RECIPIENT])
                                    ); 
            dailyRecords.get(vcId).add(currentRecord);
        }
    }

    private void readVC() throws IOException {
        List<String> lines = Files.readAllLines(Keys.VCS_PATH);

        for (String line : lines) {
            String[] row = line.split(",");
            int vcId = Integer.parseInt(row[Keys.ID]);

            Vc tempVc = new Vc(
                vcId,
                row[Keys.NAME],
                row[Keys.PASSWORD],
                row[Keys.PHONE],
                Integer.parseInt(row[Keys.VC_CAPACITY]),
                Integer.parseInt(row[Keys.VC_TOTAL_VAC_COUNT]),
                dailyRecords.get(vcId),
                recipients_vcs.get(vcId)
            );

            vcs.add(tempVc);
        }

    }

    private void readMOH() throws IOException {
        String[] row = Files.readAllLines(Keys.MOH_PATH).get(0).split(",");

        moh = new MOH(
            Integer.parseInt(row[Keys.ID]),
            row[Keys.NAME],
            row[Keys.PASSWORD],
            row[Keys.PHONE],
            recipients,
            vcs
        );
    }

    private void readVaccine() throws IOException {
        List<String> lines = Files.readAllLines(Keys.VACCINE_PATH);

        for (String line : lines) {
            String[] row = line.split(",");
            int vcId            = Integer.parseInt(row[Keys.VACCINE_VCID]);
            int recipientId     = Integer.parseInt(row[Keys.VACCINE_RECIPIENT_ID]);
            int recipientDose   = Integer.parseInt(row[Keys.VACCINE_RECIPIENT_DOSE]);
    
            Vaccine tempVac  = new Vaccine(
                vcId,
                recipientId,
                recipientDose,
                Integer.parseInt(row[Keys.VACCINE_BATCHNO])
            );

            if (recipientId != -1)
                loadRecipient(recipientId).getStatus().getDose(recipientDose).setVaccine(tempVac);
            else // <- toggle either vc should contains the used vaccines or not
                loadVC(vcId).addVaccine(tempVac);
            vaccines.add(tempVac);
        }
    }

    ////////////// setter //////////////

    public void addVaccine(Vaccine vaccine) {
        vaccines.add(vaccine);
    }

    public void saveRecipients() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Recipient recipient : recipients) 
            sb.append(recipient.toString()+"\n"); 
        
        Files.write(Keys.RECIPIENTS_PATH, sb.toString().getBytes());
    }

    public void saveVcs() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Vc vc : vcs) 
            sb.append(vc.toString()+"\n");   

        Files.write(Keys.VCS_PATH, sb.toString().getBytes());
        saveRecipients();
    }

    public void saveMOH() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(moh.toString()+"\n");
        Files.write(Keys.MOH_PATH, sb.toString().getBytes());
    }
    
    public void saveDailyRecords() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<VcDaily> curVC : dailyRecords)
            for (VcDaily curRecord : curVC)
                sb.append(curRecord.toTxtString()+ "\n");

        Files.write(Keys.DAILYRECORDS_PATH, sb.toString().getBytes());
    }

    public void saveVaccines() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Vaccine vaccine : vaccines)
            sb.append(vaccine.toTxtString()+"\n");

        Files.write(Keys.VACCINE_PATH, sb.toString().getBytes());    
    }

    public void refreshVaccine() throws IOException {
        vaccines.clear();
        readVaccine();
    }
}
