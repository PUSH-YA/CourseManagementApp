package model;

import model.exceptions.AlreadyExists;
import model.exceptions.TooLongDuration;

import java.time.LocalDate;
import java.util.*;

public class Student {

    private String name;
    private List<Course> listOfCourses;
    private LinkedHashMap<LocalDate, List<HomeWork>> schedule;

    //EFFECTS: creates a student with name and empty course and schedule
    public Student(String name) {
        this.name = name;
        listOfCourses = new ArrayList<>();
        schedule = new LinkedHashMap<LocalDate, List<HomeWork>>();
    }

    //MODIFIES: listOfCourses
    //EFFECTS: adds the course to the course list
    public void addCourse(Course course) throws AlreadyExists {
        if (checkCourseList(course)) {
            listOfCourses.add(course);
        } else {
            throw new AlreadyExists();
        }

    }

    //EFFECT: returns list of courses for the student
    public List<Course> getListOfCourses() {
        return listOfCourses;
    }

    //EFFECTS:      Based on the listOfCourse:
    //              For each start date, creates a list of assignments/hwks to do for the courses
    //              if the total duration for a day passes 24 hours, cannot add it and returns false
    public void scheduleMaker() throws TooLongDuration {
        for (Course course : listOfCourses) {
            List<HomeWork> hwkList = course.getHomeworks();
            for (HomeWork hwk : hwkList) {
                try {
                    addToSchedule(hwk);
                } catch (TooLongDuration e) {
                    throw new TooLongDuration();
                }
            }
        }
    }


    //EFFECTS: takes the input of the homework and adds it to the schedule
    //          if the homework increases the duration > 20, throws TooLongDuration
    public void addToSchedule(HomeWork hwk) throws TooLongDuration {
        Set<LocalDate> dates = schedule.keySet();
        if (dates.contains(hwk.getDeadline())) {
            List<HomeWork> homeworks = schedule.get(hwk.getDeadline());
            try {
                homeworks.add(hwk);
                schedule.put(hwk.getDeadline(), homeworks);
                isItTooLong(homeworks);
            } catch (TooLongDuration e) {
                throw new TooLongDuration();
            }
        } else {
            List<HomeWork> homeworks = new ArrayList<>();
            homeworks.add(hwk);
            schedule.put(hwk.getDeadline(), homeworks);
        }

    }

    //MODIFIES: schedule
    //EFFECTS: throws TooLongDurationError if the homework list has more than 20 hours of work
    private void isItTooLong(List<HomeWork> homeworks) throws TooLongDuration {
        int total = 0;
        for (HomeWork hwk : homeworks) {
            total += hwk.getDuration();
        }
        if (total > 20) {
            throw new TooLongDuration();
        }
    }

    //EFFECTS: returns the schedule
    public LinkedHashMap<LocalDate, List<HomeWork>> getSchedule() {
        return schedule;
    }

    //EFFECTS: returns false if the student is already registered in the course otherwise true
    public boolean checkCourseList(Course c) {
        String courseName = c.getCourseName().toLowerCase();
        for (Course course :  listOfCourses) {
            String currName = course.getCourseName().toLowerCase();
            if (currName.equals(courseName)) {
                return false;
            }
        }
        return true;
    }
}
