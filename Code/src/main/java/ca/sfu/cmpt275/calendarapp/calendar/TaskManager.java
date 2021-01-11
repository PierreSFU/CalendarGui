// References: [1], [2], [3], [4], [5], [6], [7], [8], [67], [69], [70], [71], [72], [73], [74], [75]

package ca.sfu.cmpt275.calendarapp.calendar;

import java.awt.*;
import java.util.*;
import ca.sfu.cmpt275.calendarapp.util.CalendarFile;
import ca.sfu.cmpt275.calendarapp.util.FileManager;

// TaskManager stores all of the user input into a list of Courses and stores everything the CalendarGUI is displaying
// in a list of TaskBlocks[] so that we know what Block a user is editing and how it relates to a given course
public class TaskManager
{
    private static ArrayList<Course> ourCourses = new ArrayList<>(); // Our courses
    private static ArrayList<ReportBlock> ourReports = new ArrayList<>();
    static private int maxTotalStudyTimePerDayMilliseconds; // End of day Time - Start of day time, in milliseconds

    public void initializeTaskManager(){
        // Load CalendarGUI
        CalendarGUI cal = new CalendarGUI();

        // Load FileManager
        try {
            CalendarFile file = FileManager.load("", FileManager.FILE_NAME);
            FileManager.loadToGUI(cal, file);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Unable to load from file! Continuing with empty calendar...");
        }

        // Initialize CourseList
        // If doesn't exist add this course
        if(findCourseIndex(TaskEnum.PERSONAL.getLabel()) == -1)
                ourCourses.add(new Course(TaskEnum.PERSONAL.getLabel(),
                        "Hobbies, Work, etc", 0, 1, Color.GREEN));
    }
    public TaskManager() {
        initializeTaskManager(); // Adds Calendar
    }

    // Getters
    static public int getMaxTotalStudyTimePerDayMilliseconds() {
        return maxTotalStudyTimePerDayMilliseconds;
    }
    public static ArrayList<Course> getOurCourses() {
        return ourCourses;
    }
    public static ArrayList<ReportBlock> getOurReports() {
        return ourReports;
    }

    // Setters
    static public void setMaxTotalStudyTimePerDayMilliseconds(int m) {
        maxTotalStudyTimePerDayMilliseconds = m;
    }
    public static void setOurCourses(ArrayList<Course> ourCourses) {
        TaskManager.ourCourses = ourCourses;
    }
    public static void setOurReports(ArrayList<ReportBlock> ourReports) {
        TaskManager.ourReports = ourReports;
    }

    // Methods to handle Course class
    public static int findCourseIndex(String inputtedName){
        for(int i = 0; i < ourCourses.size(); i++){
            if(ourCourses.get(i).getCourseName().equals(inputtedName)){
                return i;
            }
        }
        return -1;
    }

    // Adds a Course
    public static boolean addCourse(String courseName, String description, int credits, int creditMultiplier, Color colour) {
        // If -1 is returned, that means no course with that name exists
        if(findCourseIndex(courseName) == -1) {
            ourCourses.add(new Course(courseName, description, credits, creditMultiplier, colour));
            return true;
        }
        return false;
    }

    // Edits a Course
    public static boolean editCourse(String courseName, String newCourseName, String description, int credits, int creditMultiplier, Color colour) {
        int courseNumber = findCourseIndex(courseName);
        if(courseNumber != -1) {
            if(!newCourseName.equals(courseName)) { // Since they are not equal, we should update the courseName
                ourCourses.get(courseNumber).setCourseName(newCourseName);
                ourCourses.get(courseNumber).updateTaskCourseNames();
            }
            ourCourses.get(courseNumber).setDescription(description);
            ourCourses.get(courseNumber).setCredits(credits);
            ourCourses.get(courseNumber).setCreditMultiplier(creditMultiplier);
            ourCourses.get(courseNumber).setColour(colour);
            return true;
        }
        return false;
    }

    // Deletes a Course if given the courseName key
    public static boolean deleteCourse(String courseName) {
        int courseNumber = findCourseIndex(courseName);
        if(courseNumber != -1) {
            ourCourses.remove(courseNumber);
        }
        else{
            return false;
        }
        return true;
    }

