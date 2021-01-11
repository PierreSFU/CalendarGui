/*
Description: - TaskBlock holds variables for CalendarGUI so that given variables can be backed up appropriately and
             show up on the CalendarGUI.
             - TaskBlock also adds button functionality, gets input from the user and is essentially the interface that
             the user will interact with most of the time. The only exception is the generate schedule button

References: [4], [5], [6], [7], [8], [9], [10], [11], [12], [13], [14], [15], [16], [17], [18], [19],
[20], [21], [22], [23], [24], [25], [26], [27], [28], [29], [30], [31], [32], [33], [34], [35], [36],
[37], [38], [39], [40], [41], [42], [43], [44], [45], [46], [47], [48], [49], [50], [51], [52], [53],
[54], [55], [56], [57], [58] , [69], [70], [71], [72], [73], [74], [75], [76]
*/
package ca.sfu.cmpt275.calendarapp.calendar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class TaskBlock implements ActionListener
{
    private Color colour;
    private Calendar dayOfWeek;
    private Calendar startTime;
    private Calendar endTime;
    private int priority;
    private int recurring;
    private String taskName;
    private String description;
    private String courseName;
    private String itemType; // Quiz, Assignment, Other
    private JButton button;

    // Button
    int x; int y;
    int width; int height;

    TaskBlock()
    {
        // GUI
        button = new JButton("");
        button.addActionListener(this);
        taskName = "";
        colour = Color.black;

        // TaskManager Info
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        description = "";
        courseName = "";
        priority = -1;
        itemType = "";
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }


    TaskBlock(Calendar theStartTime, Calendar theEndTime, String theCourseName,
              String theTaskName, String theItemType, int newPriority, Color theColor){
        button = new JButton("");
        button.setBorder(new LineBorder(Color.BLACK));

        // TaskManager Info
        description = "";
        priority = -1;
        this.colour = Color.black;

        x = 0;
        y = 0;
        width = 0;
        height = 0;
        button.addActionListener(this);

        setStartTime(theStartTime);
        setEndTime(theEndTime);
        setCourseName(theCourseName);
        setTaskName(theTaskName);
        setItemType(theItemType);
        setPriority(newPriority);
        setColour(theColor);
    }

// Getters
    public JButton getButton(){ return button; }
    public String getTaskName(){ return taskName; }
    public Color getColour() {
        return colour;
    }
    public Calendar getStartTime() {
        return startTime;
    }
    public Calendar getEndTime() {
        return endTime;
    }
    public int getPriority() {
        return priority;
    }
    public String getDescription() {
        return description;
    }
    public String getCourseName() {
        return courseName;
    }
    public String getItemType() {
        return itemType;
    }
    public int getRecurring() {
        return recurring;
    }
    public Calendar getDayOfWeek() {
        return dayOfWeek;
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
    public void setTaskName(String l){ taskName = l;this.button.setText(l);
    }
    public void setSize(int width, int height){ button.setSize(width, height); }
    public void setColour(Color colour) { this.colour = colour; }
    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public void setButton(JButton button) {
        this.button = button;
    }
    public void setRecurring(int recurring) {
        this.recurring = recurring;
    }
    public void setDayOfWeek(Calendar dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

// Button Functions
    public void changeColour(Color c){ button.setBackground(c); }
    public void resetColour(){ button.setBackground(colour); }
    public void applyLabel(){ button.setText(taskName); }
    public void setBorder(Color courseColor){
        button.setBorder(new LineBorder(CalendarGUI.getBorderColorByCourse(courseColor), 1));
    }

// TaskManager Functions
    public void addItemToTaskManager(){
        TaskManager.addCourseTask(this.courseName, this.itemType, this.taskName, this.startTime,
                this.endTime, this.description, this.priority, this.recurring,
                this.x, this.y, this.width, this.height);
    }

    public void deleteItemFromTaskManager(){
        TaskManager.deleteCourseTask(this.courseName, this.itemType, this.taskName, this.startTime,
                this.endTime);
    }

    private void PopUpMenu(){
        /*
        User can either edit or delete the object (Quiz, Assignment, etc) this task block represents or they can add  a new task block at this time
         */
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel,BoxLayout.Y_AXIS));
        JLabel labelAction = new JLabel("Select Action:");
        String[] actionsAllowed = {"Edit", "Delete", "Add"};
        JComboBox inputAction = new JComboBox(actionsAllowed);
        inputAction.setSelectedIndex(0);

        myPanel.add(labelAction);
        myPanel.add(inputAction);


        int enteredInfo = JOptionPane.showConfirmDialog(null, myPanel, "Selection Menu", JOptionPane.OK_CANCEL_OPTION);

        if(enteredInfo == JOptionPane.CLOSED_OPTION || enteredInfo == JOptionPane.CANCEL_OPTION) { return;}

        if (enteredInfo == JOptionPane.YES_OPTION) {
            String actionChosen = (String)inputAction.getSelectedItem();

            if(actionChosen == actionsAllowed[0]){
                CalendarGUI.editBlock(this);
            }
            else if(actionChosen == actionsAllowed[1]){
                CalendarGUI.deleteBlock(this);
            }
            else{
                CalendarGUI.addBlock(this.getStartTime());
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if(getItemType().equals(TaskEnum.STUDYBLOCK.getLabel())){
            JOptionPane.showMessageDialog(new JFrame(), "Cannot edit a study block.", "Error", JOptionPane.ERROR_MESSAGE);
        }
       else {
           PopUpMenu();
       }
    }
}