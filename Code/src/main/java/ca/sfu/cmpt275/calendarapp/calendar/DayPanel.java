package ca.sfu.cmpt275.calendarapp.calendar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/*
Holds functionality for a specific day

References: [4], [5], [6], [7], [8], [9], [10], [11], [12], [13], [14], [15], [16], [17], [18], [19],
[20], [21], [22], [23], [24], [25], [26], [27], [28], [29], [30], [31], [32], [33], [34], [35], [36],
[37], [38], [39], [40], [41], [42], [43], [44], [45], [46], [47], [48], [49], [50], [51], [52], [53],
[54], [55], [56], [57], [58] , [69], [70], [71], [72], [73], [74], [75], [76]
 */

public class DayPanel extends JPanel implements MouseListener {
    static private final int numTimes = 12; // Number of ticks allowed
    private final int startTime = 8; // Calendar starts at 8 AM
    private final int unitTimePerBlock = 1; //The unit length a block represents
    private final int maxConflictsAllowed = 2; //To ensure clean look and avoid complications (that are out of scope)
    private final int maxHour = 19; // This is the maximum value time can take for user input (ex. 24 means user can only input until 24 hours - not 25)
    private final int maxMin = 60;
    private final int numMinTicks = 12; // We consider 12 ticks in one hour (5 min intervals)
    private final int yOffsetCol = 26; //Used for keeping the labels of the day not-clickable and drawn appropriately

    private Graphics2D g2D;
    private int panelWidth;
    private int panelWidth_Blocks;
    private int individualPanelHeight;
    private int mainPanelHeight;
    private int base_Zero;
    private int lengthOfMin;
    private JLayeredPane parentPanel;
    private String dateLabel;
    private Calendar panelStartDate; // this is the start date of the panel as a Date object (for use in TaskManager)
    private Calendar panelEndDate; // this is the end date of the panel as a Date object (for use in TaskManager)
    private List<TaskBlock> listOfCurrentlyDrawnBlocks; //only for use to remove/draw on the panel
    private List<TaskBlock> studyBlocks;

    private JLabel dateLabelPanel;

    public DayPanel(int w, int h, int h2, JLayeredPane parentPanel, int baseX_Zero, String l, Calendar datePanel){
        this.parentPanel = parentPanel;
        this.panelWidth = w;
        this.panelWidth_Blocks = this.panelWidth - 2;
        this.individualPanelHeight = h;
        this.mainPanelHeight = h2;
        this.base_Zero = baseX_Zero;
        this.dateLabel = l;
        this.lengthOfMin =  this.individualPanelHeight/this.numMinTicks;
        this.panelStartDate = setPanelDate(datePanel, this.startTime);
        this.panelEndDate = setPanelDate(datePanel, this.maxHour);
        listOfCurrentlyDrawnBlocks = new ArrayList<>();
        studyBlocks = new ArrayList<>();

        this.setBounds(this.base_Zero,0,this.panelWidth,this.mainPanelHeight);
        this.addMouseListener(this);
        addLabels();
    }

    public void changePanelDay(Calendar newDate){
        /*
        Properly change the date of the day panel
        This includes drawing the properly items
         */
        setPanelStartDate(newDate);
        setPanelEndDate(newDate);
        updateLabels();
        removeStudyBlocks();
        redrawBlocks();
        addStudyBlocks();
    }

    private void setPanelStartDate(Calendar newDate){
         /*
        Set the panel start date appropriately
         */
        this.panelStartDate = (Calendar) newDate.clone();
        this.panelStartDate.set(Calendar.HOUR_OF_DAY, this.startTime);
        this.panelStartDate.set(Calendar.MINUTE, 0);
        this.panelStartDate.set(Calendar.SECOND, 0);
    }

    private void setPanelEndDate(Calendar newDate) {
         /*
        Set the panel start date appropriately
         */
        this.panelEndDate = (Calendar) newDate.clone();
        this.panelEndDate.set(Calendar.HOUR_OF_DAY, this.maxHour);
        this.panelEndDate.set(Calendar.MINUTE, 0);
        this.panelEndDate.set(Calendar.SECOND, 0);
    }

    private Calendar setPanelDate(Calendar panelDate, int hour){
        Calendar calendar_Date = (Calendar) panelDate.clone();
        calendar_Date.set(Calendar.HOUR_OF_DAY, hour);
        return calendar_Date;
    }

