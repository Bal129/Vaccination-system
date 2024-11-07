package JavaDir;

import java.io.IOException;
import java.util.ArrayList;

public class Finder {
    private static Database database;
    private static MOH moh = new MOH();
    private static ArrayList<Vc> vcs = new ArrayList<>();
    private static ArrayList<Recipient> recipients = new ArrayList<>();

    private Finder() throws IOException {
        database = new Database();
        moh = database.loadMOH();
        vcs = database.loadVCs();
        recipients = database.loadRecipients();
    }

    public static Database loadDatabase() throws IOException {
        if (database==null)
            new Finder();
        return database;
    }

    public static MOH getMOH() {
        return moh;
    }

    public static ArrayList<Vc> getVcs() {
        return vcs;
    }

    public static ArrayList<Recipient> getRecipients() {
        return recipients;
    }

    public static Vc getVc(int id) {
        for (Vc vc : vcs) {
            if (vc.getId() == id)
                return vc;
        }
        return null;
    }

    public static Recipient getRecipient(int id) {
        for (Recipient recipient : recipients) {
            if (recipient.getId() == id)
                return recipient;
        }
        return null;
    }
}