    // This assumes there is already a course created
    public static boolean addCourseTask(String courseName, String taskType, String taskName, Calendar startTime, Calendar endTime,
                                        String description, int priority, int recurring,
                                        int x, int y, int width, int height) {
        int courseNumber = findCourseIndex(courseName); // Find course number
        // Add Task if course exists
        if(courseNumber != -1) {
            ourCourses.get(courseNumber).addTask(taskType, taskName, startTime, endTime, description, priority,
                    recurring, x, y, width, height);
            return true;
        }
        return false;
    }

    // Edits a given courseTask
    public static boolean editCourseTask(String courseName, String taskType, String taskName, Calendar startTime, Calendar endTime, String newTaskName,
                                         Calendar newStartTime, Calendar newEndTime, String description, int priority, int recurring,
                                         int x, int y, int width, int height){
        int courseNumber = findCourseIndex(courseName); // Find course number
        if(courseNumber != -1) {
            return ourCourses.get(courseNumber).editTask(taskType, taskName, startTime, endTime, newTaskName, newStartTime,
                    newEndTime, description, priority, recurring, x, y, width, height);
        }
        return false;
    }

    public static boolean deleteCourseTask(String courseName, String taskType, String taskName, Calendar startTime,
                                           Calendar endTime){
        int courseNumber = findCourseIndex(courseName); // Find course number
        if(courseNumber != -1) {
            return ourCourses.get(courseNumber).deleteTask(taskType, taskName, startTime, endTime);
        }
        return false;
    }

    // Comparators
// Methods to create schedule
    static public int sortStudyBlocksDate(Calendar obj1, Calendar obj2){
        if(obj2.before(obj1)){ // If obj2 is before Obj1 return true
            return 1;
        }
        else if(obj1.before(obj2)){
            return -1;
        }
        // if they're equal
        return 1;
    }
    // Returns 1 if obj1 (left) is >= obj2, else -1
    static public int sortStudyBlocksPriority(Homework obj1, Homework obj2){
        int obj1Priority = obj1.getPriority();
        int obj2Priority = obj2.getPriority();

        if(obj1Priority < obj2Priority){ // Give priority to obj1,
            return 1;
        }
        return -1;
    }

    // Returns 1 if obj1 (left) is >= obj2, else -1
    static public int secondSortPriorityAndDate(Homework obj1, Homework obj2){
        int obj1Priority = obj1.getPriority();
        int obj2Priority = obj2.getPriority();
        Calendar obj1StartTime = obj1.getStartTime();
        Calendar obj2StartTime = obj2.getStartTime();
        if(obj1Priority == obj2Priority){ // Give priority to obj1,
            if(obj1StartTime.before(obj2StartTime)){
                return -1;
            }
        }
        return 1;
    }

    // Returns 1 if theDate is between or equalTo the startDate and endDate, else -1
    public static boolean datesBetweenEqualTo(Calendar startDate, Calendar endDate, Calendar theDate){
        return ((theDate.after(startDate) && theDate.before(endDate)) ||
                theDate.equals(startDate) || theDate.equals(endDate));
    }

    // Functions
    // Gets a a list of TaskBlock within the given time range (), including recurring lectures
    static ArrayList<TaskBlock> getTaskBlocks(String courseName, Calendar startDate, Calendar endDate){
        int courseNumber = findCourseIndex(courseName);
        ArrayList <TaskBlock> listTaskBlock = new ArrayList<>();

        if(courseNumber != -1)
        {
            // 1. Get all blocks with startDate and endDate between given points
            Course currentCourse = ourCourses.get(courseNumber);
            ArrayList<Homework> homeworkList = currentCourse.getMyHomework();
            ArrayList<Other> otherList = currentCourse.getMyOther();
            ArrayList<Lecture> lectureList = currentCourse.getMyLecture();

            for(int i = 0; i < lectureList.size(); i++){
                Lecture currentTask = lectureList.get(i);
                // If currentTask is between and equal to startTime & endTime
                if(datesBetweenEqualTo(startDate, endDate, currentTask.getStartTime()))
                    listTaskBlock.add(currentTask.lectureToTaskBlock());
            }
            for(int i = 0; i < homeworkList.size(); i++){
                Homework currentTask = homeworkList.get(i);
                if(datesBetweenEqualTo(startDate, endDate, currentTask.getStartTime())){
                    listTaskBlock.add(currentTask.homeworkToTaskBlock());
                }
            }
            for(int i = 0; i < otherList.size(); i++){
                Other currentTask = otherList.get(i);
                if(datesBetweenEqualTo(startDate, endDate, currentTask.getStartTime())){
                    listTaskBlock.add(currentTask.otherToTaskBlock());
                }
            }
            return listTaskBlock;
        }
        return null;
    }

