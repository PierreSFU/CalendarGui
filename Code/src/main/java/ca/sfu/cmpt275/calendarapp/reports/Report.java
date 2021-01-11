package ca.sfu.cmpt275.calendarapp.reports;

import ca.sfu.cmpt275.calendarapp.calendar.*;
import org.knowm.xchart.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

// References: [59], [60], [61], [62], [63], [64], [65], [66], [68], [69], [70], [71], [72], [73], [74], [75]

public class Report {
    Date periodStart, periodEnd;
    JPanel reportPanel = null;

    public static String FILE_NAME = "";

    public Report(Date periodStart, Date periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        FILE_NAME = "rep_" + getDateValue(periodStart) + "_" + getDateValue(periodEnd); // Sets the report name constant
        display();
    }

    // This function generates a text pane with all of the text we wish to display on the bottom half of the report
    private JTextPane generateText() {
        JTextPane textPane = new JTextPane(); // Create a text pane
        textPane.setContentType("text/plain");
        textPane.setFont(new Font("Calibri", Font.PLAIN, 14)); // Set font
        String numLectures = (getTotalTaskCount(TaskEnum.LECTURE) > 0) ? getTotalTaskCount(TaskEnum.LECTURE) + "" : "no"; // Ensures grammar is correct based on number of lectures in report period
        String numHomework = (getTotalTaskCount(TaskEnum.ASSIGNMENT) > 0) ? getTotalTaskCount(TaskEnum.ASSIGNMENT) + "" : "no"; // Ensures grammar is correct based on number of assignments in report period
        String numOther = (getTotalTaskCount(TaskEnum.OTHER) > 0) ? getTotalTaskCount(TaskEnum.OTHER) + "" : "no"; // Ensures grammar is correct based on number of other tasks in report period
        String lectureWord = (getTotalTaskCount(TaskEnum.LECTURE) != 1) ? "lectures" : "lecture"; // Grammar check
        String hwWord = (getTotalTaskCount(TaskEnum.ASSIGNMENT) != 1) ? "assignments/quizzes" : "assignment/quiz"; // Grammar check
        String dayWord = (getDays(periodStart, periodEnd) != 1) ? "days" : "day"; // Grammar check
        String taskNumSentence = "For the specified " + getDays(periodStart, periodEnd) + " " + dayWord + ", you were given " + numLectures + " " + lectureWord + ", " + numHomework + " " + hwWord + ", and " + numOther + " other course related tasks.";

        String highestPrioTask;
        if (getHighestPriorityTask() == null) { // We don't want to show a numerical value if there ais less than 1 task
            highestPrioTask = "You didn't have any tasks for this period.";
        } else {
            highestPrioTask = "Your highest priority task for this period is \"" + getHighestPriorityTask().getTaskName() + "\" from " + getHighestPriorityTask().getCourseName() + ".";
        }
        textPane.setText(taskNumSentence + "\n" + highestPrioTask + "\n" + generateStats()); // Format and set the text on the pane
        textPane.setEditable(false); // We do not permit editing in the pane

        return textPane;
    }

    private String generateStats() {
        String out = "";

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(periodStart);
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(periodEnd);
        startDateCal.set(Calendar.HOUR_OF_DAY, 9);
        startDateCal.set(Calendar.MINUTE, 0);
        startDateCal.set(Calendar.SECOND, 0);

        endDateCal.set(Calendar.HOUR_OF_DAY, 20);
        endDateCal.set(Calendar.MINUTE, 0);
        endDateCal.set(Calendar.SECOND, 0);
        TaskManager.dataForReport(startDateCal, endDateCal, 14);

        for (ReportBlock rb : TaskManager.getOurReports()) {
            out += "\n" + rb.getCourseName() + ": " + rb.getTotalCourseMinutes() + " total minutes";
        }

        return out;
    }

    // This function loops through the courses and its tasks to determine the task during the report period having the highest priority
    private Task getHighestPriorityTask() {
        Task max = null;
        for (Course c : TaskManager.getOurCourses()) { // Loop through courses
            for (Homework hw : c.getMyHomework()) {
                // Ensure we only check tasks that are within the report period
                if (getTimeMillis(periodStart) <= getTimeMillis(hw.getStartTime().getTime()) && getTimeMillis(hw.getStartTime().getTime()) <= getTimeMillis(periodEnd)) {
                    if (max == null) { // Statement removes possibility of checking the priority of a null object
                        max = hw;
                    } else if (hw.getPriority() > max.getPriority()) {
                        max = hw;
                    }
                }
            }
            for (Other oth : c.getMyOther()) {
                if (getTimeMillis(periodStart) <= getTimeMillis(oth.getStartTime().getTime()) && getTimeMillis(oth.getStartTime().getTime()) <= getTimeMillis(periodEnd)) {
                    if (max == null) {
                        max = oth;
                    } else if (oth.getPriority() > max.getPriority()) {
                        max = oth;
                    }
                }
            }
        }
        return max;
    }

