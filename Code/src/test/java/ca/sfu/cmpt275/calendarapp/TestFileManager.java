package ca.sfu.cmpt275.calendarapp;

import ca.sfu.cmpt275.calendarapp.calendar.Course;
import ca.sfu.cmpt275.calendarapp.calendar.TaskManager;
import ca.sfu.cmpt275.calendarapp.util.CalendarFile;
import ca.sfu.cmpt275.calendarapp.util.FileManager;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestFileManager {
    @Test
    public void testSaveAndLoad() {

        Course[] courses = {new Course("Test Course 1", "testing", 3, 1, Color.BLUE),
                new Course("Test Course 2", "testing", 3, 1, Color.ORANGE)};
        try {
            FileManager.save("", "cal_save_test");
            CalendarFile file = FileManager.load("", "cal_save_test");
            Assert.assertEquals(file.getCourses().toArray(), courses);
            Files.deleteIfExists(Paths.get("cal_save_test.cal"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
