package ca.sfu.cmpt275.calendarapp.calendar;

import java.util.Calendar;
import java.util.Date;

/*
Note: Not used yet.
 */

public interface Task {
// Setters
    void setDescription(String description);
    void setTaskName(String taskName);
    void setCourseName(String courseName);
    void setStartTime(Calendar startTime);
    void setEndTime(Calendar endTime);
    void setPriority(int priority);
    void setNumHoursToSpendStudying(long numHoursToSpendStudying);
    void setX(int x);
    void setY(int y);
    void setWidth(int width);
    void setHeight(int height);

// Getters
    long getNumHoursToSpendStudying();
    int getPriority();
    String getDescription();
    String getCourseName();
    String getTaskName();
    Calendar getStartTime();
    Calendar getEndTime();
    int getX();
    int getY();
    int getWidth();
    int getHeight();
}