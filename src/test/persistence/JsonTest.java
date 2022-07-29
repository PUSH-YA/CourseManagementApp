package persistence;

import model.Course;
import model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkStudent(String name, Student std, Course course) {
        assertEquals(name, std.getName());
        assertEquals(course, course.getHomeworks());
    }
}
