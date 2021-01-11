package ca.sfu.cmpt275.calendarapp.calendar;
public enum TaskEnum {
    QUIZ("Quiz"), ASSIGNMENT("Assignment"), OTHER("Other"), LECTURE("Lecture"),
    STUDYBLOCK("StudyBlock"), PERSONAL("Personal");
    private String label;

    private TaskEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}