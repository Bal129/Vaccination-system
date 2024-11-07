package JavaDir;

import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;
import java.nio.file.Path;

public interface Keys {
    public static final String PATH                 = System.getProperty("user.dir");
    public static final String MOH_FILE             = "\\TextDir\\Moh.txt";
    public static final String VCS_FILE             = "\\TextDir\\Vcs.txt";
    public static final String RECIPIENTS_FILE      = "\\TextDir\\Recipients.txt";
    public static final String DAILYRECORDS_FILE    = "\\TextDir\\DailyRecords.txt";
    public static final String VACCINE_FILE         = "\\TextDir\\Vaccine.txt";
    public static final String STYLING_FILE         = "Styling.css";
    public static final String ICON                 = "icon.jpg";

    public static final Path MOH_PATH               = Paths.get(PATH+MOH_FILE);
    public static final Path VCS_PATH               = Paths.get(PATH+VCS_FILE);
    public static final Path RECIPIENTS_PATH        = Paths.get(PATH+RECIPIENTS_FILE);
    public static final Path DAILYRECORDS_PATH      = Paths.get(PATH+DAILYRECORDS_FILE);
    public static final Path VACCINE_PATH           = Paths.get(PATH+VACCINE_FILE);

    public static final int ID       = 0;
    public static final int NAME     = 1;
    public static final int PASSWORD = 2;
    public static final int PHONE    = 3;

    public static final int RECIPIENT_STATUS_VCID                   = 4;
    public static final int RECIPIENT_STATUS_PENDING                = 5;
    public static final int RECIPIENT_STATUS_DOSE_1_APPOINMENT      = 6;
    public static final int RECIPIENT_STATUS_DOSE_1_COMPLETE        = 7;
    public static final int RECIPIENT_STATUS_DOSE_2_APPOINMENT      = 8;
    public static final int RECIPIENT_STATUS_DOSE_2_COMPLETE        = 9;
    public static final int RECIPIENT_AGE                           = 10;

    public static final int VC_CAPACITY             = 4;
    public static final int VC_TOTAL_VAC_COUNT      = 5;
    public static final int VC_TODAY_DATE           = 6;

    public static final int VC_DAILY_TOTAL_RECIPIENT      = 1;
    public static final int VC_DAILY_DATE                 = 2;

    public static final int VACCINE_VCID                  = 0;
    public static final int VACCINE_RECIPIENT_ID          = 1;
    public static final int VACCINE_RECIPIENT_DOSE        = 2;
    public static final int VACCINE_BATCHNO               = 3;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final String CANNOTSAVE = "Cannot Update Database";
    public static final String NOTFOUND = "Account Not Found";
}
