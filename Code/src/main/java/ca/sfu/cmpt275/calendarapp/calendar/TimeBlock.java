package ca.sfu.cmpt275.calendarapp.calendar;

import java.util.Calendar;
public class TimeBlock {
    private Calendar startingTime;
    private Calendar endingTime;

    public TimeBlock(Calendar theStartingTime, Calendar theEndingTime) {
        setStartingTime(theStartingTime);
        setEndingTime(theEndingTime);
    }

// Getters
    public Calendar getStartingTime() {
        return startingTime;
    }
    public Calendar getEndingTime() {
        return endingTime;
    }

// Setters
    public void setStartingTime(Calendar startingTime) {
        this.startingTime = startingTime;
    }
    public void setEndingTime(Calendar endingTime) {
        this.endingTime = endingTime;
    }
}