    // Return recurring items of ArrayList<Lecture>, does NOT return ORIGINAL lectures
    static ArrayList<Lecture> returnRecurringLectures(Calendar startingDate, Calendar endingDate){
        long oneDayMilli = hoursToMilliseconds(24);
        long differenceInHours = 0;
        int howManyDays; // How many days till the object recurs
        Calendar currentStartTime = null;
        Calendar currentEndTime = null;
        Lecture currentLectureObject = null;
        ArrayList<Lecture> lectureList = new ArrayList<>();

        for (int i = 0; i < ourCourses.size(); i++)
        {
            Course currentCourse = ourCourses.get(i);
            for (int x = 0; x < currentCourse.getMyLecture().size(); x++)
            {
                currentLectureObject = currentCourse.getMyLecture().get(x).cloneLectureObject();
                // How many days till it recurs again
                howManyDays = currentLectureObject.getRecurring();
                if(howManyDays <= 0) // Not recurring, so exit loop
                    break;

                currentStartTime = (Calendar) currentLectureObject.getStartTime().clone();
                currentEndTime = (Calendar) currentLectureObject.getEndTime().clone();
                differenceInHours = unroundedDifferenceDateToHours(currentStartTime,currentEndTime);

                //Iterate once so we don't return the original lecture object
                currentStartTime.setTimeInMillis(currentStartTime.getTimeInMillis() + oneDayMilli*howManyDays);

                // Iterate until we've reached the the startingDate with respect to the original starting date, inefficient
                while(currentStartTime.before(startingDate) && (!currentStartTime.equals(startingDate))) {
                    currentStartTime.setTimeInMillis(currentStartTime.getTimeInMillis() + oneDayMilli*howManyDays);
                }

                // If between starting & ending date OR equal to startingDate/ending date
                if (datesBetweenEqualTo(startingDate, endingDate, currentStartTime)) {
                    // While currentDate is before endingDate, NOT equal to. Add the recurring lecture as needed
                    while(currentStartTime.before(endingDate)){
                        // If currentStartTime is NOT equal to original startingTime
                        if(!currentStartTime.equals(currentLectureObject.getStartTime())) {
                            currentLectureObject = currentCourse.getMyLecture().get(x).cloneLectureObject();
                            currentLectureObject.setStartTime((Calendar) currentStartTime.clone());
                            currentEndTime.setTimeInMillis(currentStartTime.getTimeInMillis() + hoursToMilliseconds(differenceInHours));
                            currentLectureObject.setEndTime((Calendar) currentEndTime.clone());
                            lectureList.add(currentLectureObject); // Since it is between the given dates
                        }
                        currentStartTime.setTimeInMillis((currentStartTime.getTimeInMillis() + oneDayMilli*howManyDays));
                    }
                }
            }
        }

        return lectureList;
    }

    // Finds total minutes between two Date objects
    static public long differenceDateToMinutes(Calendar startingDateTime, Calendar endingDateTime) {
        double totalMilliseconds = (endingDateTime.getTimeInMillis() - startingDateTime.getTimeInMillis());
        totalMilliseconds = Math.round(((totalMilliseconds/1000)/60));
        return (long) totalMilliseconds;
    }

    // Finds total hours between two Date objects, ROUNDS DOWN!!
    // Milliseconds -> Hours, (x/1000) = seconds, x/60 = minutes, x/60 hours
    // 59 minutes will be rounded up
    static public long unroundedDifferenceDateToHours(Calendar startingDateTime, Calendar endingDateTime) {
        long minutes = differenceDateToMinutes(startingDateTime, endingDateTime);
        // Round up
        if(minutes%60 >= 59)
            minutes++;

        return (minutes/60);
    }

