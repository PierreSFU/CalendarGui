// References: [1], [2], [3], [4], [5], [6], [7], [8], [67], [69], [70], [71], [72], [73], [74], [75]

package ca.sfu.cmpt275.calendarapp.calendar;

import java.util.Calendar;
import java.io.Serializable;

public class Homework implements Task, Serializable{
    private static final long serialVersionUID = -1567793641153480705L;
    private int priority;

    private long numHoursToSpendStudying;
    private Calendar startTime;
    private Calendar endTime;

    private String description;
    private String taskName;
    private String homeworkType;
    private String courseName;

    // Button
    private int x; private int y;
    private int width; private int height;


    public Homework(String ourCourseName, String ourTaskName, Calendar ourStartTime, Calendar ourEndTime, String ourDescription, String homeworkType, int ourPriority,
                    int x, int y, int width, int height) {
        setCourseName(ourCourseName);
        setTaskName(ourTaskName);
        setStartTime(ourStartTime);
        setEndTime(ourEndTime);
        setDescription(ourDescription);
        setPriority(ourPriority);
        setHomeworkType(homeworkType);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

// Functions
    // Clone Homework object
    public Homework cloneHomeworkObject(){
        Homework newClone = new Homework(this.getCourseName(), this.getTaskName(), (Calendar) this.getStartTime().clone(),
                (Calendar) this.getEndTime().clone(), this.getDescription(), this.getHomeworkType(), this.getPriority(),
                this.getX(), this.getY(), this.getWidth(), this.getHeight());
        newClone.setNumHoursToSpendStudying(this.getNumHoursToSpendStudying());

        return newClone;
    }

    // If current item <= itemInputtedIntoFunction return 1, else -1
    public int compareWeight(Homework firstHomework){
        if(this.getPriority() <= firstHomework.getPriority())
            return 1;
        return -1;
    }

    // Converts Homework Object to TaskBlock
    TaskBlock homeworkToTaskBlock(){
        TaskBlock theTaskBlock = new TaskBlock();
        theTaskBlock.setCourseName(getCourseName());
        theTaskBlock.setTaskName(getTaskName());
        theTaskBlock.setStartTime(getStartTime());
        theTaskBlock.setEndTime(getEndTime());
        theTaskBlock.setDescription(getDescription());
        theTaskBlock.setItemType(getHomeworkType());
        theTaskBlock.setPriority(getPriority());
        theTaskBlock.setX(x);
        theTaskBlock.setY(y);
        theTaskBlock.setWidth(width);
        theTaskBlock.setHeight(height);

        return theTaskBlock;
    }
    // Getters
    @Override
    public int getPriority() {
        return priority;
    }
    @Override
    public long getNumHoursToSpendStudying() {
        return numHoursToSpendStudying;
    }
    @Override
    public Calendar getStartTime() {
        return startTime;
    }
    @Override
    public Calendar getEndTime() {
        return endTime;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public String getTaskName() {
        return taskName;
    }
    public String getHomeworkType() {
        return homeworkType;
    }
    public String getCourseName() {
        return courseName;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }


    // Setters
    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }
    @Override
    public void setNumHoursToSpendStudying(long numHoursToSpendStudying) {
        this.numHoursToSpendStudying = numHoursToSpendStudying;
    }
    @Override
    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
    @Override
    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setHomeworkType(String assignmentType) {
        this.homeworkType = assignmentType;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
}