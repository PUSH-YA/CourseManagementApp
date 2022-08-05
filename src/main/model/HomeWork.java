package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//represents a homework for a specific course that needs to be done and carries a % weight of the grade for the course
public class HomeWork implements Writable {
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

    //Getters
    public LocalDate getDate() {
        return startDate;
    }

    public String getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public double getWeighing() {
        return weighing;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    //EFFECTS: returns the status of the homework
    public boolean getStatus() {
        return done;
    }

    //MODIFIES: done
    //EFFECTS: change the status of the code from the previous state
    public void changeStatus(boolean status) {
        done = status;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        json.put("start date", startDate.format(formatter1));
        json.put("course", course);
        json.put("grade", grade);
        json.put("duration", duration);
        json.put("grade", grade);
        json.put("weighing", weighing);
        json.put("done?", done);
        return json;
    }
}