    // Hours*60 -> minutes*60 -> seconds*1000 -> milliseconds
    static public long hoursToMilliseconds(long hours){
        return (((hours*60)*60)*1000);
    }

    // Adds hours to a given starting time
    static public Calendar addHoursToDate(Calendar startingDateTime, long hoursToAdd){
        // Converts hours to milliseconds
        long totalMilliseconds = hoursToMilliseconds(hoursToAdd);

        // Add hours to startingDateTime in milliseconds
        startingDateTime.setTimeInMillis(startingDateTime.getTimeInMillis() + totalMilliseconds);
        return startingDateTime;
    }

    public static int findReportIndex(String theCourseName, ArrayList<ReportBlock> theReport){
        // Check if courseName already exists
        for(int j = 0; j < theReport.size(); j++)
        {
            // If it exists, add all the data to it
            if(theCourseName.equals(theReport.get(j).getCourseName())){
                return j;
            }
        }
        return -1;
    }

    // Get total hours studied for a given course and for each task within that course
    public static void dataForReport(Calendar startTime, Calendar endTime, int numDays) {
        Calendar originalStartTime = (Calendar) startTime.clone();
        Calendar originalEndTime = (Calendar) endTime.clone();

        ArrayList <TaskBlock> studyBlocks = schedulerBasic(startTime, numDays);

        // Make sure studyBlocks are between startTime and endTime
        // If false remove object since it's not between the startTime and endTime
        for(int i = 0; i < studyBlocks.size(); i++) {
            if(!datesBetweenEqualTo((Calendar) originalStartTime.clone(),
                    (Calendar) originalEndTime.clone(), studyBlocks.get(i).getStartTime()))
                studyBlocks.remove(i);
        }
        ArrayList<ReportBlock> theReport = new ArrayList<>();

        // Default Labels
        final String assignmentName = TaskEnum.ASSIGNMENT.getLabel();
        final String quizName = TaskEnum.QUIZ.getLabel();

        // Variables
        String courseName = "";
        String theTaskType = "";
        Color theColor = Color.BLACK;
        long numberMinutes = 0;
        int itemIndex = 0;

        for(int i = 0; i < studyBlocks.size(); i++)
        {
            courseName = studyBlocks.get(i).getCourseName();
            theTaskType = studyBlocks.get(i).getItemType();
            itemIndex = findReportIndex(courseName, theReport);
            theColor = studyBlocks.get(i).getColour();

            // Number hours for given studyBlock
            numberMinutes = differenceDateToMinutes((Calendar) studyBlocks.get(i).getStartTime().clone(),
                    (Calendar) studyBlocks.get(i).getEndTime().clone());

            // If it is equal to -1 we don't have a course ReportObject made yet
            if(itemIndex == -1) {
                ReportBlock newReportBlock = new ReportBlock(courseName);
                if(theTaskType.equals(assignmentName)){
                    newReportBlock.setAssignmentMinutes(numberMinutes);
                }
                else if(theTaskType.equals(quizName)){
                    newReportBlock.setQuizMinutes(numberMinutes);
                }
                else{
                    newReportBlock.setOtherMinutes(numberMinutes);
                }
                newReportBlock.setTotalCourseMinutes(numberMinutes);
                newReportBlock.setTheColor(theColor);
                theReport.add(newReportBlock);
            }
            // It exists, just add the time as needed
            else {
                ReportBlock oldReportBlock = theReport.get(itemIndex);
                if(theTaskType.equals(assignmentName)){
                    oldReportBlock.setAssignmentMinutes(numberMinutes + oldReportBlock.getAssignmentMinutes());
                }
                else if(theTaskType.equals(quizName)){
                    oldReportBlock.setQuizMinutes(numberMinutes + oldReportBlock.getQuizMinutes());
                }
                else{
                    oldReportBlock.setOtherMinutes(numberMinutes + oldReportBlock.getOtherMinutes());
                }
                oldReportBlock.setTotalCourseMinutes(numberMinutes + oldReportBlock.getTotalCourseMinutes());
                oldReportBlock.setTheColor(theColor);
                theReport.add(oldReportBlock);
            }
        }
        setOurReports(theReport);
    }

