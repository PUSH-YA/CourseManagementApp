package persistence;

import model.Course;
import model.HomeWork;
import model.Student;

import model.exceptions.NullCourseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Student read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseStudent(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses student from JSON object and returns it
    private Student parseStudent(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Student std = new Student(name);
        addCourses(std, jsonObject);
        return std;
    }

    // MODIFIES: std
    // EFFECTS: parses courseList from JSON object and adds them to student
    private void addCourses(Student std, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("courseList");
        for (Object json : jsonArray) {
            JSONObject nextCourse = (JSONObject) json;
            addCourse(std, nextCourse);
        }
    }

    // MODIFIES: course
    // EFFECTS: parses course from JSON object and adds it to student
    private void addCourse(Student std, JSONObject jsonObject) {

        String name = jsonObject.getString("name");
        Course course = new Course(name);
        addHomeWorks(course, jsonObject);
        std.addCourseJson(course);
    }

    // MODIFIES: Course
    // EFFECTS: parses homeworks from JSON object and adds them to course, returns course
    private void addHomeWorks(Course course, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("homeworks");
        for (Object json : jsonArray) {
            JSONObject nextHwk = (JSONObject) json;
            addHomeWork(course, nextHwk);
        }
    }

    // MODIFIES: std
    // EFFECTS: parses homework from JSON object and adds it to the course, returns course
    private void addHomeWork(Course course, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String startDate = jsonObject.getString("start date");
        String courseName = jsonObject.getString("course");
        Double grade = jsonObject.getDouble("grade");
        int duration = jsonObject.getInt("duration");
        double weighing = jsonObject.getDouble("weighing");
        boolean done = jsonObject.getBoolean("done?");

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(startDate, formatter1);

        HomeWork hwk = new HomeWork(name, localDate, courseName, duration, weighing);

        hwk.setGrade(grade);

        if (done) {
            hwk.changeStatus();
        }
        course.addHomeWorkJson(hwk);
    }
}