    private void addLabels(){
        /*
        This is to add the correct labels for the look of the calendar GUI
         */
        this.dateLabelPanel = new JLabel(this.dateLabel + " (" + this.panelStartDate.get(Calendar.YEAR) + "/" + (this.panelStartDate.get(Calendar.MONTH) + 1) + "/" + this.panelStartDate.get(Calendar.DAY_OF_MONTH) + ")");
        dateLabelPanel.setToolTipText("YYYY/MM/DD");
        dateLabelPanel.setBounds(getRelativeX(0), this.yOffsetCol /2, 10,10);
        this.add(this.dateLabelPanel);

        Calendar currentDate = Calendar.getInstance();
        if((this.panelStartDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) && (this.panelStartDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) && (this.panelStartDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR))){
            dateLabelPanel.setForeground(Color.GREEN.darker());
        }
        else{
            dateLabelPanel.setForeground(Color.BLACK);
        }
    }

    private void updateLabels(){
        dateLabelPanel.setText(this.dateLabel + " (" + this.panelStartDate.get(Calendar.YEAR) + "/" + (this.panelStartDate.get(Calendar.MONTH) + 1) + "/" + this.panelStartDate.get(Calendar.DAY_OF_MONTH) + ")");
        Calendar currentDate = Calendar.getInstance();
        if((this.panelStartDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) && (this.panelStartDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) && (this.panelStartDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR))){
            dateLabelPanel.setForeground(Color.GREEN.darker());
        }
        else{
            dateLabelPanel.setForeground(Color.BLACK);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g;

        g2D.setPaint(Color.gray);
        g2D.setStroke(new BasicStroke(1.0f));

        for(int i = 0; i < numTimes; i++ ){
            g2D.drawLine(0,  getActualY(i*this.individualPanelHeight), this.panelWidth,  getActualY(i*this.individualPanelHeight));
        }

        g2D.setStroke(new BasicStroke(4.0f));
        g2D.setPaint(Color.darkGray);
        g2D.drawLine(this.getWidth(), 0, this.getWidth(), 12*this.individualPanelHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getY()>this.yOffsetCol && e.getY() < getActualY((numTimes-1)*this.individualPanelHeight - 3)) {
            if(getStartTimeFromY(e.getY()) != -1) {
                popUpMenu(getStartTimeFromY(getRelativeY(e.getY())));
            }
            else{
                JOptionPane.showMessageDialog(new JFrame(), "Error occurred in attempting to add item here. Please try elsewhere", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    private int getStartTimeFromY(int y){
        /*
        Tells you the start time of the block you clicked on.
        This can be used to provide a user time input.
         */

        int timeStart = this.startTime-this.unitTimePerBlock; // because we end up adding an additional one in for loop below, so start one ahead
        for(int i = 0; i <= this.numTimes; i++){
            if( y <= i*this.individualPanelHeight){
                timeStart += i;
                return (timeStart);
            }
        }
        return -1; //value not in range, something went wrong
    }

    public void popUpMenu(int startTime){
        /*
        Main popup menu when adding an item. This deals with selecting what type of item to add and the time at which to add it
         */
        if(startTime < this.startTime || startTime > this.maxHour){
            JOptionPane.showMessageDialog(new JFrame(), "Error in adding item. Start time was incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel myPanel = new JPanel();
        JLabel labelStart_Colon = new JLabel(":");
        JTextField inputStartTime_Hour = new JTextField(2);
        JTextField inputStartTime_Min = new JTextField(2);
        JLabel labelEnd_Colon = new JLabel(":");
        JTextField inputEndTime_Hour = new JTextField(2);
        JTextField inputEndTime_Min = new JTextField(2);
        JLabel labelMid = new JLabel(" to ");

        inputStartTime_Hour.setText(String.valueOf(startTime));
        inputStartTime_Min.setText("00");
        inputEndTime_Hour.setText(String.valueOf(startTime+this.unitTimePerBlock));
        inputEndTime_Min.setText("00");

        String[] dayOptions = {TaskEnum.QUIZ.getLabel(), TaskEnum.ASSIGNMENT.getLabel(), TaskEnum.OTHER.getLabel() + " Course Related", TaskEnum.LECTURE.getLabel(), TaskEnum.OTHER.getLabel() + " Personal"};
        JComboBox typeToSelect = new JComboBox(dayOptions);
        typeToSelect.setSelectedIndex(0);
        myPanel.add(typeToSelect);
        JLabel labelType = new JLabel("Select Task Type:");

        myPanel.add(labelType);
        myPanel.add(typeToSelect);
        myPanel.add(inputStartTime_Hour);
        myPanel.add(labelStart_Colon);
        myPanel.add(inputStartTime_Min);
        myPanel.add(labelMid);
        myPanel.add(inputEndTime_Hour);
        myPanel.add(labelEnd_Colon);
        myPanel.add(inputEndTime_Min);

        boolean inputNotSuccess = true;
        int tries = 0;

        while(inputNotSuccess) {
            tries++;
            if (tries > 3) {
                JOptionPane.showMessageDialog(new JFrame(), "Exceeded number of tries allowed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } //After 3 tries, close the window (no more input allowed)

            int enteredInfo = JOptionPane.showConfirmDialog(null, myPanel, "Please enter task information.", JOptionPane.OK_CANCEL_OPTION);

            int endTimePanel_Hour = 0;
            int startTimePanel_Hour = 0;
            int startTimePanel_Min = 0;
            int endTimePanel_Min = 0;

            if(enteredInfo == JOptionPane.CLOSED_OPTION || enteredInfo == JOptionPane.CANCEL_OPTION) { return;}

            if (enteredInfo == JOptionPane.YES_OPTION) {
                String chosenType = (String) typeToSelect.getSelectedItem();

                if(inputStartTime_Hour.getText().equals("") || inputStartTime_Min.getText().equals("")){
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time is incorrect. Input cannot be blank", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if(inputEndTime_Hour.getText().equals("") || inputEndTime_Min.getText().equals("")){
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time is incorrect. Input cannot be blank", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    startTimePanel_Hour = Integer.parseInt(inputStartTime_Hour.getText());
                    startTimePanel_Min = Integer.parseInt(inputStartTime_Min.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time is incorrect. Input must me Hour:Min", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    endTimePanel_Hour = Integer.parseInt(inputEndTime_Hour.getText());
                    endTimePanel_Min = Integer.parseInt(inputEndTime_Min.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time is incorrect. Input must me Hour:Min", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                //Need to check input or show error message and return:
                if (startTimePanel_Hour < this.startTime || startTimePanel_Hour > this.maxHour) {
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time (Hour) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (startTimePanel_Min < 0 || startTimePanel_Min > this.maxMin) {
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time (Min) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (endTimePanel_Hour < this.startTime || endTimePanel_Hour > this.maxHour) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time (Hour) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (endTimePanel_Min < 0 || endTimePanel_Min > this.maxMin) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time (Min) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (endTimePanel_Hour < startTimePanel_Hour) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be greater than Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if(startTimePanel_Hour == endTimePanel_Hour && startTimePanel_Min > endTimePanel_Min){
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be greater than Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if(endTimePanel_Hour - startTimePanel_Hour == 1 && startTimePanel_Min == 60 && endTimePanel_Min == 0){
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be exactly Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(endTimePanel_Hour == startTimePanel_Hour && startTimePanel_Min == endTimePanel_Min){
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be exactly Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(startTimePanel_Min == 60){
                    startTimePanel_Min = 0;
                    startTimePanel_Hour++;
                }
                if(endTimePanel_Min == 60){
                    endTimePanel_Min = 0;
                    endTimePanel_Hour++;
                }

                inputNotSuccess = false; // when passes we can break out of the loop as input is no longer needed
                if (chosenType == TaskEnum.QUIZ.getLabel()) {
                    HomeworkPopUP(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, TaskEnum.QUIZ.getLabel());
                } else if (chosenType == TaskEnum.ASSIGNMENT.getLabel()) {
                    HomeworkPopUP(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, TaskEnum.ASSIGNMENT.getLabel());
                } else if (chosenType == TaskEnum.LECTURE.getLabel()) {
                    LecturePopUP(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min);
                } else if (chosenType.equals(dayOptions[2])){
                    HomeworkPopUP(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, TaskEnum.OTHER.getLabel());
                }
                else{
                    addOtherPersonalPopUp(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min);
                }
            }
        }
    }

    private void HomeworkPopUP(int startTimePanel_Hour, int startTimePanel_Min, int endTimePanel_Hour, int endTimePanel_Min, String typeHomework){
       /*
        Popup menu to add an object of type Homework (represents Quizzes, Assignments, and Course - Other)
         */
        String[] courseListArray = CalendarGUI.listCoursesNames();
        if(courseListArray.length == 0){
            JOptionPane.showMessageDialog(new JFrame(), "<html> You must have courses before adding a " + typeHomework + ".<br> Do so by adding a Course under Update Calendar (menu). </html>", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel,BoxLayout.Y_AXIS));
        JLabel labelPriority = new JLabel("Set Priority* [1,2,3]:");
        labelPriority.setToolTipText("3 is most important, 2 is medium, and 1 is least important");
        JTextField labelPriority_input = new JTextField(1);
        JLabel labelTaskName = new JLabel("Name for task*:");
        JTextField labelTaskName_input = new JTextField(10);
        JLabel labelDescription = new JLabel("Add description:");
        JTextField labelDescription_input = new JTextField(20);
        JLabel labelCourseList = new JLabel("Select Course:");
        JComboBox courseList = new JComboBox(courseListArray);
        courseList.setSelectedIndex(0);

        myPanel.add(labelTaskName);
        myPanel.add(labelTaskName_input);
        myPanel.add(labelPriority);
        myPanel.add(labelPriority_input);
        myPanel.add(labelDescription);
        myPanel.add(labelDescription_input);
        myPanel.add(labelCourseList);
        myPanel.add(courseList);

        int priority = 1;
        String label = "";
        String description = "";

        boolean inputNotSuccess = true;
        int tries = 0;

        while(inputNotSuccess) {
            tries++;
            if(tries > 3){
                JOptionPane.showMessageDialog(new JFrame(), "Exceeded number of tries allowed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } //After 3 tries, close the window (no more input allowed)

            int enteredInfo = JOptionPane.showConfirmDialog(null, myPanel, "Please enter task information.", JOptionPane.OK_CANCEL_OPTION);

            if (enteredInfo == JOptionPane.CLOSED_OPTION || enteredInfo == JOptionPane.CANCEL_OPTION) {
                return;
            }

            if (enteredInfo == JOptionPane.YES_OPTION) {
                label = labelTaskName_input.getText();
                description = labelDescription_input.getText();
                String courseSelected = (String) courseList.getSelectedItem();

                if (label.equals("")) {
                    JOptionPane.showMessageDialog(new JFrame(), "Name for task is a required field.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (labelPriority_input.getText().equals("")) {
                    JOptionPane.showMessageDialog(new JFrame(), "Priority is a required field.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    priority = Integer.parseInt(labelPriority_input.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), "Priority is incorrect. Input must be an integer [1,2,3]", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (priority != 1 && priority != 2 && priority != 3) {
                    JOptionPane.showMessageDialog(new JFrame(), "Priority is incorrect. Input must be 1,2, or 3", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (label == "") {
                    JOptionPane.showMessageDialog(new JFrame(), "Label must be filled in", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else {
                    inputNotSuccess = false;
                    if (typeHomework == TaskEnum.QUIZ.getLabel()) {
                        setBlock(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, priority, label, description, TaskEnum.QUIZ.getLabel(), courseSelected, true, Calendar.getInstance(), Calendar.getInstance(), -1, label, Calendar.getInstance(), Calendar.getInstance());
                    }
                    else if(typeHomework == TaskEnum.ASSIGNMENT.getLabel()) {
                        setBlock(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, priority, label, description, TaskEnum.ASSIGNMENT.getLabel(), courseSelected, true, Calendar.getInstance(), Calendar.getInstance(), -1, label, Calendar.getInstance(), Calendar.getInstance());
                    }
                    else{
                        setBlock(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, priority, label, description, TaskEnum.ASSIGNMENT.getLabel(), courseSelected, true, Calendar.getInstance(), Calendar.getInstance(), -1, label, Calendar.getInstance(), Calendar.getInstance());
                    }
                }
            }
        }
    }

    private void LecturePopUP(int startTimePanel_Hour, int startTimePanel_Min, int endTimePanel_Hour, int endTimePanel_Min){
           /*
        Popup menu to add a lecture
         */
        String[] courseListArray = CalendarGUI.listCoursesNames();
        if(courseListArray.length == 0){
            JOptionPane.showMessageDialog(new JFrame(),  "<html> You must have courses before adding a " + TaskEnum.LECTURE.getLabel() + ".<br> Do so by adding a Course under Update Calendar (menu). </html>", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel,BoxLayout.Y_AXIS));
        JLabel labelTaskName = new JLabel("Name for task*:");
        JTextField labelTaskName_input = new JTextField(10);
        JLabel labelDescription = new JLabel("Add description:");
        JTextField labelDescription_input = new JTextField(20);
        JLabel recurringLabel = new JLabel("Recurring:");
        String[] recurringOptions = {"Never", "Once a week", "Once every two weeks"};
        JComboBox recurringOptionSelected = new JComboBox(recurringOptions);
        JLabel labelCourseList = new JLabel("Select Course:");
        JComboBox courseList = new JComboBox(courseListArray);
        courseList.setSelectedIndex(0);

        myPanel.add(labelTaskName);
        myPanel.add(labelTaskName_input);
        myPanel.add(labelDescription);
        myPanel.add(labelDescription_input);
        myPanel.add(recurringLabel);
        myPanel.add(recurringOptionSelected);
        myPanel.add(labelCourseList);
        myPanel.add(courseList);


        boolean inputNotSuccess = true;
        int tries = 0;

        while(inputNotSuccess) {
            tries++;
            if (tries > 3) {
                JOptionPane.showMessageDialog(new JFrame(), "Exceeded number of tries allowed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } //After 3 tries, close the window (no more input allowed)

            int priority = -1;
            String label = "";
            String description = "";

            int enteredInfo = JOptionPane.showConfirmDialog(null, myPanel, "Please enter task information.", JOptionPane.OK_CANCEL_OPTION);

            if (enteredInfo == JOptionPane.CLOSED_OPTION || enteredInfo == JOptionPane.CANCEL_OPTION) {
                return;
            }

            if (enteredInfo == JOptionPane.YES_OPTION) {
                label = labelTaskName_input.getText();
                description = labelDescription_input.getText();
                String recurringSelection = (String) recurringOptionSelected.getSelectedItem();
                int recurringValue = 0;
                String courseSelected = (String) courseList.getSelectedItem();

                if (recurringSelection.equals(recurringOptions[0])) {
                    recurringValue = -1;
                } else if (recurringSelection.equals(recurringOptions[1])) {
                    recurringValue = 7;
                } else if (recurringSelection.equals(recurringOptions[2])) {
                    recurringValue = 14;
                }


                if(label.equals("")){
                    JOptionPane.showMessageDialog(new JFrame(), "Name must be filled in", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else {
                    inputNotSuccess = false; // when passes we can break out of the loop as input is no longer needed
                    setBlock(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, priority, label, description, TaskEnum.LECTURE.getLabel(), courseSelected, true, Calendar.getInstance(), Calendar.getInstance(), recurringValue, label, Calendar.getInstance(), Calendar.getInstance());
                }
            }
        }
    }


    private void addOtherPersonalPopUp(int startTimePanel_Hour, int startTimePanel_Min, int endTimePanel_Hour, int endTimePanel_Min){
        /*
        Popup menu to add a task of type personal other (ex. Doctors appointment)
         */
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel,BoxLayout.Y_AXIS));
        JLabel labelTaskName = new JLabel("Name for task*:");
        JTextField labelTaskName_input = new JTextField(10);
        JLabel labelDescription = new JLabel("Add description:");
        JTextField labelDescription_input = new JTextField(20);

        myPanel.add(labelTaskName);
        myPanel.add(labelTaskName_input);
        myPanel.add(labelDescription);
        myPanel.add(labelDescription_input);

        int priority = -1;
        String label = "";
        String description = "";

        boolean inputNotSuccess = true;
        int tries = 0;

        while(inputNotSuccess) {
            tries++;
            if(tries > 3){
                JOptionPane.showMessageDialog(new JFrame(), "Exceeded number of tries allowed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } //After 3 tries, close the window (no more input allowed)

            int enteredInfo = JOptionPane.showConfirmDialog(null, myPanel, "Please enter task information.", JOptionPane.OK_CANCEL_OPTION);

            if (enteredInfo == JOptionPane.CLOSED_OPTION || enteredInfo == JOptionPane.CANCEL_OPTION) {
                return;
            }

            if (enteredInfo == JOptionPane.YES_OPTION) {
                label = labelTaskName_input.getText();
                description = labelDescription_input.getText();

                if (label.equals("")) {
                    JOptionPane.showMessageDialog(new JFrame(), "Name for task is a required field.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                else {
                    inputNotSuccess = false;
                    setBlock(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, priority, label, description, TaskEnum.OTHER.getLabel(), TaskEnum.PERSONAL.getLabel(), true, Calendar.getInstance(), Calendar.getInstance(), -1, label, Calendar.getInstance(), Calendar.getInstance());
                }
            }
        }
    }

    public void editPopUp(Calendar startTime, Calendar endTime, int priorityDefault, String taskNameDefault, String descriptionDefault, String taskType, String courseName, int recurring){
        /*
        Popup to edit a block successfully
         */
        JPanel bottomPanel = new JPanel();
        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.Y_AXIS));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);

        JLabel label_Start = new JLabel("Time:");
        label_Start.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel labelStart_Colon = new JLabel(":");
        labelStart_Colon.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTextField inputStartTime_Hour = new JTextField(2);
        inputStartTime_Hour.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTextField inputStartTime_Min = new JTextField(2);
        inputStartTime_Min.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel labelEnd_Colon = new JLabel(":");
        labelEnd_Colon.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTextField inputEndTime_Hour = new JTextField(2);
        inputEndTime_Hour.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTextField inputEndTime_Min = new JTextField(2);
        inputEndTime_Min.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel labelMid = new JLabel(" to ");
        labelMid.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel recurringLabel = new JLabel("Recurring:");
        String[] recurringOptions = {"Never", "Once a week", "Once every two weeks"};
        JComboBox recurringOptionSelected = new JComboBox(recurringOptions);


        inputStartTime_Hour.setText(Integer.toString(startTime.get(Calendar.HOUR_OF_DAY)));
        inputStartTime_Min.setText(Integer.toString(startTime.get(Calendar.MINUTE)));
        inputEndTime_Min.setText(Integer.toString(endTime.get(Calendar.MINUTE)));
        inputEndTime_Hour.setText(Integer.toString(endTime.get(Calendar.HOUR_OF_DAY)));

        JLabel labelPriority = new JLabel("Set Priority [1,2,3]:");
        labelPriority.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        labelPriority.setToolTipText("3 is most important, 2 is medium, and 1 is least important");
        JTextField labelPriority_input = new JTextField(1);
        labelPriority_input.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        labelPriority_input.setText(Integer.toString(priorityDefault));
        JLabel labelTaskName = new JLabel("Name for task:");
        labelTaskName.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTextField labelTaskName_input = new JTextField(10);
        labelTaskName_input.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        labelTaskName_input.setText(taskNameDefault);
        JLabel labelDescription = new JLabel("Set description:");
        labelDescription.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTextField labelDescription_input = new JTextField(20);
        labelDescription_input.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        labelDescription_input.setText(descriptionDefault);

        topPanel.add(label_Start);
        topPanel.add(inputStartTime_Hour);
        topPanel.add(labelStart_Colon);
        topPanel.add(inputStartTime_Min);
        topPanel.add(labelMid);
        topPanel.add(inputEndTime_Hour);
        topPanel.add(labelEnd_Colon);
        topPanel.add(inputEndTime_Min);
        if(taskType.equals(TaskEnum.ASSIGNMENT.getLabel()) || taskType.equals(TaskEnum.QUIZ.getLabel())) {
            bottomPanel.add(labelPriority);
            bottomPanel.add(labelPriority_input);
        }
        bottomPanel.add(labelTaskName);
        bottomPanel.add(labelTaskName_input);
        bottomPanel.add(labelDescription);
        bottomPanel.add(labelDescription_input);
        if(taskType.equals((TaskEnum.LECTURE.getLabel()))){
            if(recurring == 7){
                recurringOptionSelected.setSelectedIndex(1);
            }
            else if(recurring == 14){
                recurringOptionSelected.setSelectedIndex(2);
            }
            else{
                recurringOptionSelected.setSelectedIndex(0);
            }
            bottomPanel.add(recurringLabel);
            bottomPanel.add(recurringOptionSelected);
        }


        boolean inputNotSuccess = true;
        int tries = 0;

        while(inputNotSuccess) {
            tries++;
            if (tries > 3) {
                JOptionPane.showMessageDialog(new JFrame(), "Exceeded number of tries allowed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } //After 3 tries, close the window (no more input allowed)

            int enteredInfo = JOptionPane.showConfirmDialog(null, splitPane, "Please enter task information.", JOptionPane.OK_CANCEL_OPTION);

            if (enteredInfo == JOptionPane.CLOSED_OPTION || enteredInfo == JOptionPane.CANCEL_OPTION) {
                return;
            }

            int priority = -1;
            String label = "";
            String description = "";
            int endTimePanel_Hour = 0;
            int startTimePanel_Hour = 0;
            int startTimePanel_Min = 0;
            int endTimePanel_Min = 0;
            Calendar newStartTime = (Calendar) startTime.clone();
            Calendar newEndTime = (Calendar) endTime.clone();

            if (enteredInfo == JOptionPane.YES_OPTION) {
                label = labelTaskName_input.getText();
                description = labelDescription_input.getText();

                try {
                    startTimePanel_Hour = Integer.parseInt(inputStartTime_Hour.getText());
                    startTimePanel_Min = Integer.parseInt(inputStartTime_Min.getText());
                    newStartTime.set(Calendar.YEAR, this.panelStartDate.get(Calendar.YEAR));
                    newStartTime.set(Calendar.MONTH, this.panelStartDate.get(Calendar.MONTH));
                    newStartTime.set(Calendar.DATE, this.panelStartDate.get(Calendar.DATE));
                    newStartTime.set(Calendar.MINUTE, startTimePanel_Min);
                    newStartTime.set(Calendar.HOUR_OF_DAY, startTimePanel_Hour);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time is incorrect. Input must me Hour:Min", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    endTimePanel_Hour = Integer.parseInt(inputEndTime_Hour.getText());
                    endTimePanel_Min = Integer.parseInt(inputEndTime_Min.getText());
                    newEndTime.set(Calendar.YEAR, this.panelStartDate.get(Calendar.YEAR));
                    newEndTime.set(Calendar.MONTH, this.panelStartDate.get(Calendar.MONTH));
                    newEndTime.set(Calendar.DATE, this.panelStartDate.get(Calendar.DATE));
                    newEndTime.set(Calendar.MINUTE, endTimePanel_Min);
                    newEndTime.set(Calendar.HOUR_OF_DAY, endTimePanel_Hour);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time is incorrect. Input must me Hour:Min", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                //Need to check input or show error message and return:
                if (startTimePanel_Hour < CalendarGUI.getStartHr() || startTimePanel_Hour > CalendarGUI.getMaxHourAllowed()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time (Hour) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (startTimePanel_Min < 0 || startTimePanel_Min > CalendarGUI.getMaxMinValue()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Start Time (Min) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (endTimePanel_Hour < CalendarGUI.getStartHr() || endTimePanel_Hour > CalendarGUI.getMaxHourAllowed()) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time (Hour) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (endTimePanel_Min < 0 || endTimePanel_Min > CalendarGUI.getMaxMinValue()) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time (Min) is out of range.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (endTimePanel_Hour < startTimePanel_Hour) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be greater than Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (startTimePanel_Hour == endTimePanel_Hour && startTimePanel_Min > endTimePanel_Min) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be greater than Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (endTimePanel_Hour - startTimePanel_Hour == 1 && startTimePanel_Min == 60 && endTimePanel_Min == 0) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be exactly Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (endTimePanel_Hour == startTimePanel_Hour && startTimePanel_Min == endTimePanel_Min) {
                    JOptionPane.showMessageDialog(new JFrame(), "End Time cannot be exactly Start Time.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (startTimePanel_Min == 60) {
                    startTimePanel_Min = 0;
                    startTimePanel_Hour++;
                }
                if (endTimePanel_Min == 60) {
                    endTimePanel_Min = 0;
                    endTimePanel_Hour++;
                }

                if (taskType.equals(TaskEnum.ASSIGNMENT.getLabel()) || taskType.equals(TaskEnum.QUIZ.getLabel())) {
                    try {
                        priority = Integer.parseInt(labelPriority_input.getText());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(new JFrame(), "Priority is incorrect. Input must be an integer [1,2,3]", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    if (priority != 1 && priority != 2 && priority != 3) {
                        JOptionPane.showMessageDialog(new JFrame(), "Priority is incorrect. Input must be 1,2, or 3", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } else if (label == "") {
                    JOptionPane.showMessageDialog(new JFrame(), "Label must be filled in", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                int recursionSelected = 0;
                if (taskType.equals(TaskEnum.LECTURE.getLabel())) {
                    String recurringSelection = (String) recurringOptionSelected.getSelectedItem();

                    if (recurringSelection.equals(recurringOptions[2])) {
                        recursionSelected = 14;
                    } else if (recurringSelection.equals(recurringOptions[1])) {
                        recursionSelected = 7;
                    } else {
                        recursionSelected = 0;
                    }
                }

                if (!canUpdateTime(newStartTime, newEndTime, courseName, taskNameDefault, taskType, startTime, endTime)) {
                    JOptionPane.showMessageDialog(new JFrame(), "Too many conflicts. Cannot update time", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                inputNotSuccess = false;
                setBlock(startTimePanel_Hour, startTimePanel_Min, endTimePanel_Hour, endTimePanel_Min, priority, label, description, taskType, courseName, false, startTime, endTime, recursionSelected, taskNameDefault, startTime, endTime);
            }
        }
    }

    private boolean canUpdateTime(Calendar newStartTime, Calendar newEndTime, String courseName, String taskName, String taskType, Calendar oldStartTime, Calendar oldEndTime){
        /*
        Tells you if the new time has too many conflicts (which is not allowed due to it not looking clean)
         */
        int y1 = getY1(newStartTime.get(Calendar.HOUR_OF_DAY), newStartTime.get(Calendar.MINUTE));
        int y2 = getY2(newEndTime.get(Calendar.HOUR_OF_DAY), newEndTime.get(Calendar.MINUTE));

        List<TaskBlock> listConflicts = getConflicts(y1, y2, true, taskType, taskName, oldStartTime, oldEndTime);

        if(listConflicts.size() >= this.maxConflictsAllowed){ return false; }
        return true;
    }

    private int compareBlocks(JButton first, JButton second){
        /*
        Used for ordering the blocks from leftmost to rightmost in the calendar GUI
         */
        if(first.getX()<second.getX()){
            return -1;
        }
        else{
            return 1;
        }
    }

    private int getActualY(int y_orig){
        return (this.yOffsetCol + y_orig);
    } //This is to deal with the offset needed for proper layout (title at top)

    private int getRelativeY(int y_orig){
        return (y_orig - this.yOffsetCol);
    }//This is to deal with the offset needed for proper layout (title at top)

    private int getRelativeX(int x_orig){
        return (x_orig-this.base_Zero);
    } //Since we have the value relative to LayeredPane but panels located at different places on it we offset to account for this
    //Instead of considering starting x location of panel we consider it as zero

    private int convertToOurX(int x_orig){
        return (x_orig+this.base_Zero);
    } //Since we have the value relative to LayeredPane but panels located at different places on it we offset to account for this


    public void redrawBlocks(){
        /*
        Redraws the task blocks onto the panel by clearing the old blocks and getting an updated list
         */

        for(int i = 0; i < listOfCurrentlyDrawnBlocks.size(); i++){
            listOfCurrentlyDrawnBlocks.get(i).getButton().setVisible(false);
            this.parentPanel.remove(listOfCurrentlyDrawnBlocks.get(i).getButton());
            this.remove(listOfCurrentlyDrawnBlocks.get(i).getButton());
        }
        listOfCurrentlyDrawnBlocks.clear();

        listOfCurrentlyDrawnBlocks.addAll(getRecentTaskBlocks());

        for(int i = 0; i < listOfCurrentlyDrawnBlocks.size(); i++){
            int y = listOfCurrentlyDrawnBlocks.get(i).getY();
            int x = listOfCurrentlyDrawnBlocks.get(i).getX();
            int height = listOfCurrentlyDrawnBlocks.get(i).getHeight();
            int width = listOfCurrentlyDrawnBlocks.get(i).getWidth();
            listOfCurrentlyDrawnBlocks.get(i).getButton().setBounds(x,y,width,height);
            listOfCurrentlyDrawnBlocks.get(i).getButton().setToolTipText(listOfCurrentlyDrawnBlocks.get(i).getDescription());
            this.parentPanel.add(listOfCurrentlyDrawnBlocks.get(i).getButton(), JLayeredPane.DRAG_LAYER);
            listOfCurrentlyDrawnBlocks.get(i).getButton().setVisible(true);
        }

    }

    private List<TaskBlock> getAllRecentTaskBlocks(){
        /*
        Gets the most recent list of task blocks for the current date (of this day panel)
         */
        List<TaskBlock> updatedListBlocks = new ArrayList<>();
        String[] courseNames = CalendarGUI.listCoursesNames();
        for(int i =0; i < courseNames.length; i++) {
            updatedListBlocks.addAll(TaskManager.getTaskBlocks(courseNames[i], this.panelStartDate, this.panelEndDate));
        }
        return updatedListBlocks;
    }

    private List<TaskBlock> getRecentTaskBlocks(){
        /*
        Gets all the task blocks in the day
         */
        List<TaskBlock> updatedListBlocks = new ArrayList<>();
        String[] courseNames = CalendarGUI.listCoursesNames();
        for(int i =0; i < courseNames.length; i++) {
            updatedListBlocks.addAll(addTaskBlocksWithColouring(courseNames[i]));
        }
        return updatedListBlocks;
    }

    private List<TaskBlock> addTaskBlocksWithColouring(String courseName){
        /*
        Returns task blocks for a specified course with the correct colouring applied
         */

        List<TaskBlock> courseTaskBlocks = TaskManager.getTaskBlocksAndRecurring(courseName, this.panelStartDate, this.panelEndDate);

        //Get course colour
        Color courseColor = getCourseColour(courseName);

        //Now that you have the specific course's colour, apply the correct colouring to all its task blocks
        for(int i = 0; i < courseTaskBlocks.size(); i++){
            Color taskColor = CalendarGUI.getColorByCourse(courseColor, courseTaskBlocks.get(i).getItemType());
            courseTaskBlocks.get(i).setColour(taskColor);
            courseTaskBlocks.get(i).resetColour();
            courseTaskBlocks.get(i).setBorder(courseColor);
        }
        return courseTaskBlocks;
    }

    private Color getCourseColour(String courseName){
        /*
        Gives the course colour based on its name
         */
        List<Course> listofCourses = TaskManager.getOurCourses();
        Course currentCourse = listofCourses.get(TaskManager.findCourseIndex(courseName));
        Color courseColor = currentCourse.getColour();
        return courseColor;
    }


    private void addNewBlock(int x1, int y1, int width, int height, int priorityBlock, String labelBlock, String descriptionBlock, String typeBlock, String courseBlock, int recurring){
        /*
        Adds a new block to TaskManager's list and updates the day panel to show it
         */
        boolean passedAdding = TaskManager.addCourseTask(courseBlock, typeBlock, labelBlock, getDate(y1), getDate(y1+height), descriptionBlock, priorityBlock, recurring, convertToOurX(x1), getActualY(y1), width, height);

        if(!passedAdding) {
            JOptionPane.showMessageDialog(new JFrame(), "System not able to add the task.", "Error", JOptionPane.WARNING_MESSAGE);
        }

        redrawBlocks();
    }

    private Calendar getDate(int y){
        /*
        Gets the date based on the y value
         */
        int dateStart_Hour = getStartTimeFromY(getActualY(y)) ;
        int dateStart_Min = getMinFromY(y);

        Calendar calendar_Date = (Calendar) panelStartDate.clone();
        calendar_Date.set(Calendar.HOUR_OF_DAY, dateStart_Hour);
        calendar_Date.set(Calendar.MINUTE, dateStart_Min);
        calendar_Date.set(Calendar.SECOND, 0);
        return calendar_Date;
    }

    private int getMinFromY(int y){
        /*
        Gets you the minute portion of the date based on the y value
         */
        int y_min = 0;

        //Get the y part for just the minute (remove the hour)
        for(int i = 0; i <= this.numTimes; i++){
            if( y <= i*this.individualPanelHeight){
                y_min = y - (i-1)*this.individualPanelHeight;
                break;
            }
        }

        for(int tick=0; tick<this.numMinTicks; tick++){
            if(y_min < tick*this.lengthOfMin){
                return (tick-1)*this.lengthOfMin;
            }
        }
        return 0; //default to 0 to avoid major problems
    }

    private int getY1(int startTimeY, int minStartTime){
        /*
        Gets you the starting y value of a block based on the start hour and min
         */
        int y1 = this.individualPanelHeight*(startTimeY-8);
        y1 += getYFromMinutes(minStartTime);

        return y1;
    }

    private int getY2(int endTime, int minEndTime){
        /*
        Gets you the ending y value of a block based on the start hour and min
         */
        int y2 = this.individualPanelHeight*(endTime-8);
        y2 += getYFromMinutes(minEndTime);

        return y2;
    }

    private int getYFromMinutes(int min){
        /*
        Gets the y value (location on pane for block) based on the minute
         */
        for(int tick=0; tick<this.numMinTicks; tick++){
            if(min < tick*this.lengthOfMin){
                return (tick-1)*this.lengthOfMin;
            }
        }
        return 0; //default to 0 to avoid major problems
    }

    private List<TaskBlock> getConflicts(int y1_ours, int y2_ours, boolean removingBlockEdited, String taskType, String taskName, Calendar startTime, Calendar endTime){
        /*
        This function will return a list of taskBlocks that are conflicting with the one we want to add
        y1_ours and y2_ours defines the start and end y values of the task block we want to add
         */
        List<TaskBlock> listOfRecentBlocks = getAllRecentTaskBlocks();
        List<TaskBlock> blocksConflicting = new ArrayList<>();

        if(removingBlockEdited){
            for(int i = 0; i < listOfRecentBlocks.size(); i++){
                if(listOfRecentBlocks.get(i).getItemType().equals(taskType) && listOfRecentBlocks.get(i).getTaskName().equals(taskName) && listOfRecentBlocks.get(i).getStartTime().equals(startTime) && listOfRecentBlocks.get(i).getEndTime().equals(endTime)){
                    listOfRecentBlocks.remove(i);
                }
            }
        }

        for (int block = 0; block < listOfRecentBlocks.size(); block++) {
            int y1 = getRelativeY(listOfRecentBlocks.get(block).getY());
            int y2 = y1 + listOfRecentBlocks.get(block).getHeight();

            if(y1 > y1_ours && y1 < y2_ours){
                blocksConflicting.add(listOfRecentBlocks.get(block));
            }
            else if(y2 > y1_ours && y2 < y2_ours){
                blocksConflicting.add(listOfRecentBlocks.get(block));
            }
            else if(y2 == y2_ours && y1 == y1_ours){
                blocksConflicting.add(listOfRecentBlocks.get(block));
            }
            else if(y1_ours > y1 && y1_ours < y2){
                blocksConflicting.add(listOfRecentBlocks.get(block));
            }
            else if(y2_ours > y1 && y2_ours < y2){
                blocksConflicting.add(listOfRecentBlocks.get(block));
            }
        }
        return blocksConflicting;
    }



    private void setBlock(int startTime, int startTimeMin, int endTime, int endTimeMin, int priorityBlock, String labelBlock, String descriptionBlock, String typeBlock, String courseBlock, boolean areAddingNewBlock, Calendar editStartTime, Calendar editEndTime, int recurrance, String oldLabel, Calendar oldStartTime, Calendar oldEndTime){
       /*
        Sets where the block should go on the panel (and its dimensions), and resizes conflicting blocks if needed
         */
        int y1_ours =  getY1(startTime, startTimeMin);
        int y2_ours = getY2(endTime, endTimeMin);
        int heightOfBlock = y2_ours - y1_ours;

        List<TaskBlock> buttonsConflicting = getConflicts(y1_ours, y2_ours,!areAddingNewBlock, typeBlock, oldLabel, oldStartTime, oldEndTime);

        int numConflicts = buttonsConflicting.size();

        if(numConflicts >= this.maxConflictsAllowed){
            JOptionPane.showMessageDialog(new JFrame(), "Too many items in the same time range will distort the view. It is not allowed", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (numConflicts > 0){
        /*
        We have conflicts that we must deal with (resizing)
         */
            int newWidth = (this.panelWidth_Blocks+1) / (numConflicts+1); //plus one to include block that will be added

        /*
        Case One: we have conflicts but it does not require resizing. Instead we must place new block in an available space
                  between existing blocks
         */
            if(buttonsConflicting.get(0).getWidth() <= newWidth){
                if(buttonsConflicting.size() ==1){
                /*
                 Case One - Situation A: We have only one conflicting block, which can be dealt in an easy way
                */
                    int newButtonWidth = this.panelWidth_Blocks - buttonsConflicting.get(0).getWidth();

                    if(getRelativeX(buttonsConflicting.get(0).getX()) > 1){
                        //the conflicting button is on the right side, so we want to add the new button on the left side
                        if(areAddingNewBlock) {
                            addNewBlock(0, y1_ours, newButtonWidth, heightOfBlock, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance);
                        }
                        else{
                            editTaskBlock(startTime, startTimeMin, endTime, endTimeMin, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance, 0, y1_ours, newButtonWidth, heightOfBlock, oldLabel, oldStartTime, oldEndTime);

                        }
                    }
                    else{
                        if(areAddingNewBlock) {
                            addNewBlock(buttonsConflicting.get(0).getWidth(), y1_ours, newButtonWidth, heightOfBlock, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance);
                        }
                        else{
                            editTaskBlock(startTime, startTimeMin, endTime, endTimeMin, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance, buttonsConflicting.get(0).getWidth(), y1_ours, newButtonWidth, heightOfBlock, oldLabel, oldStartTime, oldEndTime);

                        }
                    }
                }
                else{
                 /*
                 Case One - Situation B: We have multiple conflicting blocks, so we must search for the location to add the new block
                */

                    //Sort the buttons in order from smallest to largest x value - leftmost button to rightmost button
                    Collections.sort(buttonsConflicting, (o1, o2) -> compareBlocks(o1.getButton(), o2.getButton()));

                    if(getRelativeX(buttonsConflicting.get(0).getX()) > 1){
                        // We have a spot right before the leftmost conflicting block
                        int newButtonWidth = getRelativeX(buttonsConflicting.get(0).getX());
                        if(areAddingNewBlock) {
                            addNewBlock(0, y1_ours, newButtonWidth, heightOfBlock, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance);
                        }
                        else{
                            editTaskBlock(startTime, startTimeMin, endTime, endTimeMin, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance, 0, y1_ours, newButtonWidth, heightOfBlock, oldLabel, oldStartTime, oldEndTime);

                        }
                    }
                    else {
                        for (int i = 0; i < buttonsConflicting.size()-1; i++) {
                            int x_end = getRelativeX(buttonsConflicting.get(i).getX()) + buttonsConflicting.get(i).getWidth();
                            int x_next_start = getRelativeX(buttonsConflicting.get(i+1).getX());

                            if(Math.abs(x_next_start - x_end) > 1){
                                int newButtonWidth = Math.abs(x_next_start - x_end);
                                if(areAddingNewBlock) {
                                    addNewBlock(x_end, y1_ours, newButtonWidth, heightOfBlock, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance);
                                }
                                else{
                                    editTaskBlock(startTime, startTimeMin, endTime, endTimeMin, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance, x_end, y1_ours, newButtonWidth, heightOfBlock, oldLabel, oldStartTime, oldEndTime);

                                }
                            }
                        }
                    }

                }
            }
            else{
                 /*
                Case Two: we have conflicts and all blocks must be resized
                */
                for(int i =0; i<buttonsConflicting.size(); i++){
                    TaskManager.editCourseTask(buttonsConflicting.get(i).getCourseName(), buttonsConflicting.get(i).getItemType(), buttonsConflicting.get(i).getTaskName(), buttonsConflicting.get(i).getStartTime(), buttonsConflicting.get(i).getEndTime(), buttonsConflicting.get(i).getTaskName(), buttonsConflicting.get(i).getStartTime(), buttonsConflicting.get(i).getEndTime(), buttonsConflicting.get(i).getDescription(),
                            buttonsConflicting.get(i).getPriority(), buttonsConflicting.get(i).getRecurring(),convertToOurX(i * newWidth), buttonsConflicting.get(i).getY(), newWidth, buttonsConflicting.get(i).getHeight());
                }
                if(areAddingNewBlock) {
                    addNewBlock(numConflicts * newWidth, y1_ours, newWidth, heightOfBlock, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance);
                }
                else{
                    editTaskBlock(startTime, startTimeMin, endTime, endTimeMin, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance, numConflicts * newWidth, y1_ours, newWidth, heightOfBlock, oldLabel, oldStartTime, oldEndTime);

                }
            }
        }
        else{
        /*
        We have no conflicts to deal with
         */
            if(areAddingNewBlock) {
                addNewBlock(0, y1_ours, this.panelWidth_Blocks, heightOfBlock, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance);
            }
            else{
                editTaskBlock(startTime, startTimeMin, endTime, endTimeMin, priorityBlock, labelBlock, descriptionBlock, typeBlock, courseBlock, recurrance, 0, y1_ours, this.panelWidth_Blocks, heightOfBlock, oldLabel, oldStartTime, oldEndTime);

            }
        }

    }

    private void
    editTaskBlock(int startTime, int startTimeMin, int endTime, int endTimeMin, int priorityBlock, String labelBlock, String descriptionBlock, String typeBlock, String courseBlock, int recurring, int x, int y, int width, int height, String oldLabel, Calendar oldStartTime, Calendar oldEndTime){
        /*
       Edit a specified task block
         */
        Calendar startTimeCal = (Calendar) this.panelStartDate.clone();
        startTimeCal.set(Calendar.HOUR_OF_DAY, startTime);
        startTimeCal.set(Calendar.MINUTE, startTimeMin);
        Calendar endTimeCal = (Calendar) this.panelStartDate.clone();
        endTimeCal.set(Calendar.HOUR_OF_DAY, endTime);
        endTimeCal.set(Calendar.MINUTE, endTimeMin);

        boolean ableToEdit = TaskManager.editCourseTask(courseBlock, typeBlock, oldLabel, oldStartTime, oldEndTime, labelBlock, startTimeCal, endTimeCal, descriptionBlock, priorityBlock, recurring, convertToOurX(x), getActualY(y), width, height);

        if(!ableToEdit){
            JOptionPane.showMessageDialog(new JFrame(), "Unable to edit block", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        redrawBlocks();
    }


    public void addStudyBlocks(){
        /*
        Add the last generated study blocks to this day panel
         */
        removeStudyBlocks();
        studyBlocks.addAll(CalendarGUI.getSpecificDayStudyBlocks(this.panelStartDate, this.panelEndDate));

        for(int i = 0; i < studyBlocks.size(); i++){
            int y = getY1(studyBlocks.get(i).getStartTime().get(Calendar.HOUR_OF_DAY), studyBlocks.get(i).getStartTime().get(Calendar.MINUTE));
            int y_end = getY2(studyBlocks.get(i).getEndTime().get(Calendar.HOUR_OF_DAY), studyBlocks.get(i).getEndTime().get(Calendar.MINUTE));
            int height = y_end - y;
            studyBlocks.get(i).getButton().setBounds(convertToOurX(0), getActualY(y), this.panelWidth_Blocks, height);
            studyBlocks.get(i).getButton().setText("<html>"+studyBlocks.get(i).getCourseName() + ": <br>" + studyBlocks.get(i).getTaskName()+"</html>");
            studyBlocks.get(i).getButton().setToolTipText("Study during this time for the " + studyBlocks.get(i).getTaskName() + " for course " + studyBlocks.get(i).getCourseName() + ".This task has priority " +studyBlocks.get(i).getPriority()+".");
            Color studyBlockColour = CalendarGUI.getColorByCourse(getCourseColour(studyBlocks.get(i).getCourseName()),  TaskEnum.STUDYBLOCK.getLabel());
            studyBlocks.get(i).getButton().setBackground(studyBlockColour);
            studyBlocks.get(i).getButton().setBorder(new LineBorder(studyBlockColour.darker(), 1));
            this.parentPanel.add(studyBlocks.get(i).getButton(), JLayeredPane.DRAG_LAYER);
            studyBlocks.get(i).getButton().setVisible(true);
        }
    }

    public void removeStudyBlocks(){
        /*
        Remove study blocks currently present on the day panel
         */
        for(int i = 0; i < studyBlocks.size(); i++){
            studyBlocks.get(i).getButton().setVisible(false);
            this.parentPanel.remove(studyBlocks.get(i).getButton());
            this.remove(studyBlocks.get(i).getButton());
        }
        studyBlocks.clear();
    }


    public void deleteSpecifiedBlock(TaskBlock blockToDelete){
        /*
        Delete the block passed in by blockToDelete and resize all blocks that conflict with it if needed
         */
        int y1_ours = blockToDelete.getY();
        int y2_ours = y1_ours + blockToDelete.getHeight();

        //Get list of all conflicts
        List<TaskBlock> conflictingBlocks = getConflicts(y1_ours, y2_ours, true, blockToDelete.getItemType(), blockToDelete.getTaskName(), blockToDelete.getStartTime(), blockToDelete.getEndTime());

        boolean deleteSuccess = TaskManager.deleteCourseTask(blockToDelete.getCourseName(), blockToDelete.getItemType(), blockToDelete.getTaskName(), blockToDelete.getStartTime(), blockToDelete.getEndTime());

        if(!deleteSuccess){
            JOptionPane.showMessageDialog(new JFrame(), "Could not delete item.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        //If there are conficts with the conflicts then do not resize anything, otherwise resize
        if(!areSecondaryConflicts(conflictingBlocks)){
            //Need to resize conflicting blocks:
            for(int i = 0; i < conflictingBlocks.size(); i++) {
                if(conflictingBlocks.get(i).getX() < 1){
                    //The conflicting block was at x = 0 - only need to resize width
                    TaskManager.editCourseTask(conflictingBlocks.get(i).getCourseName(), conflictingBlocks.get(i).getItemType(), conflictingBlocks.get(i).getTaskName(), conflictingBlocks.get(i).getStartTime(), conflictingBlocks.get(i).getEndTime(), conflictingBlocks.get(i).getTaskName(), conflictingBlocks.get(i).getStartTime(), conflictingBlocks.get(i).getEndTime(),  conflictingBlocks.get(i).getDescription(), conflictingBlocks.get(i).getPriority(), conflictingBlocks.get(i).getRecurring(), conflictingBlocks.get(i).getX(), conflictingBlocks.get(i).getY(), this.panelWidth_Blocks, conflictingBlocks.get(i).getHeight());
                }
                else{
                    //The conflicting block was at x != 0 - need to resize and shift left
                    TaskManager.editCourseTask(conflictingBlocks.get(i).getCourseName(), conflictingBlocks.get(i).getItemType(), conflictingBlocks.get(i).getTaskName(), conflictingBlocks.get(i).getStartTime(), conflictingBlocks.get(i).getEndTime(), conflictingBlocks.get(i).getTaskName(), conflictingBlocks.get(i).getStartTime(), conflictingBlocks.get(i).getEndTime(),  conflictingBlocks.get(i).getDescription(), conflictingBlocks.get(i).getPriority(), conflictingBlocks.get(i).getRecurring(), convertToOurX(0), conflictingBlocks.get(i).getY(), this.panelWidth_Blocks, conflictingBlocks.get(i).getHeight());
                }
            }
        }
        redrawBlocks();
    }

    private boolean areSecondaryConflicts(List<TaskBlock> listPrimaryConflicts){
        /*
           For a given list of conflicts listPrimaryConflicts, this finds the blocks that conflict with the blocks in listPrimaryConflicts
         */
        for(int i = 0; i < listPrimaryConflicts.size(); i++){
            int y1 = listPrimaryConflicts.get(i).getY();
            int y2 = y1 + listPrimaryConflicts.get(i).getHeight();
            if(getConflicts(y1, y2, true, listPrimaryConflicts.get(i).getItemType(), listPrimaryConflicts.get(i).getTaskName(), listPrimaryConflicts.get(i).getStartTime(), listPrimaryConflicts.get(i).getEndTime()).size() != 0){
                return true;
            }
        }
        return false;
    }

    static public int getNumTimes(){ return numTimes;}

}
