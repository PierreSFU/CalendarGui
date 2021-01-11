package ca.sfu.cmpt275.calendarapp;

import ca.sfu.cmpt275.calendarapp.calendar.Course;
import ca.sfu.cmpt275.calendarapp.calendar.ReportBlock;
import ca.sfu.cmpt275.calendarapp.calendar.TaskEnum;
import ca.sfu.cmpt275.calendarapp.calendar.TaskManager;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

public class TestTaskManager extends TaskManager {
    @Test
    // Resets member variables
    public void junitClean(){
        setOurCourses(new ArrayList<>());
        setOurReports(new ArrayList<>());
        setMaxTotalStudyTimePerDayMilliseconds(11);
    }

    @Test
    // Tests TaskManager Functions
    public void junitTestFunctions(){
//        System.out.println("Begin JUNIT Functions Test ----------");
        junitClean();

        long oneMinuteMilliseconds = 1000*60;
        long oneHourMilliseconds = oneMinuteMilliseconds*60;

        Calendar startingDateTest = Calendar.getInstance();
        startingDateTest.set(Calendar.MONTH, 11);
        startingDateTest.set(Calendar.DAY_OF_MONTH, 7);
        startingDateTest.set(Calendar.HOUR_OF_DAY, 9);
        startingDateTest.set(Calendar.MINUTE, 0);
        startingDateTest.set(Calendar.SECOND, 0);
        Calendar endingDateTest = (Calendar) startingDateTest.clone();
        Calendar endingDateTestDelay = (Calendar) endingDateTest.clone();
        endingDateTestDelay.setTimeInMillis(endingDateTest.getTimeInMillis() + oneMinuteMilliseconds*10);

        // Difference between dateToMinutes
        assert(differenceDateToMinutes(startingDateTest,endingDateTest) == 0);
        assert(differenceDateToMinutes(endingDateTest, endingDateTestDelay) == 10);
        assert(differenceDateToMinutes(endingDateTestDelay, endingDateTest) == -10);

        // Difference date to hours, rounds to 0
        assert(unroundedDifferenceDateToHours(startingDateTest, startingDateTest) == 0);
        assert(unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 0);
        assert(unroundedDifferenceDateToHours(endingDateTestDelay, startingDateTest) == 0);

        // Difference date to hours, 3 hour delay
        endingDateTestDelay.setTimeInMillis(endingDateTest.getTimeInMillis() + oneHourMilliseconds*3);
        assert(unroundedDifferenceDateToHours(endingDateTest, endingDateTestDelay) == 3);
        assert(unroundedDifferenceDateToHours(endingDateTestDelay, endingDateTest) == -3);
        assert(unroundedDifferenceDateToHours(endingDateTest, endingDateTest) == 0);

        // Test appropriate rounding function
        endingDateTestDelay = (Calendar) endingDateTest.clone();
        endingDateTestDelay.set(Calendar.HOUR_OF_DAY, 9);
        endingDateTestDelay.set(Calendar.MINUTE, 0);

        assert(TaskManager.unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 0);
        endingDateTestDelay.set(Calendar.HOUR_OF_DAY, 9);
        endingDateTestDelay.set(Calendar.MINUTE, 15);
        assert(TaskManager.unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 0);
        endingDateTestDelay.set(Calendar.HOUR_OF_DAY, 9);
        endingDateTestDelay.set(Calendar.MINUTE, 30);
        assert(TaskManager.unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 0);
        endingDateTestDelay.set(Calendar.HOUR_OF_DAY, 9);
        endingDateTestDelay.set(Calendar.MINUTE, 45);
        assert(TaskManager.unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 0);
        endingDateTestDelay.set(Calendar.HOUR_OF_DAY, 9);
        endingDateTestDelay.set(Calendar.MINUTE, 55);
        assert(TaskManager.unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 0);
        endingDateTestDelay.set(Calendar.HOUR_OF_DAY, 9);
        endingDateTestDelay.set(Calendar.MINUTE, 59);
        assert(TaskManager.unroundedDifferenceDateToHours(startingDateTest, endingDateTestDelay) == 1);

        // Converting hours to milliseconds
        assert(hoursToMilliseconds(0) == 0);
        assert(hoursToMilliseconds(-50) == oneHourMilliseconds*-50);
        assert(hoursToMilliseconds(9999) == oneHourMilliseconds*9999);

        // Addin hours
        int hoursToAdd = -10;
        endingDateTest = (Calendar) startingDateTest.clone();
        endingDateTest.setTimeInMillis(endingDateTest.getTimeInMillis() + oneHourMilliseconds*hoursToAdd);
        assert(addHoursToDate(startingDateTest, hoursToAdd).equals(endingDateTest));

        hoursToAdd = 1;
        endingDateTest = (Calendar) startingDateTest.clone();
        endingDateTest.setTimeInMillis(endingDateTest.getTimeInMillis() + oneHourMilliseconds*hoursToAdd);
        assert(addHoursToDate(startingDateTest, hoursToAdd).equals(endingDateTest));

        hoursToAdd = 50000;
        endingDateTest = (Calendar) startingDateTest.clone();
        endingDateTest.setTimeInMillis(endingDateTest.getTimeInMillis() + oneHourMilliseconds*hoursToAdd);
        assert(addHoursToDate(startingDateTest, hoursToAdd).equals(endingDateTest));

        junitClean();
//        System.out.println("-------- END JUNIT Functions Test");
    }

