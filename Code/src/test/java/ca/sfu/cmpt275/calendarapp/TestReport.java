package ca.sfu.cmpt275.calendarapp;

import ca.sfu.cmpt275.calendarapp.reports.Report;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

public class TestReport {

    @Test
    public void testSave() {
        try {
            Report testR = new Report(Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
            testR.save("", testR.getReportPanel());
            boolean exists = Files.deleteIfExists(Paths.get(Report.FILE_NAME + ".png"));
            Assert.assertTrue(exists);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