    // Gets all scheduler items and recurring lectures
    static public ArrayList<TaskBlock> getTaskBlocksAndRecurring(String courseName, Calendar startDate, Calendar endDate){
        ArrayList <TaskBlock> studyBlocks = getTaskBlocks(courseName, startDate, endDate);
        ArrayList <Lecture> convertToTaskBlock = returnRecurringLectures(startDate, endDate);

        for(int i = 0; i < convertToTaskBlock.size(); i++){
            studyBlocks.add(convertToTaskBlock.get(i).cloneLectureObject().lectureToTaskBlock());
        }
        return studyBlocks;
    }

    // Same as schedulerBasic except returns blocks as studyBlocks of type STUDYBLOCK
    static public ArrayList<TaskBlock> scheduler(Calendar startDate, int numDays){
        ArrayList<TaskBlock> newTaskBlocks = schedulerBasic(startDate, numDays);
        for(int i = 0; i < newTaskBlocks.size(); i++){
            newTaskBlocks.get(i).setItemType(TaskEnum.STUDYBLOCK.getLabel());
        }
        return newTaskBlocks;
    }

    //  1. Get all objects so we know what times are taken *2 Weeks ahead (numDays)*, (***Start of week Monday)
    //          to determine spots available for study block
    //       2. Determine all the free blocks available for a potential study block to occupy (Store in list maybe?)
    //
    //  3. Grab all the objects that need studying (Probably just Homework objects)
    //       4. Order all the objects by priority
    //
    //  5. For a given day assign the highest priority items first, FOR LOOP for 2 weeks for each day
    //         5a. If items are of matching priority split the hours evenly, MINIMUM: 20 minutes
    //         5b. ? missing something I think. Possible issues, not decrementing hours already studied
    //       6. Convert whatever object you're sorting them in into a taskBlock[]
    //       7. return taskBlock[]

