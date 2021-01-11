package ca.sfu.cmpt275.calendarapp.calendar;

import javax.swing.*;
import java.awt.*;

/*
References: [4], [5], [6], [7], [8], [9], [10], [11], [12], [13], [14], [15], [16], [17], [18], [19],
[20], [21], [22], [23], [24], [25], [26], [27], [28], [29], [30], [31], [32], [33], [34], [35], [36],
[37], [38], [39], [40], [41], [42], [43], [44], [45], [46], [47], [48], [49], [50], [51], [52], [53],
[54], [55], [56], [57], [58] , [69], [70], [71], [72], [73], [74], [75], [76]
 */

public class GeneralInfoPanel extends JPanel {
    private Graphics2D g2D;
    private JLayeredPane parentPanel;
    private int panelWidth;
    private int individualPanelHeight;
    private int mainPanelHeight;
    private int xoffset;
    private int yoffset;
    private final int numCalendarTimes = 12;


    public GeneralInfoPanel(int w, int h, int h2, JLayeredPane parentPanel, int xLocation){
        this.parentPanel = parentPanel;
        this.panelWidth = w;
        this.individualPanelHeight = h;
        this.mainPanelHeight = h2;
        this.xoffset = xLocation;
        this.yoffset = 26;

        this.setBounds(this.xoffset,0,this.panelWidth,this.mainPanelHeight);
        this.setLayout(null);

        addHourLabels();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g;

        g2D.setPaint(Color.gray);
        g2D.setStroke(new BasicStroke(1.0f));

        for(int i = 0; i < this.numCalendarTimes; i++ ){
            g2D.drawLine(0,  i*this.individualPanelHeight + this.yoffset, this.panelWidth,  i*this.individualPanelHeight + this.yoffset);
        }

        g2D.setStroke(new BasicStroke(5.0f));
        g2D.setPaint(Color.black);
        g2D.drawLine(this.getWidth(), 0, this.getWidth(), 12*this.individualPanelHeight);
    }

    public void addHourLabels(){
        JLabel hourLabel = new JLabel("Hour");
        hourLabel.setBounds(20, this.yoffset/2 - 4, 60,10);
        this.add(hourLabel);

        JLabel[] hourTicks = new JLabel[this.numCalendarTimes];
        for(int i = 0; i < this.numCalendarTimes-1; i++ ){
            hourTicks[i] = new JLabel((i+8) +" to " + (i+9));
            hourTicks[i].setBounds(20,i*this.individualPanelHeight + this.yoffset + this.individualPanelHeight/2, 60, 10);
            this.add(hourTicks[i]);
        }

    }

}
