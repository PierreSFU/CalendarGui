package ca.sfu.cmpt275.calendarapp.calendar;

import java.awt.*;

/*
References: [4], [5], [6], [7], [8], [9], [10], [11], [12], [13], [14], [15], [16], [17], [18], [19],
[20], [21], [22], [23], [24], [25], [26], [27], [28], [29], [30], [31], [32], [33], [34], [35], [36],
[37], [38], [39], [40], [41], [42], [43], [44], [45], [46], [47], [48], [49], [50], [51], [52], [53],
[54], [55], [56], [57], [58] , [69], [70], [71], [72], [73], [74], [75], [76]
 */

public enum ColourList {
    LIGHT_PINK(java.awt.Color.getHSBColor(0.9f,0.4f,0.9f), "Light Pink"),
    PINK(java.awt.Color.getHSBColor(0.9f,0.4f,0.8f), "Pink"),
    DARK_PINK(java.awt.Color.getHSBColor(0.9f,0.4f,0.65f), "Dark Pink"),
    DARKER_PINK(java.awt.Color.getHSBColor(0.9f,0.4f,0.5f), "Darker Pink"),
    BORDER_PINK(java.awt.Color.getHSBColor(0.9f,0.4f,0.45f), "Border Pink"),
    LIGHT_BLUE(java.awt.Color.getHSBColor(0.5f,0.6f,0.9f), "Light Blue"),
    BLUE(java.awt.Color.getHSBColor(0.5f,0.6f,0.8f), "Blue"),
    DARK_BLUE(java.awt.Color.getHSBColor(0.5f,0.6f,0.65f), "Dark Blue"),
    DARKER_BLUE(java.awt.Color.getHSBColor(0.5f,0.6f,0.5f), "Darker Blue"),
    BORDER_BLUE(java.awt.Color.getHSBColor(0.5f,0.6f,0.45f), "Border Blue"),
    LIGHT_ORANGE(java.awt.Color.getHSBColor(0.1f,0.4f,0.9f), "Light Orange"),
    ORANGE(java.awt.Color.getHSBColor(0.1f,0.4f,1f), "Orange"),
    DARK_ORANGE(java.awt.Color.getHSBColor(0.1f,0.4f,0.65f), "Dark Orange"),
    DARKER_ORANGE(java.awt.Color.getHSBColor(0.1f,0.4f,0.5f), "Darker Orange"),
    BORDER_ORANGE(java.awt.Color.getHSBColor(0.1f,0.4f,0.45f), "Border Orange"),
    LIGHT_GREEN(java.awt.Color.getHSBColor(0.3f,0.5f,0.9f), "Light Green"),
    GREEN(java.awt.Color.getHSBColor(0.3f,0.5f,0.7f), "Green"),
    DARK_GREEN(java.awt.Color.getHSBColor(0.3f,0.5f,0.65f), "Dark Green"),
    DARKER_GREEN(java.awt.Color.getHSBColor(0.3f,0.5f,0.5f), "Darker Green"),
    BORDER_GREEN(java.awt.Color.getHSBColor(0.3f,0.5f,0.45f), "Border Green"),
    LIGHT_PURPLE(java.awt.Color.getHSBColor(0.9f,0.1f,0.9f), "Light Purple"),
    PURPLE(java.awt.Color.getHSBColor(0.9f,0.1f,0.7f), "Purple"),
    DARK_PURPLE(java.awt.Color.getHSBColor(0.9f,0.1f,0.65f), "Dark Purple"),
    DARKER_PURPLE(java.awt.Color.getHSBColor(0.9f,0.1f,0.5f), "Darker Purple"),
    BORDER_PURPLE(java.awt.Color.getHSBColor(0.9f,0.1f,0.45f), "Border Purple");


    private final Color colour;
    private final String nameColour;

    private ColourList(Color c, String n){
        this.colour = c;
        this.nameColour = n;
    }

    public Color getColour(){ return this.colour;}
    public String getName(){ return this.nameColour;}

    static public Color getColorByName(String nameToFind){
        ColourList[] colours = {ColourList.PINK, ColourList.BLUE, ColourList.ORANGE, ColourList.GREEN, ColourList.PURPLE};
        for(int i = 0; i < colours.length; i++){
            if(colours[i].getName().equals(nameToFind)){
                return colours[i].getColour();
            }
        }
        return Color.GRAY; //Default to grey
    }

    static public String getColourObject(Color colourObj){
        ColourList[] colours = {ColourList.PINK, ColourList.BLUE, ColourList.ORANGE, ColourList.GREEN, ColourList.PURPLE};
        for(int i = 0; i < colours.length; i++){
            if(colours[i].getColour().equals(colourObj)){
                return colours[i].getName();
            }
        }
        return colours[0].getName(); // Default to pink
    }

    static public Color getLightestColour(Color colourObj){
        String colourName = getColourObject(colourObj);
        if(colourName.equals(ColourList.PINK.getName())){
            return ColourList.LIGHT_PINK.getColour();
        }
        else if(colourName.equals(ColourList.BLUE.getName())){
            return ColourList.LIGHT_BLUE.getColour();
        }
        else if(colourName.equals(ColourList.ORANGE.getName())){
            return ColourList.LIGHT_ORANGE.getColour();
        }
        else if(colourName.equals(ColourList.GREEN.getName())){
            return ColourList.LIGHT_GREEN.getColour();
        }
        else{
            return ColourList.LIGHT_PURPLE.getColour();
        }
    }

    static public Color getDarkerColour(Color colourObj){
        String colourName = getColourObject(colourObj);
        if(colourName.equals(ColourList.PINK.getName())){
            return ColourList.DARK_PINK.getColour();
        }
        else if(colourName.equals(ColourList.BLUE.getName())){
            return ColourList.DARK_BLUE.getColour();
        }
        else if(colourName.equals(ColourList.ORANGE.getName())){
            return ColourList.DARK_ORANGE.getColour();
        }
        else if(colourName.equals(ColourList.GREEN.getName())){
            return ColourList.DARK_GREEN.getColour();
        }
        else{
            return ColourList.DARK_PURPLE.getColour();
        }
    }

    static public Color getDarkestColour(Color colourObj){
        String colourName = getColourObject(colourObj);
        if(colourName.equals(ColourList.PINK.getName())){
            return ColourList.DARKER_PINK.getColour();
        }
        else if(colourName.equals(ColourList.BLUE.getName())){
            return ColourList.DARKER_BLUE.getColour();
        }
        else if(colourName.equals(ColourList.ORANGE.getName())){
            return ColourList.DARKER_ORANGE.getColour();
        }
        else if(colourName.equals(ColourList.GREEN.getName())){
            return ColourList.DARKER_GREEN.getColour();
        }
        else{
            return ColourList.DARKER_PURPLE.getColour();
        }
    }

    static public Color getBorderColour(Color colourObj){
        String colourName = getColourObject(colourObj);
        if(colourName.contains(ColourList.PINK.getName())){
            return ColourList.BORDER_PINK.getColour();
        }
        else if(colourName.contains(ColourList.BLUE.getName())){
            return ColourList.BORDER_BLUE.getColour();
        }
        else if(colourName.contains(ColourList.ORANGE.getName())){
            return ColourList.BORDER_ORANGE.getColour();
        }
        else if(colourName.contains(ColourList.GREEN.getName())){
            return ColourList.BORDER_GREEN.getColour();
        }
        else{
            return ColourList.BORDER_PURPLE.getColour();
        }
    }

}
