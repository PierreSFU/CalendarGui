// References: [1], [2], [3], [4], [5], [6], [7], [8], [67], [69], [70], [71], [72], [73], [74], [75]

package ca.sfu.cmpt275.calendarapp.calendar;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Course implements Serializable {
    private static final long serialVersionUID = -1567793641153480705L;
    private String description;
    private String courseName;

    private int credits;
    private int creditMultiplier;
    private int maxHoursPerWeek;
    private Color colour;

    private ArrayList<Homework> myHomework = new ArrayList<>();
    private ArrayList<Other> myOther = new ArrayList<>();
    private ArrayList<Lecture> myLecture = new ArrayList<>();

    public Course(String ourCourseName, String ourDescription, int ourCredits, int ourCreditMultiplier, Color ourColour) {
        setCourseName(ourCourseName);
        setDescription(ourDescription);
        setCredits(ourCredits);
        setCreditMultiplier(ourCreditMultiplier);
        setMaxHoursPerWeek(getCredits()*getCreditMultiplier());
        setColour(ourColour);
    }

// ENUM
    final String quizName = TaskEnum.QUIZ.getLabel();
    final String assignmentName = TaskEnum.ASSIGNMENT.getLabel();
    final String otherName = TaskEnum.OTHER.getLabel();
    final String lectureName = TaskEnum.LECTURE.getLabel();

// Functions
    // Sets all objects a certain number of hours to be studying for the given item
    public void determineHoursToStudy(){
        // Set each objects numHoursToSpend
        for(int i = 0; i < myHomework.size(); i++){
            myHomework.get(i).setNumHoursToSpendStudying(getHoursToSpendStudying());
        }
    }

    // Formula = (ItemPriority*itemMaxHoursPerWeek)/(allItemPriorities)
    public long getHoursToSpendStudying(){
        return creditMultiplier*credits;
    }

    // Iterates through stored objects, updating their old coursename to the new one
    public void updateTaskCourseNames(){
        String newCourseName = this.getCourseName();

        for(int i = 0; i < myHomework.size(); i++)
            myHomework.get(i).setCourseName(newCourseName);
        for(int i = 0; i < myLecture.size(); i++)
            myLecture.get(i).setCourseName(newCourseName);
        for(int i = 0; i < myOther.size(); i++)
            myOther.get(i).setCourseName(newCourseName);
    }

    // Finds index from key values
    public int findTaskIndex(String taskType, String taskName, Calendar startTime, Calendar endTime) {
        if (taskType.equals(assignmentName) || taskType.equals(quizName) ) {
            for(int i = 0; i < myHomework.size(); i++)
            if(myHomework.get(i).getHomeworkType().equals(taskType) && myHomework.get(i).getTaskName().equals(taskName) && myHomework.get(i).getStartTime().equals(startTime)
                    && myHomework.get(i).getEndTime().equals(endTime)){
                return i;
            }
        } else if (taskType.equals(otherName)) {
            for(int i = 0; i < myOther.size(); i++)
            if(myOther.get(i).getTaskName().equals(taskName) && myOther.get(i).getStartTime().equals(startTime)
                    && myOther.get(i).getEndTime().equals(endTime)){
                return i;
            }
        }
        else if(taskType.equals(lectureName)){
            for(int i = 0; i < myLecture.size(); i++)
                if(myLecture.get(i).getTaskName().equals(taskName) && myLecture.get(i).getStartTime().equals(startTime)
                        && myLecture.get(i).getEndTime().equals(endTime)){
                    return i;
                }
        }
        return -1;
    }

    // Adds task to list
    public boolean addTask(String taskType, String taskName, Calendar startTime, Calendar endTime, String description,
                        int priority,  int recurring, int x, int y, int width, int height)  {

        if(runTests(taskType,startTime,endTime, priority, x, y, width, height) == true) {
            if (taskType.equals(assignmentName) || taskType.equals(quizName)) {
                this.myHomework.add(new Homework(getCourseName(), taskName, startTime, endTime, description, taskType,
                        priority, x, y, width, height));
            } else if (taskType.equals(otherName)) {
                this.myOther.add(new Other(getCourseName(), taskName, startTime, endTime, description, priority,
                        x, y, width, height));
            } else if (taskType.equals(lectureName)) {
                this.myLecture.add(new Lecture(getCourseName(), taskName, startTime, endTime, description,
                        recurring, x, y, width, height));
            } else
                return false;
        } else {
            System.err.println("Incorrect input in addTask");
            return false;
        }
        return true;
    }

    // Edits task from list
    //TODO: Possible let them edit their taskType, taskName
    public boolean editTask(String taskType, String taskName, Calendar startTime, Calendar endTime, String newTaskName,
                            Calendar newStartTime, Calendar newEndTime, String description, int priority,
                            int recurring, int x, int y, int width, int height) {
        int taskIndex = findTaskIndex(taskType, taskName, startTime, endTime);
        if(taskIndex == -1){
            return false;
        }
        if(runTests(taskType,startTime,endTime, priority, x, y, width, height) == true) {
            if (taskType.equals(quizName) || taskType.equals(assignmentName)) {
                myHomework.get(taskIndex).setTaskName(newTaskName);
                myHomework.get(taskIndex).setStartTime(newStartTime);
                myHomework.get(taskIndex).setEndTime(newEndTime);
                myHomework.get(taskIndex).setDescription(description);
                myHomework.get(taskIndex).setPriority(priority);
                myHomework.get(taskIndex).setX(x);
                myHomework.get(taskIndex).setY(y);
                myHomework.get(taskIndex).setWidth(width);
                myHomework.get(taskIndex).setHeight(height);
            } else if (taskType.equals(otherName)) {
                myOther.get(taskIndex).setTaskName(newTaskName);
                myOther.get(taskIndex).setStartTime(newStartTime);
                myOther.get(taskIndex).setEndTime(newEndTime);
                myOther.get(taskIndex).setDescription(description);
                myOther.get(taskIndex).setPriority(priority);
                myOther.get(taskIndex).setX(x);
                myOther.get(taskIndex).setY(y);
                myOther.get(taskIndex).setWidth(width);
                myOther.get(taskIndex).setHeight(height);
            } else if (taskType.equals(lectureName)) {
                myLecture.get(taskIndex).setTaskName(newTaskName);
                myLecture.get(taskIndex).setStartTime(newStartTime);
                myLecture.get(taskIndex).setEndTime(newEndTime);
                myLecture.get(taskIndex).setDescription(description);
                myLecture.get(taskIndex).setRecurring(recurring);
                myLecture.get(taskIndex).setX(x);
                myLecture.get(taskIndex).setY(y);
                myLecture.get(taskIndex).setWidth(width);
                myLecture.get(taskIndex).setHeight(height);
            } else
                return false;
        } else {
            System.err.println("Incorrect input in editTask");
            return false;
        }
        return true;
    }

    // Deletes task from list
    public boolean deleteTask(String taskType, String taskName, Calendar startTime, Calendar endTime) {
        int taskIndex = findTaskIndex(taskType, taskName, startTime, endTime);
        if(taskIndex == -1){
            return false;
        }

        if(taskType.equals(quizName) || taskType.equals(assignmentName)) {
            myHomework.remove(taskIndex);
        }
        else if(taskType.equals(otherName)) {
            myOther.remove(taskIndex);
        }
        else if(taskType.equals(lectureName)) {
            myLecture.remove(taskIndex);
        } else {
            System.err.println("Incorrect taskType inputted");
            return false;
        }
        return true;
    }


    // Error Checkers
    public boolean testTaskType(String taskType) {
        if(taskType.equals(assignmentName) || taskType.equals(quizName) || taskType.equals(lectureName)
                || taskType.equals(otherName))
            return true;
        System.err.println("testTaskType failed " + taskType);
        return false;
    }
    public boolean testStartTimeEndTime(Calendar startTime, Calendar endTime) {
        if(startTime.before(endTime))
            return true;
        System.err.println("testStartTimeEndTime failed");
        return false;
    }
    public boolean testPriority(int priority) {
        if(priority == 1 || priority == 2 || priority == 3)
            return true;
        System.err.println("testPriority failed: " + priority);
        return false;
    }
    public boolean testX(int x) {
        if(x > 0)
            return true;
        System.err.println("testX failed: " + x);
        return false;
    }
    public boolean testY(int y) {
        if(y > 0)
            return true;
        System.err.println("testY failed: " + y);
        return false;
    }
    public boolean testWidth(int width) {
        if(width > 0)
            return true;
        System.err.println("testWidth failed: " + width);
        return false;
    }
    public boolean testHeight(int height) {
        if(height > 0)
            return true;
        System.err.println("testHeight failed: " + height);
        return false;
    }

    // Runs test functions
    public boolean runTests(String taskType,  Calendar startTime, Calendar endTime,
                            int priority,  int x, int y, int width, int height){
        if(!testTaskType(taskType))
            return false;
        if(!testStartTimeEndTime(startTime, endTime))
            return false;
        // Priority is allowed to be invalid for Lecture & othertypes
        if(!(taskType.equals(lectureName) || taskType.equals(otherName)))
            if(!testPriority(priority))
                return false;
        if(!testX(x))
            return false;
        if(!testY(y))
            return false;
        if(!testWidth(width))
            return false;
        if(!testHeight(height))
            return false;
        return true;
    }

    // Getters
    public String getDescription() {
        return description;
    }
    public int getCreditMultiplier() {
        return creditMultiplier;
    }
    public int getMaxHoursPerWeek() {
        return maxHoursPerWeek;
    }
    public String getCourseName() {
        return courseName;
    }
    public int getCredits() {return credits;}

    public ArrayList<Homework> getMyHomework() {
        return myHomework;
    }
    public ArrayList<Other> getMyOther() {
        return myOther;
    }
    public ArrayList<Lecture> getMyLecture() {
        return myLecture;
    }
    public Color getColour() {
        return colour;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCreditMultiplier(int creditMultiplier) {
        if(creditMultiplier < 1)
            creditMultiplier = 1;
        this.creditMultiplier = creditMultiplier;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setMaxHoursPerWeek(int maxHoursPerWeek) {
        this.maxHoursPerWeek = maxHoursPerWeek;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setMyLecture(ArrayList<Lecture> myLecture) {
        this.myLecture = myLecture;
    }
    public void setColour(Color colour) {
        this.colour = colour;
    }

}