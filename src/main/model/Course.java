package model;

import model.exceptions.AlreadyExists;
import model.exceptions.NullHomeWorkException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

//represents a course that the student might be enrolled in with its own homeworks to do and a grade
public class Course implements Writable {

    private List<HomeWork> homeworks;
    private String courseName;
    private double courseGrade;

    //EFFECTS: creates a new course with the name,homework and assignments
    public Course(String name) {
        courseName = name;
        homeworks = new ArrayList<>();
        courseGrade = 0;
    }

    //Getters
    public String getCourseName() {
        return courseName;
    }

    public List<HomeWork> getHomeworks() {
        return homeworks;
    }

    public double getGrade() {
        calculateGrade();
        return courseGrade;
    }

    //MODIFIES: homeworks
    //EFFECTS: if homework does not exist already
    //         adds 1 homework to the homeworks list
    //         else throws AlreadyExists exception
    public void addHomeWork(HomeWork hwk) throws AlreadyExists, NullHomeWorkException {
        if (hwk == null) {
            throw new NullHomeWorkException();
        } else if (checkHwkList(hwk)) {
            homeworks.add(hwk);
        } else {
            throw new AlreadyExists();
        }
    }

    //MODIFIES: homeworks
    //EFFECTS: adds homework to homeworks for JSON object
    public void addHomeWorkJson(HomeWork hwk) {
        homeworks.add(hwk);
    }

    //MODIFIES: courseGrade
    //EFFECTS: calculates the weighted grades based on the homework and changes the courseGrade accordingly
    //          if there are no homeworks, returns 0
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", courseName);
        json.put("homeworks", homeworksToJson());
        return json;
    }

    //EFFECTS: returns things in this course as JSONArray
    private JSONArray homeworksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (HomeWork hwk : homeworks) {
            jsonArray.put(hwk.toJson());
        }
        return jsonArray;
    }
}
