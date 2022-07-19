package model;

import java.util.Date;

public abstract class Workload {
    private Date deadline;
    private Course course;
    private int duration;
    private double weighing;
    private boolean done;

    public Workload(Date date, Course course, int duration, double weighing) {
        this.deadline = date;
        this.course = course;
        this.duration = duration;
        this.weighing = weighing;
        done = false;
    }

    public void setNewDeadline(Date date) {
        this.deadline = date;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setNewCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

}
