/*
Pierre Drego 301301848 pdrego@sfu.ca
Gurkiran Kaur Brar 301274688 gkbrar@sfu.ca
Tyler Bailey 301346936 tjbailey@sfu.ca


References: [1], [2], [3], [4], [5], [6], [7], [8], [67], [69], [70], [71], [72], [73], [74], [75]
 */

package ca.sfu.cmpt275.calendarapp;

import ca.sfu.cmpt275.calendarapp.calendar.TaskManager;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class Main {
    public static void main(String[] args)
    {
        /*
        Pierre Drego 301301848 pdrego@sfu.ca
        Gurkiran Kaur Brar 301274688 gkbrar@sfu.ca
        Tyler Bailey 301346936 tjbailey@sfu.ca
         */

//<<<<<<< HEAD
/*
        //TaskManager thePotato = new TaskManager();
        CalendarGUI cal = new CalendarGUI();

        try {
            CalendarFile file = FileManager.load("", FileManager.FILE_NAME);
            FileManager.loadToGUI(cal, file);
            System.out.println("Loaded calendar from file!");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Unable to load from file! Continuing with empty calendar...");
        }
        cal.getThisWeek(); // Draw after loading
        */
 //       TaskManager thePotato = new TaskManager();
//=======

        // Launch TaskManager, which then launches CalendarGUI
        new TaskManager();

//>>>>>>> 29727e969d4a0f4b9f7e355b243b88a7b8825165
//        CalendarGUI cal = new CalendarGUI();
//        try {
//            CalendarFile file = FileManager.load("", FileManager.FILE_NAME);
//            FileManager.loadToGUI(cal, file);
//        } catch (Exception e) {
//            //e.printStackTrace();
//            System.out.println("Unable to load from file! Continuing with empty calendar...");
//        }
//        cal.getThisWeek(); // Draw after loading
        /*try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date start = format.parse("01-12-2020 00:00:00");
            Date end = format.parse("33-12-2020 23:59:59");
            System.out.println("START DATE: " + start);
            System.out.println("END DATE: " + end);
        } catch (ParseException e) {
            System.out.println("UH OH DATE ERROR CAUGHT");
        }*/
        //new Report(start, end);
    }
}