    // Returns a pie chart detailing the time spent by the user for each course during the report's time period
    private PieChart generatePieChart() {
        PieChart chart = new PieChartBuilder().width(500).height(400).title("Course Task Study Distribution").build();

        //JPanel chartPanel = new SwingWrapper(chart).getXChartPanel();
        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(periodStart);
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(periodEnd);
        startDateCal.set(Calendar.HOUR_OF_DAY, 9);
        startDateCal.set(Calendar.MINUTE, 0);
        startDateCal.set(Calendar.SECOND, 0);

        endDateCal.set(Calendar.HOUR_OF_DAY, 20);
        endDateCal.set(Calendar.MINUTE, 0);
        endDateCal.set(Calendar.SECOND, 0);
        TaskManager.dataForReport(startDateCal, endDateCal, 14);
        List<ReportBlock> blocks = TaskManager.getOurReports();

        if (getTotalTaskCount(TaskEnum.LECTURE) + getTotalTaskCount(TaskEnum.ASSIGNMENT) + getTotalTaskCount(TaskEnum.OTHER) != 0) {
            Color[] colours = getCourseColours(); // Create array to store course colours
            for (int i = 0; i < blocks.size(); i++) {
                ReportBlock block = blocks.get(i);
                long taskTime = getTaskTime(block.getCourseName(), TaskEnum.LECTURE) + getTaskTime(block.getCourseName(), TaskEnum.ASSIGNMENT) + getTaskTime(block.getCourseName(), TaskEnum.QUIZ) + getTaskTime(block.getCourseName(), TaskEnum.OTHER);
                //System.out.println("TASK TIME: " + taskTime);
                try {
                    chart.addSeries(block.getCourseName(), taskTime); // Add course and corresponding item count to chart series
                } catch (Exception e) {
                }

            }
            chart.getStyler().setSeriesColors(colours);
        } else {
            chart = null;
        }
        return chart;
    }

    // Get the amount of time spent on a specific task type for a course
    private long getTaskTime(String courseName, TaskEnum taskID) {
        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(periodStart);
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(periodEnd);
        startDateCal.set(Calendar.HOUR_OF_DAY, 9);
        startDateCal.set(Calendar.MINUTE, 0);
        startDateCal.set(Calendar.SECOND, 0);

        endDateCal.set(Calendar.HOUR_OF_DAY, 20);
        endDateCal.set(Calendar.MINUTE, 0);
        endDateCal.set(Calendar.SECOND, 0);

        TaskManager.dataForReport(startDateCal, endDateCal, 14);

        long minutes = 0;
        for (ReportBlock rb : TaskManager.getOurReports()) {
            if (courseName.equals(rb.getCourseName())) {
                if (taskID.equals(TaskEnum.LECTURE)) {
                    minutes = rb.getLectureMinutes();

                } else if (taskID.equals(TaskEnum.ASSIGNMENT)) {
                    minutes = rb.getAssignmentMinutes();

                } else if (taskID.equals(TaskEnum.QUIZ)) {
                    minutes = rb.getQuizMinutes();

                } else if (taskID.equals(TaskEnum.OTHER)) {
                    minutes = rb.getOtherMinutes();

                }
            }
        }
        return minutes;
    }

