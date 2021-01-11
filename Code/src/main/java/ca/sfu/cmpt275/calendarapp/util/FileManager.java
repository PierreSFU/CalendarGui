package ca.sfu.cmpt275.calendarapp.util;

import ca.sfu.cmpt275.calendarapp.calendar.CalendarGUI;
import ca.sfu.cmpt275.calendarapp.calendar.TaskManager;

import java.io.*;

// References: [59], [60], [61], [62], [63], [64], [65], [66], [68], [69], [70], [71], [72], [73], [74], [75]

public class FileManager {

    public static final String FILE_NAME = "cal_save";

    // Save an object to file (exceptions are to be handled externally)
    public static void save(String loc, String name) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(loc + name + ".cal");
        ObjectOutputStream output = new ObjectOutputStream(fileOut);
        output.writeObject(new CalendarFile());

        output.close();
        fileOut.close();
    }

    // Load an object from file if it exists (exceptions are to be handled externally)
    public static CalendarFile load(String loc, String name) throws IOException, ClassNotFoundException {
        CalendarFile obj; // Declare null object to be initialized via load
        FileInputStream fileIn = new FileInputStream(loc + name + ".cal");
        ObjectInputStream input = new ObjectInputStream(fileIn);

        obj = (CalendarFile) input.readObject(); // Attempt to load object from file

        input.close();
        fileIn.close();

        return obj;
    }

    // Updates display
    public static void loadToGUI(CalendarGUI cal, CalendarFile file) {
        TaskManager.setOurCourses(file.getCourses());
        TaskManager.setOurReports(file.getReports());
        cal.getThisWeek();
    }
}
