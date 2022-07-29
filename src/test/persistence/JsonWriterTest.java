package persistence;

import model.Course;
import model.HomeWork;
import model.Student;
import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;
import model.exceptions.NullHomeWorkException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    private Student std;
    private HomeWork hwk1;
    private HomeWork hwk2;

    @BeforeEach
    void setup() {
        std = new Student("Jeff");

        String hwk1Date = "24/03/2003";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate1 = LocalDate.parse(hwk1Date, formatter1);
        hwk1 = new HomeWork("hwk", localDate1 , "cpsc", 3, 30.0);

        String hwk2Date = "24/03/2003";
        LocalDate localDate2 = LocalDate.parse(hwk2Date, formatter1);
        hwk2 = new HomeWork("hwk2", localDate2 , "cpsc", 3, 30.0);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyStudent() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyStudent.json");
            writer.open();
            writer.write(std);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyStudent.json");
            std = reader.read();
            assertEquals("Jeff", std.getName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStudent() {
        try {
            std.addCourse(new Course("english"));
            std.addCourse(new Course("comp sci"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralStudent.json");
            writer.open();
            writer.write(std);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralStudent.json");
            std = reader.read();
            assertEquals("Jeff", std.getName());
            List<Course> courseList = std.getListOfCourses();
            assertEquals(2, courseList.size());

            assertEquals(courseList.get(0).getCourseName(), "english");
            assertEquals(courseList.get(1).getCourseName(), "comp sci");

        } catch (IOException e) {
            fail();
        } catch (AlreadyExists e) {
            fail();
        } catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    void testWriterStudentWithHomeWork() {
        try {
            Course course1 = new Course("english");
            Course course2 = new Course("comp sci");
            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            std.addCourse(course1);
            std.addCourse(course2);

            JsonWriter writer = new JsonWriter("./data/testWriterStudentWithHomeWork.json");
            writer.open();
            writer.write(std);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterStudentWithHomeWork.json");
            std = reader.read();
            assertEquals("Jeff", std.getName());
            List<Course> courseList = std.getListOfCourses();
            assertEquals(2, courseList.size());

            assertEquals(courseList.get(0).getHomeworks().size(), 1 );
            assertEquals(courseList.get(0).getCourseName(), "english");
            assertEquals(courseList.get(0).getHomeworks().get(0).getName(), "hwk");

            assertEquals(courseList.get(1).getHomeworks().size(), 1 );
            assertEquals(courseList.get(1).getCourseName(), "comp sci");
            assertEquals(courseList.get(1).getHomeworks().get(0).getName(), "hwk2");

        } catch (IOException e) {
            fail();
        } catch (AlreadyExists e) {
            fail();
        } catch (NullCourseException e) {
            fail();
        } catch (NullHomeWorkException e) {
            fail();
        }
    }
}

