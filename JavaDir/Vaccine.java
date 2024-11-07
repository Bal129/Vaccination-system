package JavaDir;

public class Vaccine {
    private static int vaccineCount;
    private int batchNo;
    private int vcId;
    private int recipientId = -1;
    private int recipientDose = -1;

    Vaccine(int vcId) {
        setVcId(vcId);
        setBatchNo(vaccineCount);
        vaccineCount++;
    }

    Vaccine(int vcId, int recipientId, int recipientDose) {
        setVcId(vcId);
        setBatchNo(vaccineCount);
        setRecipientId(recipientId);
        setRecipientDose(recipientDose);
        vaccineCount++;
    }

    Vaccine(int vcId, int recipientId, int recipientDose, int batchNo) {
        setVcId(vcId);
        setRecipientId(recipientId);
        setRecipientDose(recipientDose);
        setBatchNo(batchNo);
        vaccineCount++;
    }

    // getter
    public int getBatchNo() {
        return batchNo;
    }
    public int getRecipientId() {
        return recipientId;
    }
    public int getVcId() {
        return vcId;
    }
    public int getRecipientDose() {
        return recipientDose;
    }

    // setter
    public void setBatchNo(int batchNo) {
        if (batchNo < 0)
            throw new IllegalArgumentException("Invalid setBatchNo(int batchNo)");
        this.batchNo = batchNo;
    }

    public void setRecipientId(int recipientId) {
        if (recipientId < -2)
            throw new IllegalArgumentException("Invalid setRecipientId(int recipientId)");
        this.recipientId = recipientId;
    }

    public void setVcId(int vcId) {
        if (vcId < 0)
           throw new IllegalArgumentException("Invalid setVcId(int vcId)");
        this.vcId = vcId;
    }

    public void setRecipientDose(int recipientDose) {
        if (recipientDose < -1)
            throw new IllegalArgumentException("Invalid setRecipientDose(int recipientDose)");
        this.recipientDose = recipientDose;
    }

    @Override
    public String toString() {
        return String.valueOf(getBatchNo());
    }

    public String toTxtString() {
        return String.valueOf(vcId)             + ","
               + String.valueOf(recipientId)    + ","
               + String.valueOf(recipientDose)  + ","
               + String.valueOf(getBatchNo());
    }
}