    // Get the total number of a specific task in report period (for all courses)
    private int getTotalTaskCount(TaskEnum taskID) {
        int count = 0;
        for (Course course : TaskManager.getOurCourses()) {
            if (taskID == TaskEnum.LECTURE) {
                for (Lecture lec : course.getMyLecture()) {
                    if (getTimeMillis(periodStart) <= getTimeMillis(lec.getStartTime().getTime()) && getTimeMillis(lec.getStartTime().getTime()) <= getTimeMillis(periodEnd)) {
                        count++;
                    }
                }
            } else if (taskID == TaskEnum.ASSIGNMENT) {
                for (Homework hw : course.getMyHomework()) {
                    if (getTimeMillis(periodStart) <= getTimeMillis(hw.getStartTime().getTime()) && getTimeMillis(hw.getStartTime().getTime()) <= getTimeMillis(periodEnd)) {
                        count++;
                    }
                }
            } else if (taskID == TaskEnum.OTHER) {
                for (Other oth : course.getMyOther()) {
                    if (getTimeMillis(periodStart) <= getTimeMillis(oth.getStartTime().getTime()) && getTimeMillis(oth.getStartTime().getTime()) <= getTimeMillis(periodEnd)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // Used to get an array of the colours corresponding to each course
    private Color[] getCourseColours() {
        Color[] colours = new Color[TaskManager.getOurReports().size()];
        for (int i = 0; i < TaskManager.getOurReports().size(); i++) {
            colours[i] = TaskManager.getOurReports().get(i).getTheColor();
        }
        return colours;
    }

    // Opens a new JFrame to display report
    public void display() { // IDEA: write report onto graphics object of panel so can save as image
        JFrame frame = new JFrame("Report for " + getDateValue(periodStart) + " - " + getDateValue(periodEnd));
        frame.setPreferredSize(new Dimension(600, 800));
        frame.setResizable(false); // Don't want resizing
        //frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Don't want to exit program, just the report jframe

        JPanel mainPanel = new JPanel(); // Creating a panel to set as the frames content pane (this will store all screen objects)
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Want vertical alignment within this panel
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Border to make this look a bit cleaner

        PieChart pieChart = generatePieChart(); // Generate a pie chart for this report period
        JPanel pieChartPanel = new JPanel();
        if (pieChart != null) { // Null check incase there weren't any courses for this period
            pieChartPanel = new XChartPanel<>(pieChart);
        } else {
            JLabel error = new JLabel("There is not enough information for this period to generate a pie chart!");
            pieChartPanel.add(error);
        }

        JPanel chartPanel = new JPanel(new GridLayout(0, 1)); // This panel will contain the graphs/charts
        JPanel contentPanel = new JPanel(new GridLayout(2, 0)); // This will split the main panel horizontally so we can have text below the charts
        //contentPanel.setBackground(Color.ORANGE);
        chartPanel.add(pieChartPanel);

        contentPanel.add(chartPanel);
        contentPanel.add(generateText());

        // Format the header of the report document
        JLabel header = new JLabel("Study Report");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(new Font("Calibri", Font.BOLD, 24));
        JLabel subHeader = new JLabel(getDateValue(periodStart) + " - " + getDateValue(periodEnd));
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        subHeader.setFont(new Font("Calibri", Font.PLAIN, 16));

        mainPanel.add(header);
        mainPanel.add(subHeader);
        mainPanel.add(contentPanel);
        frame.add(mainPanel);

        JMenuBar menuBar = new JMenuBar(); // Create a menu bar to allow for various options
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // When a user wishes to save a report to file
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select report save directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retVal = chooser.showOpenDialog(frame);
                String selectedDir = "./";
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    selectedDir = chooser.getSelectedFile().getAbsolutePath();
                }

                save(selectedDir, mainPanel);
            }
        });
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener() { // When a user wishes to close the report through the menu bar
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        fileMenu.add(saveItem);
        fileMenu.add(closeItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
        frame.pack(); // Format
        frame.setVisible(true);

        reportPanel = mainPanel;
    }

    // Function for converting a date to a string
    private static String getDateValue(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(date);
    }

    // Gets the number of days in the report period
    private int getDays(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int startTimeMin = (int) ((cal.getTimeInMillis() / 1000) / 60); // Convert to minutes
        cal.setTime(end);
        int endTimeMin = (int) ((cal.getTimeInMillis() / 1000) / 60);

        return ((endTimeMin - startTimeMin) / 60 / 24); // take difference and convert to days
    }

    // Gets the time in milliseconds of a date
    private long getTimeMillis(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    // Saves the current report as an image to a specified location
    public void save(String loc, JPanel reportPanel) {
        if (reportPanel != null) {
            BufferedImage img = new BufferedImage(reportPanel.getWidth(), reportPanel.getHeight(), BufferedImage.TYPE_INT_RGB); // Create a new blank image the size of the report panel
            Graphics2D g2d = img.createGraphics(); // Get the images graphics so we can copy the report panel onto it
            reportPanel.paintAll(g2d); // Paint the report panel onto the image's graphics object
            try { // Try to save the image as a png (catch any io errors)
                if (ImageIO.write(img, "png", new File(loc + "\\" + FILE_NAME + ".png"))) {
                    System.out.println("Report successfully saved.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to save report!");
        }
    }

    // Getter for the report panel
    public JPanel getReportPanel() {
        return reportPanel;
    }
}
