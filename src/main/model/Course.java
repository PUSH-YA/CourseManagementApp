package model;

import java.util.List;

public class Course {

    private List<Assignments> assignments;
    private List<HomeWork> homeworks;

    public void addHomeWork(HomeWork hwk) {
        homeworks.add(hwk);
    }

    public List<HomeWork> getHomeworks() {
        return homeworks;
    }

    public void addAssignment(Assignments asgm) {
        assignments.add(asgm);
    }

    public List<Assignments> getAssignments() {
        return assignments;
    }
}
