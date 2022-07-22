package model;

import java.time.LocalDate;
import java.util.Date;

public class HomeWork {
    private String name;
    private LocalDate startDate;
    private String course;
    private int duration;
    private double grade;
    private double weighing;
    private boolean done;

    //EFFECTS: creates a workload with the given fields
    public HomeWork(String name, LocalDate startDate, String course, int duration, double weighing) {
        this.name = name;
        this.startDate = startDate;
        this.course = course;
        this.duration = duration;
        this.weighing = weighing;
        grade = 0;
        done = false;
    }

    //EFFECTS: returns the startDate
    public LocalDate getDeadline() {
        return startDate;
    }

    //EFFECTS: returns the courseName of the workload
    public String getCourse() {
        return course;
    }

    //EFFECTS: returns the name of the homework
    public String getName() {
        return name;
    }

    //EFFECTS: returns the duration of the homework
    public int getDuration() {
        return duration;
    }

    //EFFECTS: returns the current weighing of the homework
    public double getWeighing() {
        return weighing;
    }

    //EFFECTS: returns the grade of the homework
    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    //EFFECTS: returns the status of the homework as a string
    public String getStatus() {
        if (done) {
            return "done";
        } else {
            return "incomplete";
        }
    }

    //EFFECTS: change the status of the code from the previous state
    public void changeStatus() {
        done = !done;
    }

}