    // Input: startDate must have hours set to appropriate starting hour & minute, ex: Dec 10,2020 7:15AM
    //        numDays is the number of days the scheduler will look ahead to accommodate study blocks for, 14 = 2 weeks
    // Output: Returns an Arraylist of <TaskBlock> sorted by startTime for numDays ahead
    // Description: Returns hours to study for a given Course, appropriately scheduled in regards to every other already
    //              scheduled object
    static public ArrayList<TaskBlock> schedulerBasic(Calendar startDate, int numDays)
    {
        // 1. Get all objects so we know what times are taken *2 Weeks ahead (numDays)*, (***Start of week Monday)
        //          to determine spots available for study block. Sort them by startTime
        //  OUTPUT: Single list of Objects sorted by StartTime

        // Number of days calendar looks ahead
        // Initialize time in milliseconds
        final long minimumMinutes = 20;  // Minimum studyBlock time
        final int oneMinuteMilliseconds = 60000;
        final int oneDayMilliseconds = 24*60*oneMinuteMilliseconds; // 60 minutes in 1 hour, 24 hours in one day

        Calendar startingDate = (Calendar) startDate.clone();
        Calendar endingDate = Calendar.getInstance();
        endingDate.setTimeInMillis(startDate.getTimeInMillis() + oneDayMilliseconds*numDays + oneMinuteMilliseconds*60*11);

        // Initialize Lists
        ArrayList<Homework> allHomework = new ArrayList<>();
        ArrayList<Other> allOther = new ArrayList<>();

        // First generate all recurring objects appropriately
        ArrayList<Lecture> allLecture = new ArrayList<>(returnRecurringLectures(startingDate, endingDate));

        // Sets  amount of hours to study for each Homework object, must be initialized at start
        for(int i = 0; i < ourCourses.size(); i++) {
            ourCourses.get(i).determineHoursToStudy();
        }

        // Get all list items that are due (startTime) before numDays and after startingDate
        //  ex: numDays = 14, Get all items that have
        // a start time of 14 days from the current date
        Calendar dateToCheck;

        if(ourCourses != null) // Checks that an object is valid, since this previously caused an error
        {
            for (int i = 0; i < ourCourses.size(); i++) {
                Course currentCourse = ourCourses.get(i);
                for (int x = 0; x < currentCourse.getMyHomework().size(); x++) {
                    // If date after startingDate & before endingDate or equalTo
                    dateToCheck = currentCourse.getMyHomework().get(x).getStartTime();
                    if (datesBetweenEqualTo(startingDate, endingDate, dateToCheck))
                        allHomework.add(currentCourse.getMyHomework().get(x).cloneHomeworkObject());
                }
                for (int x = 0; x < currentCourse.getMyOther().size(); x++) {
                    dateToCheck = currentCourse.getMyOther().get(x).getStartTime();
                    if (datesBetweenEqualTo(startingDate, endingDate, dateToCheck))
                        allOther.add(currentCourse.getMyOther().get(x).cloneOtherObject());
                }
                for (int x = 0; x < currentCourse.getMyLecture().size(); x++) {
                    dateToCheck = currentCourse.getMyLecture().get(x).getStartTime();
                    if (datesBetweenEqualTo(startingDate, endingDate, dateToCheck))
                        allLecture.add(currentCourse.getMyLecture().get(x).cloneLectureObject());
                }
            }
        }

        // Collate all objects into a single one
        ArrayList<TimeBlock> allObjects = new ArrayList<>();
        for(int i = 0; i < allHomework.size(); i++){
            allObjects.add(new TimeBlock((Calendar) allHomework.get(i).getStartTime().clone(),
                    (Calendar) allHomework.get(i).getEndTime().clone()));
        }
        for(int i = 0; i < allOther.size(); i++){
            allObjects.add(new TimeBlock((Calendar) allOther.get(i).getStartTime().clone(),
                    (Calendar) allOther.get(i).getEndTime().clone()));
        }
        for(int i = 0; i < allLecture.size(); i++){
            allObjects.add(new TimeBlock((Calendar) allLecture.get(i).getStartTime().clone(),
                    (Calendar) allLecture.get(i).getEndTime().clone()));
        }

        // Sort allObjects by startTime
        Collections.sort(allObjects, (o1, o2)->sortStudyBlocksDate(o1.getStartingTime(),o2.getStartingTime()));

        /*
            Ex:     a) Monday-> Beginning time = 8AM, Ending Time = 9PM
                    ex1:) 8am -> allObjects1(3->5PM) -> allObjects2(5:10->7PM) -> allObjects3(7:20PM->8PM) Easy
                        output: 8am-3PM, 7-7:20PM, 8-9PM
                    ex2:) 8am -> allObjects1(3:01-3:10PM) -> allObjects2(3:02-9:00)
                    ex2: Problem
         */

        // First combine all objects that have endTimes AFTER the next objects startTime
        // Ex: 8am -> allObjects1(3:01-3:10PM) -> allObjects2(3:02-9:00), Check if 3:10PM is after 3:02PM
        // If 3:10PM is after 3:02 PM, Then check whose endTime is later and set that as a big block that is taken
        // Output: A list of allObjects, with NO intersecting times
        Calendar currentObjectEnd = Calendar.getInstance();
        Calendar nextObjectStart = Calendar.getInstance();
        Calendar nextObjectEnd = Calendar.getInstance();
        for(int i = 0; i < allObjects.size()-1; i++){
            currentObjectEnd.setTime(allObjects.get(i).getEndingTime().getTime());
            nextObjectStart.setTime(allObjects.get(i+1).getStartingTime().getTime());
            nextObjectEnd.setTime(allObjects.get(i+1).getEndingTime().getTime());

            // Current objects endTime is after next objects startTime, we know that they intersect
            if(currentObjectEnd.after(nextObjectStart))
            {
                // If currentObjects endTime is before/equals nextObjects endTime, set currentObjects endTime to next objectsEndTime
                // Or in other words, since our object is from 7->8pm and theirs is from 7:30->9pm since they intersect,
                // we will set our endTime to their endTime since we are combining the blocks
                if (currentObjectEnd.before(nextObjectEnd) || currentObjectEnd.equals(nextObjectEnd)) {
                    allObjects.get(i).setEndingTime(nextObjectEnd);
                }
                //ELSE: Since our endTime is greater than their endTime, we don't need to do anything

                // Remove next object since we've combined them with the current one
                allObjects.remove((i + 1));
            }
        }

        ArrayList<TimeBlock> allFreeBlocks = new ArrayList<>();
        // NOTE: allObjects is already sorted by startDate and are all in blocks not intersecting
        // GOAL: Get all available studyBlocks within numDays, sorted by Date

        Calendar endOfDay = (Calendar) startDate.clone();
        Calendar currentStartTime = (Calendar) startDate.clone();
        Calendar currentEndTime = Calendar.getInstance(); // currentStartTime + minimum minutes
        Calendar objectsStartTime = Calendar.getInstance(); // comparison objects startTime
        Calendar objectsEndTime = Calendar.getInstance(); // comparison objects startTime
        Calendar nextObjectsStartTime = Calendar.getInstance(); // comparison objects startTime
        Calendar nextObjectsEndTime = Calendar.getInstance(); // comparison objects startTime


        int allObjectsIterator = 0;
        int currentDay = 1;
        while(currentDay < numDays)
        {
            currentEndTime.setTimeInMillis(currentStartTime.getTimeInMillis() + oneMinuteMilliseconds * minimumMinutes);
            endOfDay.setTimeInMillis((startDate.getTimeInMillis() + oneDayMilliseconds * (currentDay-1)) + getMaxTotalStudyTimePerDayMilliseconds());

            // if object exists
            if(allObjectsIterator < allObjects.size()) {
                objectsStartTime = (Calendar) allObjects.get(allObjectsIterator).getStartingTime().clone();
                objectsEndTime = (Calendar) allObjects.get(allObjectsIterator).getEndingTime().clone();

                if(allObjectsIterator+1 < allObjects.size()) {
                    nextObjectsStartTime = (Calendar) allObjects.get(allObjectsIterator+1).getStartingTime().clone();
                    nextObjectsEndTime = (Calendar) allObjects.get(allObjectsIterator+1).getEndingTime().clone();
                }
                // Don't let the program think there's a next object in the way
                else {
                    nextObjectsStartTime.setTimeInMillis(objectsStartTime.getTimeInMillis() + oneDayMilliseconds);
                    nextObjectsEndTime.setTimeInMillis(objectsEndTime.getTimeInMillis() + oneDayMilliseconds);
                }
            }
            // Make the program think there are no objects in the way, so set the objects startTimes and endTimes to
            // somewhere distant
            else{
                objectsStartTime.setTimeInMillis(endOfDay.getTimeInMillis() + oneDayMilliseconds);
                objectsEndTime.setTimeInMillis(endOfDay.getTimeInMillis() + oneDayMilliseconds*2);
            }

            // 1. If the object is at the next day, we will switch days, but first check if there are available hours
            // between now and the end of the day, ex: next object is 10AM tmrw, currentTime = 6PM, endOfDay = 9PM
            // add allFreeBlocks = 6PM-9PM. No blocks inbetween since it's sorted by date
            if (objectsStartTime.after(endOfDay)) {
                if (currentEndTime.before(endOfDay)) {
                    allFreeBlocks.add(new TimeBlock((Calendar) currentStartTime.clone(), (Calendar) endOfDay.clone()));
                }
                // Initialize next day
                currentStartTime.setTimeInMillis(startDate.getTimeInMillis() + currentDay * oneDayMilliseconds); // start of next day
                currentDay++;
            }
            // 2. Check if the current objects endTime is before/equal to the next objects startingTime
            // If true then we can just set our studyBlocks endTime to their objects startTime
            else if (currentEndTime.before(nextObjectsStartTime) || currentEndTime.equals(nextObjectsStartTime)) {
                allFreeBlocks.add(new TimeBlock((Calendar) currentStartTime.clone(), (Calendar) nextObjectsStartTime.clone()));
                currentStartTime.setTime(nextObjectsEndTime.getTime()); // our new startTime is the NEXT objects endTime
                allObjectsIterator += 2; // Since we've gone through the current object AND next object
            }
            // 3. If that objects startTime is before our currentTimeEnd ex: currentTimeEnd = 8AM->8:20AM,
            // objectsStartTime = 7AM->9AM We don't have enough space, so let's iterate to the next object.
            // currentTime = 9AM
            else if (currentEndTime.after(objectsStartTime)) {
                currentStartTime.setTime(objectsEndTime.getTime());
                allObjectsIterator++;
            }
            // No objects left! just add up all times as needed
        }

        // Sort by starting time
        Collections.sort(allObjects, (o1, o2)->sortStudyBlocksDate(o1.getStartingTime(),o2.getStartingTime()));

        // 3. Grab all the objects that need studying (Probably just Homework objects); Already done (allHomework)
        //       4. Order all the objects by priority

        // Sort by priority, highest priority first! Then sort by closest startingDate of equal priority
        Collections.sort(allHomework, (o1, o2)->sortStudyBlocksPriority(o1,o2));
        Collections.sort(allHomework, (o1, o2)->secondSortPriorityAndDate(o1,o2));

        // 5. For a given day assign the highest priority items first, FOR LOOP for 2 weeks for each day
        //         5a. If items are of matching priority split the hours evenly, MINIMUM: 20 minutes
        //         5b. ? missing something I think. Possible issues, not decrementing hours already studied
        //       6. Convert whatever object you're sorting them in into a taskBlock[]

        ArrayList <TaskBlock> studyBlocks = new ArrayList<>();
        Calendar startingTime = Calendar.getInstance();
        Calendar endingTime = Calendar.getInstance();

        long hoursAvailable = 0;
        long hoursFilled = 0;
        long hoursToBeFilled = 0;
        for(int i = 0; i < allHomework.size(); i++)
        {
            hoursFilled = 0;
            hoursToBeFilled = allHomework.get(i).getNumHoursToSpendStudying();

            String courseName = allHomework.get(i).getCourseName();
            String taskType = allHomework.get(i).getHomeworkType();
            String taskName = allHomework.get(i).getTaskName();
            int taskPriority = allHomework.get(i).getPriority();

            Color theColor = Color.BLACK;
            int theIndex = findCourseIndex(courseName);
            if(theIndex >= 0)
                theColor = ourCourses.get(theIndex).getColour();

            while(hoursFilled < hoursToBeFilled)
            {
                // If we have no freeStudyBlocks left, exit the loop
                if(allFreeBlocks.size() == 0) {
                    i = allHomework.size() + 1;
                    break;
                }
                // If the homeworks startTime is before the Objects startTime
                else if(allHomework.get(i).getStartTime().before(allFreeBlocks.get(0).getStartingTime())){
                    hoursToBeFilled = hoursFilled-1; // exit loop
                }
                else {
                    hoursAvailable = unroundedDifferenceDateToHours(allFreeBlocks.get(0).getStartingTime(), allFreeBlocks.get(0).getEndingTime());

                    // If a given freeBlock has enough hours to fill a studyBlock
                    if ((hoursAvailable + hoursFilled) >= hoursToBeFilled) {
                        // Shift starting time for the free block over
                        startingTime = allFreeBlocks.get(0).getStartingTime();
                        endingTime = addHoursToDate((Calendar) startingTime.clone(), (hoursToBeFilled - hoursFilled));

                        // Reduce original blocks size
                        allFreeBlocks.get(0).setStartingTime(endingTime);

                        // Fill hoursFilled with the amount of hours needed to fill it
                        hoursFilled += unroundedDifferenceDateToHours(startingTime, endingTime);
                    }
                    // If we don't have enough hours in a given free block to fill a studyBlock
                    else if ((hoursAvailable + hoursFilled) < hoursToBeFilled) {
                        // Set startingTime and endingTime for studyBlock
                        startingTime = (Calendar) allFreeBlocks.get(0).getStartingTime().clone();
                        endingTime = addHoursToDate((Calendar) startingTime.clone(), hoursAvailable);

                        // Reduce original blocks size
                        allFreeBlocks.get(0).setStartingTime((Calendar) endingTime.clone());

                        // Fill hoursFilled with the amount of hours needed to fill it
                        hoursFilled += unroundedDifferenceDateToHours((Calendar) startingTime.clone(), (Calendar) endingTime.clone());
                    }

                    // If a free block doesn't have minimum minutes remaining don't assign it
                    if (((differenceDateToMinutes(allFreeBlocks.get(0).getStartingTime(),
                            allFreeBlocks.get(0).getEndingTime())) < minimumMinutes)) {
                        allFreeBlocks.remove(0);
                    }

                    studyBlocks.add(new TaskBlock((Calendar) startingTime.clone(), (Calendar) endingTime.clone(),
                            courseName, taskName + '{' + taskType + '}', taskType, taskPriority, theColor));
                }
            }
        }
        // 7. return taskBlock[]
        return studyBlocks;
    }

}
