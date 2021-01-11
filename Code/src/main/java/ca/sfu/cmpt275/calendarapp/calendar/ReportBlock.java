package ca.sfu.cmpt275.calendarapp.calendar;

import java.awt.*;
import java.io.Serializable;

public class ReportBlock implements Serializable {
    private static final long serialVersionUID = -1567793641153480705L;
    private long totalCourseMinutes;
    private long quizMinutes;
    private long assignmentMinutes;
    private long lectureMinutes;
    private long otherMinutes;

    private String courseName;
    private Color theColor;

    ReportBlock(String theCourseName){
        setCourseName(theCourseName);
        setTotalCourseMinutes(0);
        setQuizMinutes(0);
        setAssignmentMinutes(0);
        setLectureMinutes(0);
        setOtherMinutes(0);
        setTheColor(Color.BLACK);
    }

// Setters
    public void setTotalCourseMinutes(long totalCourseMinutes) {
        this.totalCourseMinutes = totalCourseMinutes;
    }
    public void setQuizMinutes(long quizMinutes) {
        this.quizMinutes = quizMinutes;
    }
    public void setAssignmentMinutes(long assignmentMinutes) {
        this.assignmentMinutes = assignmentMinutes;
    }
    public void setLectureMinutes(long lectureMinutes) {
        this.lectureMinutes = lectureMinutes;
    }
    public void setOtherMinutes(long otherMinutes) {
        this.otherMinutes = otherMinutes;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setTheColor(Color theColor) {
        this.theColor = theColor;
    }


// Getters
    public long getTotalCourseMinutes() {
        return totalCourseMinutes;
    }
    public long getQuizMinutes() {
        return quizMinutes;
    }
    public long getAssignmentMinutes() {
        return assignmentMinutes;
    }
    public long getLectureMinutes() {
        return lectureMinutes;
    }
    public long getOtherMinutes() {
        return otherMinutes;
    }
    public String getCourseName() {
        return courseName;
    }
    public Color getTheColor() {
        return theColor;
    }
}
