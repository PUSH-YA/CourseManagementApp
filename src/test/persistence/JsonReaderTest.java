package persistence;

import model.Course;
import model.Student;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Student std = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyStudent.json");
        try {
            Student std = reader.read();
            assertEquals("Jeff", std.getName());
            assertEquals(0, std.getListOfCourses().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralStudent() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralStudent.json");
        try {
            Student std = reader.read();
            assertEquals("Jeff", std.getName());
            List<Course> courses = std.getListOfCourses();
            assertEquals(2, courses.size());
            assertEquals(courses.get(0).getCourseName(), "english");
            assertEquals(courses.get(1).getCourseName(), "comp sci");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testReaderStudentWithHomeWork(){
        JsonReader reader = new JsonReader("./data/testReaderStudentWithHomeWork.json");
        try {
            Student std = reader.read();
            assertEquals("Jeff", std.getName());
            List<Course> courseList = std.getListOfCourses();
            assertEquals(2, courseList.size());

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
            LocalDate localDate1 = LocalDate.parse("24/03/2022", formatter1);

            assertEquals(courseList.get(0).getHomeworks().size(), 1);
            assertEquals(courseList.get(0).getCourseName(), "english");
            assertEquals(courseList.get(0).getHomeworks().get(0).getName(), "hwk");
            assertEquals(courseList.get(0).getHomeworks().get(0).getGrade(), 10);
            assertEquals(courseList.get(0).getHomeworks().get(0).getDuration(), 3);
            assertEquals(courseList.get(0).getHomeworks().get(0).getWeighing(), 30);
            assertFalse(courseList.get(0).getHomeworks().get(0).getStatus());
            assertEquals(courseList.get(0).getHomeworks().get(0).getDate(), localDate1);

            assertEquals(courseList.get(1).getHomeworks().size(), 1);
            assertEquals(courseList.get(1).getCourseName(), "comp sci");
            assertEquals(courseList.get(1).getHomeworks().get(0).getName(), "hwk2");
            assertEquals(courseList.get(1).getHomeworks().get(0).getGrade(), 0);
            assertEquals(courseList.get(1).getHomeworks().get(0).getDuration(), 3);
            assertEquals(courseList.get(1).getHomeworks().get(0).getWeighing(), 30);
            assertTrue(courseList.get(1).getHomeworks().get(0).getStatus());
            assertEquals(courseList.get(1).getHomeworks().get(0).getDate(), localDate1);
        } catch(IOException e) {
            fail();
        }

    }


}