    @Test
    // Tests TaskManager Courses
    public void junitTestCourses(){
//        System.out.println("Begin JUNIT Course Test ----------");
        junitClean();

        long oneMinuteMilliseconds = 1000*60;

        Calendar startingDateTest = Calendar.getInstance();
        Calendar endingDateTest = (Calendar) startingDateTest.clone();
        endingDateTest.setTimeInMillis(startingDateTest.getTimeInMillis() + oneMinuteMilliseconds);

        Calendar startingDateTestDelay = (Calendar) startingDateTest.clone();
        startingDateTestDelay.setTimeInMillis(startingDateTest.getTimeInMillis() + oneMinuteMilliseconds*10);
        Calendar endingDateTestDelay = (Calendar) endingDateTest.clone();
        endingDateTestDelay.setTimeInMillis(endingDateTest.getTimeInMillis() + oneMinuteMilliseconds*10);

        String J1 = "Junit1";
        String J4 = "Junit4";
        String desc = "Description";

        // Course Test
        TaskManager.addCourse(J1, J1, 1, 1, Color.RED);
        TaskManager.addCourse("Junit2", "Junit2", 2, 2, Color.YELLOW);
        TaskManager.addCourse("Junit3", "Junit3", 3, 3, Color.GREEN);
        TaskManager.addCourse(J4, J4, 4, 4, Color.BLUE);

        assert (TaskManager.getOurCourses().get(0).getCourseName().equals(J1));
        assert (TaskManager.getOurCourses().get(1).getCourseName().equals("Junit2"));
        assert (TaskManager.getOurCourses().get(2).getCourseName().equals("Junit3"));
        assert (TaskManager.getOurCourses().get(3).getCourseName().equals(J4));
        assert(TaskManager.getOurCourses().size() == 4);
        TaskManager.deleteCourse("Junit3");

        assert (TaskManager.getOurCourses().get(0).getCourseName().equals(J1));
        assert (TaskManager.getOurCourses().get(1).getCourseName().equals("Junit2"));
        assert (TaskManager.getOurCourses().get(2).getCourseName().equals(J4));
        assert(TaskManager.getOurCourses().size() == 3);
        TaskManager.deleteCourse("Junit2");

        assert (TaskManager.getOurCourses().get(0).getCourseName().equals(J1));
        assert (TaskManager.getOurCourses().get(1).getCourseName().equals(J4));
        assert(TaskManager.getOurCourses().size() == 2);

        // Check if details match
        assert(TaskManager.getOurCourses().get(0).getColour().equals(Color.RED));
        assert(TaskManager.getOurCourses().get(0).getCredits() == 1);
        assert(TaskManager.getOurCourses().get(0).getCreditMultiplier() == 1);
        assert(TaskManager.getOurCourses().get(0).getDescription().equals(J1));

        assert(TaskManager.getOurCourses().get(1).getColour().equals(Color.BLUE));
        assert(TaskManager.getOurCourses().get(1).getCredits() == 4);
        assert(TaskManager.getOurCourses().get(1).getCreditMultiplier() == 4);
        assert(TaskManager.getOurCourses().get(1).getDescription().equals(J4));

        // Check if tasks are appropriately added
        String toObfuscate = " Obfuscated";
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.QUIZ.getLabel(), J1+toObfuscate,
                (Calendar) startingDateTest.clone(), (Calendar) endingDateTestDelay.clone(), desc,
                2, 0, 1, 2, 3, 4);

