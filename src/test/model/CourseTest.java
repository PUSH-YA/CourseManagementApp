package model;

import model.exceptions.AlreadyExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CourseTest {

    Course course1;
    Course course2;
    HomeWork hwk1;
    HomeWork hwk2;
    HomeWork hwk3;


    @BeforeEach
    public void setup(){
        course1 = new Course("cpsc");
        course2 = new Course("english");

        String hwk1Date = "24/03/2003";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate1 = LocalDate.parse(hwk1Date, formatter1);
        hwk1 = new HomeWork("hwk1", localDate1 , "cpsc", 3, 30.0);

        String hwk2Date = "24/03/2003";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate2 = LocalDate.parse(hwk2Date, formatter2);
        hwk2 = new HomeWork("hwk1", localDate2 , "cpsc", 4, 50.0);

        String hwk3Date = "25/03/2003";
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate3 = LocalDate.parse(hwk3Date, formatter3);
        hwk3 = new HomeWork("hwk3", localDate3 , "cpsc", 2, 49.0);
    }


    @Test
    public void testGettters(){
        // Course name
        assertEquals(course1.getCourseName(), "cpsc");
        assertEquals(course2.getCourseName(), "english");

        //HomeWorks
        try {
            course1.addHomeWork(hwk1);
            course1.addHomeWork(hwk3);
        } catch (AlreadyExists e) {
            fail();
        }
        List<HomeWork> hwkList = new ArrayList<>();
        hwkList.add(hwk1);
        hwkList.add(hwk3);
        assertEquals(course1.getHomeworks(), hwkList);

        //Grade
        assertEquals(course1.getGrade(), 0);
    }

    @Test
    public void testSameNameHomeWork(){
        try {
            course1.addHomeWork(hwk1);
            course1.addHomeWork(hwk2);
            fail();
        } catch(AlreadyExists e) {

        }
    }

    @Test
    public void testCalculatingGrade() {
        try {
            course1.addHomeWork(hwk1);
            course1.addHomeWork(hwk3);

            hwk1.setGrade(30);
            hwk3.setGrade(67);

            double grade = ((30.0*30.0)+(49.0*67.0))/(30.0+49.0);
            assertEquals(course1.getGrade(), grade);
        } catch(AlreadyExists e) {

        }
    }

}
