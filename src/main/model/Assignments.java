package model;

import java.util.Date;

public class Assignments extends Workload {

    private int breaks; // how many times needed to do it

    public Assignments(int breaks, Date date, Course course, int duration, double weighing) {
        super(date, course, duration, weighing);
        this.breaks = breaks;
    }

    public void setWithBreaks() {

    }
}
