package ca.sfu.cmpt275.calendarapp.util;

import ca.sfu.cmpt275.calendarapp.calendar.Course;
import ca.sfu.cmpt275.calendarapp.calendar.ReportBlock;
import ca.sfu.cmpt275.calendarapp.calendar.TaskBlock;
import ca.sfu.cmpt275.calendarapp.calendar.TaskManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalendarFile implements Serializable {
    private static final long serialVersionUID = -1567793641153480705L;

    private ArrayList<ReportBlock> reports;
    private ArrayList<Course> courses;

    public CalendarFile() {
        this.courses = TaskManager.getOurCourses();
        this.reports = TaskManager.getOurReports();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<ReportBlock> getReports() { return reports; }
}