        // Check if everything was appropriately added
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getTaskName().equals(J1 + toObfuscate));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getStartTime().equals(startingDateTest));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getEndTime().equals(endingDateTestDelay));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getDescription().equals(desc));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getX() == 1);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getY() == 2);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getWidth() == 3);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getHeight() == 4);

        assert(TaskManager.getOurCourses().get(0).getMyHomework().size() == 1);
        assert(TaskManager.getOurCourses().get(1).getMyHomework().size() == 0);

        // Check if tasks are appropriately edited
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setTaskName(J1);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setStartTime(startingDateTestDelay);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setEndTime(endingDateTestDelay);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setDescription(desc + toObfuscate);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setX(4);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setY(3);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setWidth(2);
        TaskManager.getOurCourses().get(0).getMyHomework().get(0).setHeight(1);

        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getTaskName().equals(J1));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getStartTime().equals(startingDateTestDelay));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getEndTime().equals(endingDateTestDelay));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getDescription().equals(desc + toObfuscate));
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getX() == 4);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getY() == 3);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getWidth() == 2);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().get(0).getHeight() == 1);

        // Check if tasks are appropriately deleted
        TaskManager.getOurCourses().get(0).getMyHomework().remove(0);
        assert(TaskManager.getOurCourses().get(0).getMyHomework().size() == 0);

        // Check if tasks are appropriately found
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.QUIZ.getLabel(), "Q1",
                (Calendar) startingDateTest.clone(), (Calendar) endingDateTest.clone(), desc,
                2, 0, 1, 2, 3, 4);
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.ASSIGNMENT.getLabel(), "A1",
                (Calendar) startingDateTest.clone(), (Calendar) endingDateTest.clone(), desc,
                2, 0, 1, 2, 3, 4);
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.QUIZ.getLabel(), "Q2",
                (Calendar) startingDateTest.clone(), (Calendar) endingDateTest.clone(), desc,
                2, 0, 1, 2, 3, 4);

        assert(TaskManager.getOurCourses().get(0).getMyHomework().size() == 3);

        // Test file search
        // TaskName & type slightly off
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.ASSIGNMENT.getLabel(), "Q1",
                startingDateTest, endingDateTest) == -1);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.ASSIGNMENT.getLabel(), "Q2",
                startingDateTest, endingDateTest) == -1);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "Q12",
                startingDateTest, endingDateTest) == -1);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "A1",
                startingDateTest, endingDateTest) == -1);

        // Dates slightly off
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "Q1",
                startingDateTestDelay, endingDateTest) == -1);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "Q1",
                startingDateTest, endingDateTestDelay) == -1);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "Q1",
                startingDateTestDelay, endingDateTestDelay) == -1);

        // Proper index
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "Q1",
                startingDateTest, endingDateTest) == 0);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.ASSIGNMENT.getLabel(), "A1",
                startingDateTest, endingDateTest) == 1);
        assert(TaskManager.getOurCourses().get(0).findTaskIndex(TaskEnum.QUIZ.getLabel(), "Q2",
                startingDateTest, endingDateTest) == 2);

        junitClean();
