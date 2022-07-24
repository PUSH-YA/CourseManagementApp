package model;

import model.exceptions.AlreadyExists;

import java.util.ArrayList;
import java.util.List;

//represents a course that the student might be enrolled in with its own homeworks to do and a grade
public class Course {

    private List<HomeWork> homeworks;
    private String courseName;
    private double courseGrade;

    //EFFECTS: creates a new course with the name,homework and assignments
    public Course(String name) {
        courseName = name;
        homeworks = new ArrayList<>();
        courseGrade = 0;
    }

    //EFFECTS: returns the course name
    public String getCourseName() {
        return courseName;
    }

    //MODIFIES: homeworks
    //EFFECTS: adds 1 homework to the homeworks list
    public void addHomeWork(HomeWork hwk) throws AlreadyExists {
        if (checkHwkList(hwk)) {
            homeworks.add(hwk);
        } else {
            throw new AlreadyExists();
        }
    }

    //EFFECTS: return the homeworks list
    public List<HomeWork> getHomeworks() {
        return homeworks;
    }

    //MODIFIES: grade
    //EFFECTS: calculates the weighted grades based on the homework
    private void calculateGrade() {
        double totalWeighing = 0;
        double grade = 0;
        if (!homeworks.isEmpty()) {
            for (HomeWork hwk : homeworks) {
                totalWeighing += hwk.getWeighing();
                grade += (hwk.getGrade() * hwk.getWeighing());
            }
            courseGrade = grade / totalWeighing;
        } else {
            courseGrade = 0;
        }
    }

    //EFFECTS: returns the current grade for the course
    public double getGrade() {
        calculateGrade();
        return courseGrade;
    }

    //EFFECTS: returns false if the homework with the same name already exists in the homework list, otherwise true
    public boolean checkHwkList(HomeWork hwk) {
        String hwkName = hwk.getName();
        hwkName = hwkName.toLowerCase();
        for (HomeWork course :  homeworks) {
            String currName = course.getName().toLowerCase();
            if (currName.equals(hwkName)) {
                return false;
            }
        }
        return true;
    }
}
