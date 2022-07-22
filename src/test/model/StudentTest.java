package model;

import model.exceptions.AlreadyExists;
import model.exceptions.TooLongDuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    Student student;
    Course course1;
    Course course2;
    HomeWork hwk1;
    HomeWork hwk2;
    HomeWork hwk3;

    @BeforeEach
    public void setup(){
        student = new Student("Jeff");
        course1 = new Course("cpsc");
        course2 = new Course("english");

        String hwk1Date = "24/03/2003";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate1 = LocalDate.parse(hwk1Date, formatter1);
        hwk1 = new HomeWork("hwk1", localDate1 , "cpsc", 3, 25);

        String hwk2Date = "24/03/2003";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate2 = LocalDate.parse(hwk2Date, formatter2);
        hwk2 = new HomeWork("hwk2", localDate2 , "english", 4, 50);

        String hwk3Date = "25/03/2003";
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate3 = LocalDate.parse(hwk3Date, formatter3);
        hwk3 = new HomeWork("hwk3", localDate3 , "english", 4, 50);
    }

    //Helper to visualise the schedule
    private void displaySchedule() {
        LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
        Set<LocalDate> localDates = schedule.keySet();
        for (LocalDate localDate : localDates) {
            List<HomeWork> list = schedule.get(localDate);
            String listString = "";
            for (HomeWork hwk : list) {
                listString = listString + " " + hwk.getName() + " (" + hwk.getCourse() + "), ";
            }
            System.out.println(localDate + " : " + listString);
        }
        System.out.println("\n");
    }

    @Test
    public void testingGettersToGet100Coverage(){
        try{
            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);

            //get List of Course
            student.addCourse(course1);
            student.addCourse(course2);
            List<Course> temp = new ArrayList<>();
            temp.add(course1);
            temp.add(course2);
            assertEquals(student.getListOfCourses(), temp);

            // check course list
            Course tempCourse = new Course("cpsc");
            assertFalse(student.checkCourseList(tempCourse));
            Course tempCourse2 = new Course("trial");
            assertTrue(student.checkCourseList(tempCourse2));

        }  catch (AlreadyExists e) {
            fail();
        }
    }

    @Test
    public void testingAddingCoursesToStudent(){
        try{
            student.addCourse(course1);
        } catch (AlreadyExists e) {
            fail();
        }
    }

    @Test
    public void testingAddingSameCourseName(){
        try{
            student.addCourse(course1);
            Course courseNew = new Course(course1.getCourseName());
            student.addCourse(courseNew);
            fail();
        } catch (AlreadyExists e) {

        }
    }

    @Test
    public void testingScheduleWithOneCourseOneHomeWork(){

        try {
            course1.addHomeWork(hwk1);
            student.addCourse(course1);
            student.scheduleMaker();


            LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
            assertEquals(schedule.size(),1);
            assertEquals(schedule.get(hwk1.getDeadline()).size(), 1);

        }
        catch (TooLongDuration e) {
            fail();
        } catch(AlreadyExists e){
            fail();
        }
    }

    @Test
    public void testingScheduleWithAllHwksNoConflicts(){
        try {
            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);
            student.addCourse(course1);
            student.addCourse(course2);


            student.scheduleMaker();
            LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
            assertEquals(schedule.size(),2);
            assertEquals(schedule.get(hwk1.getDeadline()).size(), 2);

        }
        catch (TooLongDuration e) {
            fail();
        } catch (AlreadyExists e) {
            fail();
        }
    }


    @Test
    public void testingMakingScheduleWithSameCourseName(){
        Course failure = new Course(course2.getCourseName());

        try {
            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);
            student.addCourse(course1);
            student.addCourse(course2);
            student.addCourse(failure);
            fail();

            student.scheduleMaker();
            LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
            assertEquals(schedule.size(),2);
            assertEquals(schedule.get(hwk1.getDeadline()).size(), 2);
            displaySchedule();
        }
        catch (TooLongDuration e) {
            fail();
        } catch (AlreadyExists e) {

        }
    }

    @Test
    public void testingScheduleWithDurationConflict(){
        //long homework
        String hwk4Date = "25/03/2003";
        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate4 = LocalDate.parse(hwk4Date, formatter4);
        HomeWork hwk4 = new HomeWork("hwk4", localDate4 , "english", 17, 50);

        try {
            course2.addHomeWork(hwk3);
            course2.addHomeWork(hwk4);
            student.addCourse(course2);
            student.scheduleMaker();
            fail();
        }
        catch (AlreadyExists e) {
            fail();
        }
        catch (TooLongDuration e) {

        }
    }


}
