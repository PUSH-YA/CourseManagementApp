package model;

import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;
import model.exceptions.TooLongDuration;

import java.time.LocalDate;
import java.util.*;

// Represents a student that can be enrolled in multiple courses and have a schedule for
// their homeworks from multiple courses
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

    //Getters
    public List<Course> getListOfCourses() {
        return listOfCourses;
    }

    public LinkedHashMap<LocalDate, List<HomeWork>> getSchedule() {
        return schedule;
    }

    //MODIFIES: listOfCourses
    //EFFECTS:  if the course with the same name does not exist then
    //          adds the course to the course list
    //          else throws AlreadyExists exception
    public void addCourse(Course course) throws AlreadyExists, NullCourseException {
        if (course == null) {
            throw new NullCourseException();
        }
        if (checkCourseList(course)) {
            listOfCourses.add(course);
        } else {
            throw new AlreadyExists();
        }

    }

    //EFFECTS:      Based on the listOfCourse:
    //              Takes each homework for each course and then passes it to the addHomeWorkToSchedule
    //              if the total duration for 1 date > 20 hours, cannot add it and throws tooLongDuration
    public void scheduleMaker() throws TooLongDuration {
        for (Course course : listOfCourses) {
            List<HomeWork> hwkList = course.getHomeworks();
            for (HomeWork hwk : hwkList) {
                try {
                    addHomeWorkToSchedule(hwk);
                } catch (TooLongDuration e) {
                    throw new TooLongDuration();
                }
            }
        }
    }


    //MODIFIES: schedule
    //EFFECTS: takes the input of the homework and adds it to the schedule
    //         if the schedule has the deadline of the homework, adds it to that deadline
    //         or creates a new deadline for the given homework
    //         if the homework increases the duration > 20, throws TooLongDuration
    public void addHomeWorkToSchedule(HomeWork hwk) throws TooLongDuration {
        Set<LocalDate> dates = schedule.keySet();
        if (dates.contains(hwk.getDate())) {
            List<HomeWork> homeworks = schedule.get(hwk.getDate());
            try {
                homeworks.add(hwk);
                schedule.put(hwk.getDate(), homeworks);
                isItTooLong(homeworks);
            } catch (TooLongDuration e) {
                throw new TooLongDuration();
            }
        } else {
            List<HomeWork> homeworks = new ArrayList<>();
            homeworks.add(hwk);
            schedule.put(hwk.getDate(), homeworks);
        }

    }


    //EFFECTS:      calculates the total duration of the homeworks in the list
    //             if total duration > 20 hours, throws TooLongDurationError exception
    private void isItTooLong(List<HomeWork> homeworks) throws TooLongDuration {
        int total = 0;
        for (HomeWork hwk : homeworks) {
            total += hwk.getDuration();
        }
        if (total > 20) {
            throw new TooLongDuration();
        }
    }

    //EFFECTS: returns false if the student is already registered in the
    //         course with the same name otherwise true
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