//        System.out.println("-------- END JUNIT Course Test");
    }

    @Test
    // Tests TaskManager Report
    public void junitTestReport(){
//        System.out.println("Begin JUNIT Report Test ----------");
        junitClean();

        long oneMinuteMilliseconds = 1000*60;
        long oneHourMilliseconds = oneMinuteMilliseconds*60;
        long oneDayMilliseconds = oneHourMilliseconds*24;

        // Date Dec 7th, 9AM -> 8PM
        Calendar originalStartDate = Calendar.getInstance();
        originalStartDate.set(Calendar.MONTH, 11);
        originalStartDate.set(Calendar.DAY_OF_MONTH, 7);
        originalStartDate.set(Calendar.HOUR_OF_DAY, 9);
        originalStartDate.set(Calendar.MINUTE, 0);
        originalStartDate.set(Calendar.SECOND, 0);
        Calendar originalEndDate = (Calendar) originalStartDate.clone();
        originalEndDate.set(Calendar.HOUR_OF_DAY, 20);

        Calendar testStartDate = (Calendar) originalStartDate.clone();
        Calendar testEndDate = (Calendar) originalStartDate.clone();

        String name = "Junit";
        String desc = "Junit desc";

        TaskManager.addCourse(name, desc, 1, 1, Color.RED);

        // 1st Course Monday, 11->1PM, Priority 2
        testStartDate.set(Calendar.HOUR_OF_DAY, 11);
        testEndDate.set(Calendar.HOUR_OF_DAY, 13);
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.ASSIGNMENT.getLabel(), name,
                (Calendar) testStartDate.clone(), (Calendar) testEndDate.clone(), desc,
                2, 0, 1, 2, 3, 4);

        // 2nd Course Monday, 5PM->7PM, Priority 3
        testStartDate.set(Calendar.HOUR_OF_DAY, 17);
        testEndDate.set(Calendar.HOUR_OF_DAY, 19);
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.QUIZ.getLabel(), name,
                (Calendar) testStartDate.clone(), (Calendar) testEndDate.clone(), desc,
                3, 0, 1, 2, 3, 4);

        // 3rd Course Tuesday, 11->1PM, Priority 1
        testStartDate.set(Calendar.HOUR_OF_DAY, 11);
        testEndDate.set(Calendar.HOUR_OF_DAY, 12);
        testStartDate.set(Calendar.DAY_OF_MONTH, 8);
        testEndDate.set(Calendar.DAY_OF_MONTH, 8);
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.QUIZ.getLabel(), name,
                (Calendar) testStartDate.clone(), (Calendar) testEndDate.clone(), desc,
                1, 0, 1, 2, 3, 4);

        // 4th Course Thursday, 11->1PM, Priority 3
        testStartDate.set(Calendar.HOUR_OF_DAY, 11);
        testEndDate.set(Calendar.HOUR_OF_DAY, 15);
        testStartDate.set(Calendar.DAY_OF_MONTH, 10);
        testEndDate.set(Calendar.DAY_OF_MONTH, 10);
        TaskManager.getOurCourses().get(0).addTask(TaskEnum.ASSIGNMENT.getLabel(), name,
                (Calendar) testStartDate.clone(), (Calendar) testEndDate.clone(), desc,
                3, 0, 1, 2, 3, 4);

        testEndDate.set(Calendar.DAY_OF_MONTH, 25);
        dataForReport(originalStartDate, testEndDate, 14);
        ArrayList<ReportBlock> theReports = getOurReports();

        assert(theReports.size() == 3);
        assert(theReports.get(0).getTotalCourseMinutes() == 78840);
        assert(theReports.get(0).getAssignmentMinutes() == 26280);
        assert(theReports.get(0).getQuizMinutes() == 52560);
        assert(theReports.get(0).getOtherMinutes() == 0);
        assert(theReports.get(0).getLectureMinutes() == 0);

        junitClean();
//        System.out.println("-------- END JUNIT Report Test");
    }

    @Test
    // Tests TaskManagers Stability,
    // iterationSize = 1000, 1000 courses, 1000 Tasks per course, 1 000 000 total objects
    public void stressTest(){
        //        System.out.println("Begin JUNIT stressTest ----------");
        junitClean();

        int iterationSize = 1000;
        for(int i = 0; i < iterationSize; i++){
            getOurCourses().add(new Course("Personal" + i, "Hobbies, Work, etc", 1,
                    1, Color.GREEN));
        }

        // Date Dec 7th, 9AM -> 8PM
        Calendar originalStartDate = Calendar.getInstance();
        originalStartDate.set(Calendar.MONTH, 11);
        originalStartDate.set(Calendar.DAY_OF_MONTH, 7);
        originalStartDate.set(Calendar.HOUR_OF_DAY, 9);
        originalStartDate.set(Calendar.MINUTE, 0);
        originalStartDate.set(Calendar.SECOND, 0);
        Calendar originalEndDate = (Calendar) originalStartDate.clone();
        originalEndDate.set(Calendar.HOUR_OF_DAY, 20);

        Calendar testStartDate = (Calendar) originalStartDate.clone();
        Calendar testEndDate = (Calendar) originalStartDate.clone();

        // 2nd Course Monday, 5PM->7PM, Priority 3
        testStartDate.set(Calendar.HOUR_OF_DAY, 17);
        testEndDate.set(Calendar.HOUR_OF_DAY, 19);

        for(int i = 0; i < iterationSize; i++){
            getOurCourses().add(new Course("Personal" + i, "Hobbies, Work, etc", 1,
                    1, Color.GREEN));
        }

        String name = "Junit ";
        String desc = "Desc ";
        for(int j = 0; j < iterationSize; j++)
            for(int i = 0; i < iterationSize; i++)
                TaskManager.getOurCourses().get(j).addTask(TaskEnum.QUIZ.getLabel(), name + i,
                        (Calendar) testStartDate.clone(), (Calendar) testEndDate.clone(), desc,
                        3, 0, 1, 2, 3, 4);


        junitClean();
        //        System.out.println("-------- END JUNIT stressTest");
    }
}
