package model;

import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;
import model.exceptions.TooLongDuration;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.util.*;

// Represents a student that can be enrolled in multiple courses and have a schedule for
// their homeworks from multiple courses
public class Student implements Writable {

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

    public String getName() {
        return name;
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

    //MODIFIES: listOfCourses
    //EFFECTS: adds the course directly to the student without checking for null course exception
    public void addCourseJson(Course course) {
        listOfCourses.add(course);
    }

    //EFFECTS:      Based on the listOfCourse:
    //              Takes each homework for each course and then passes it to the addHomeWorkToSchedule
    //              if the total duration for 1 date > 20 hours, cannot add it and throws tooLongDuration
    public void scheduleMaker() throws TooLongDuration {
        schedule.clear();
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
            int totalDuration = hwk.getDuration() + isItTooLong(homeworks);
            if (totalDuration <= 20) {
                homeworks.add(hwk);
            } else {
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
    private int isItTooLong(List<HomeWork> homeworks) {
        int total = 0;
        for (HomeWork hwk : homeworks) {
            total += hwk.getDuration();
        }
        return total;
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("courseList", courseListToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray courseListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Course c : listOfCourses) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }
}
