// References: [1], [2], [3], [4], [5], [6], [7], [8], [67], [69], [70], [71], [72], [73], [74], [75]

package ca.sfu.cmpt275.calendarapp.calendar;

import java.io.Serializable;
import java.util.Calendar;

public class Lecture implements Task, Serializable {
    private static final long serialVersionUID = -1567793641153480705L;
    private int recurring;
    private Calendar startTime;
    private Calendar endTime;
    private String description;
    private String courseName;
    private String taskName;

    // Button
    private int x; private int y;
    private int width; private int height;

    public Lecture(String ourCourseName, String ourTaskName, Calendar ourStartTime, Calendar ourEndTime, String ourDescription,  int ourRecurring,
                   int x, int y, int width, int height) {
        setCourseName(ourCourseName);
        setTaskName(ourTaskName);
        setStartTime(ourStartTime);
        setEndTime(ourEndTime);
        setDescription(ourDescription);
        setRecurring(ourRecurring);
        setX(x);
        setY(y);
        this.setWidth(width);
        setHeight(height);
    }
// Functions
    // Clone lecture object
    Lecture cloneLectureObject(){
        Lecture newClone = new Lecture(this.getCourseName(), this.getTaskName(), (Calendar) this.getStartTime().clone(),
                (Calendar) this.getEndTime().clone(), this.getDescription(), this.getRecurring(), this.getX(),
                this.getY(), this.getWidth(), this.getHeight());
        return newClone;
    }


    // Converts lecture Object to TaskBlock
    TaskBlock lectureToTaskBlock(){
        TaskBlock theTaskBlock = new TaskBlock();

        theTaskBlock.setCourseName(getCourseName());
        theTaskBlock.setTaskName(getTaskName());
        theTaskBlock.setItemType(TaskEnum.LECTURE.getLabel());
        theTaskBlock.setStartTime(getStartTime());
        theTaskBlock.setEndTime(getEndTime());
        theTaskBlock.setDescription(getDescription());
        theTaskBlock.setRecurring(getRecurring());
        theTaskBlock.setX(x);
        theTaskBlock.setY(y);
        theTaskBlock.setWidth(width);
        theTaskBlock.setHeight(height);

        return theTaskBlock;
    }

// Getters
    public int getRecurring() {
        return recurring;
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
    public long getNumHoursToSpendStudying() {
        return 0;
    }
    @Override
    public int getPriority() {
        return 0;
    }
    @Override
    public String getDescription() {
        return description;
    }

    public String getCourseName() {
        return courseName;
    }
    @Override
    public String getTaskName() {
        return taskName;
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
    public void setRecurring(int recurring) {
        this.recurring = recurring;
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
    public void setPriority(int priority) {
    }
    @Override
    public void setNumHoursToSpendStudying(long numHoursToSpendStudying) {

    }
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    @Override
    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
