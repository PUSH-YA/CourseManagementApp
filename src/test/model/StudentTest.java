package model;

import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;
import model.exceptions.NullHomeWorkException;
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
    Student another;
    Course course1;
    Course course2;
    HomeWork hwk1;
    HomeWork hwk2;
    HomeWork hwk3;
    HomeWork hwk4;

    @BeforeEach
    public void setup(){
        student = new Student("Jeff");
        another = new Student("no one cares about");
        course1 = new Course("cpsc");
        course2 = new Course("english");

        String hwk1Date = "24/03/2003";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate1 = LocalDate.parse(hwk1Date, formatter1);
        hwk1 = new HomeWork("hwk1", localDate1 , "cpsc", 3, 25);

        String hwk2Date = "24/03/2003";
        LocalDate localDate2 = LocalDate.parse(hwk2Date, formatter1);
        hwk2 = new HomeWork("hwk2", localDate2 , "english", 4, 50);

        String hwk3Date = "25/03/2003";
        LocalDate localDate3 = LocalDate.parse(hwk3Date, formatter1);
        hwk3 = new HomeWork("hwk3", localDate3 , "english", 4, 50);

        String hwk4Date = "25/03/2003";
        LocalDate localDate4 = LocalDate.parse(hwk4Date, formatter1);
        hwk4 = new HomeWork("hwk4", localDate4 , "english", 17, 50);
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
    public void testingGetters(){
        try{
            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);

            //get name
            assertEquals(student.getName(), "Jeff");

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
        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testNullCourseExceptions(){
        try{
            student.addCourse(null);
        } catch (AlreadyExists e) {
            fail();
        } catch (NullCourseException e){

        }

    }
    @Test
    public void testingAddingCoursesToStudent(){
        try{
            student.addCourse(course1);
            student.addCourse(course2);
            assertEquals(student.getListOfCourses().size(), 2);
            assertTrue(student.getListOfCourses().contains(course1));
            assertTrue(student.getListOfCourses().contains(course2));

            assertEquals(another.getListOfCourses().size(), 0);
            assertFalse(another.getListOfCourses().contains(course1));
            assertFalse(another.getListOfCourses().contains(course2));
        } catch (AlreadyExists e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingAddingSameCourseName(){
        Course courseNew = new Course(course1.getCourseName());
        try{
            //control test
            another.addCourse(courseNew);
            student.addCourse(course1);
            student.addCourse(courseNew);
            fail();

        } catch (AlreadyExists e) {

        }  catch (NullCourseException e) {
            fail();
        }
        assertEquals(student.getListOfCourses().size(), 1);
        assertEquals(student.getListOfCourses().get(0), course1);

        assertEquals(another.getListOfCourses().size(), 1);
        assertTrue(another.getListOfCourses().contains(courseNew));
        assertFalse(another.getListOfCourses().contains(course1));
    }

    @Test
    public void testingScheduleWithOneCourseOneHomeWork(){

        try {
            course1.addHomeWork(hwk1);
            student.addCourse(course1);
            student.scheduleMaker();

            course2.addHomeWork(hwk2);
            another.addCourse(course2);
            another.scheduleMaker();

            LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
            assertEquals(schedule.size(),1);
            assertEquals(schedule.get(hwk1.getDate()).size(), 1);
            assertEquals(schedule.get(hwk1.getDate()).get(0), hwk1);

            LinkedHashMap<LocalDate, List<HomeWork>> scheduleAnother = another.getSchedule();
            assertEquals(scheduleAnother.size(),1);
            assertEquals(scheduleAnother.get(hwk2.getDate()).size(), 1);
            assertEquals(scheduleAnother.get(hwk2.getDate()).get(0), hwk2);

        }
        catch (TooLongDuration e) {
            fail();
        } catch(AlreadyExists e){
            fail();
        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
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
            assertEquals(schedule.get(hwk1.getDate()).size(), 2);
            assertTrue(schedule.get(hwk1.getDate()).contains(hwk1));
            assertTrue(schedule.get(hwk2.getDate()).contains(hwk2));
            assertFalse(schedule.get(hwk3.getDate()).contains(hwk1));
            assertFalse(schedule.get(hwk3.getDate()).contains(hwk2));
            assertTrue(schedule.get(hwk3.getDate()).contains(hwk3));
        }
        catch (TooLongDuration e) {
            fail();
        } catch (AlreadyExists e) {
            fail();
        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingSameNameHomeWorkScheduleConflict(){
        try {
            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);
            student.addCourse(course1);
            student.addCourse(course2);
            student.scheduleMaker();

            another.addCourse(course1);
            another.addCourse(course2);
            another.scheduleMaker();

            String hwk1Date = "24/03/2003";
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
            LocalDate localDate1 = LocalDate.parse(hwk1Date, formatter1);
            HomeWork hwkTemp = new HomeWork("hwk1", localDate1 , "cpsc", 3, 25);
            course1.addHomeWork(hwkTemp);
            student.scheduleMaker();
            fail();


        }
        catch (TooLongDuration e) {
            fail();
        } catch (AlreadyExists e) {

        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }

        LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
        assertEquals(schedule.size(),2);
        assertEquals(schedule.get(hwk1.getDate()).size(), 2);
        assertTrue(schedule.get(hwk1.getDate()).contains(hwk1));
        assertTrue(schedule.get(hwk2.getDate()).contains(hwk2));
        assertFalse(schedule.get(hwk3.getDate()).contains(hwk1));
        assertFalse(schedule.get(hwk3.getDate()).contains(hwk2));
        assertTrue(schedule.get(hwk3.getDate()).contains(hwk3));

        LinkedHashMap<LocalDate, List<HomeWork>> scheduleAnother = another.getSchedule();
        assertEquals(scheduleAnother.size(),2);
        assertEquals(scheduleAnother.get(hwk1.getDate()).size(), 2);
        assertTrue(scheduleAnother.get(hwk1.getDate()).contains(hwk1));
        assertTrue(scheduleAnother.get(hwk2.getDate()).contains(hwk2));
        assertFalse(scheduleAnother.get(hwk3.getDate()).contains(hwk1));
        assertFalse(scheduleAnother.get(hwk3.getDate()).contains(hwk2));
        assertTrue(scheduleAnother.get(hwk3.getDate()).contains(hwk3));

    }

    @Test
    public void testingMakingScheduleAfterSameNameCourse(){
        Course failure = new Course(course2.getCourseName());

        try {
            failure.addHomeWork(hwk1);

            another.addCourse(failure);
            another.scheduleMaker();

            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);
            student.addCourse(course1);
            student.addCourse(course2);
            student.scheduleMaker();
            student.addCourse(failure);
            student.scheduleMaker();
            fail();

        }
        catch (TooLongDuration e) {
            fail();
        } catch (AlreadyExists e) {

        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }

        LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
        assertEquals(schedule.size(),2);
        assertEquals(schedule.get(hwk1.getDate()).size(), 2);
        assertTrue(schedule.get(hwk1.getDate()).contains(hwk2));
        assertFalse(schedule.get(hwk1.getDate()).contains(hwk3));
        assertTrue(schedule.get(hwk3.getDate()).contains(hwk3));


        LinkedHashMap<LocalDate, List<HomeWork>> scheduleAnother = another.getSchedule();
        assertEquals(scheduleAnother.size(),1);
        assertEquals(scheduleAnother.get(hwk1.getDate()).size(), 1);
        assertTrue(scheduleAnother.get(hwk1.getDate()).contains(hwk1));
        assertFalse(scheduleAnother.get(hwk1.getDate()).contains(hwk3));
    }


    @Test
    public void testingMakingScheduleBeforeSameNameCourse(){
        Course failure = new Course(course2.getCourseName());

        try {
            failure.addHomeWork(hwk1);

            another.addCourse(failure);
            another.scheduleMaker();

            course1.addHomeWork(hwk1);
            course2.addHomeWork(hwk2);
            course2.addHomeWork(hwk3);
            student.addCourse(course1);
            student.addCourse(course2);
            student.addCourse(failure);
            student.scheduleMaker();
            fail();

        }
        catch (TooLongDuration e) {
            fail();
        } catch (AlreadyExists e) {

        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }

        LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
        assertEquals(schedule.size(),0);

        LinkedHashMap<LocalDate, List<HomeWork>> scheduleAnother = another.getSchedule();
        assertEquals(scheduleAnother.size(),1);
        assertEquals(scheduleAnother.get(hwk1.getDate()).size(), 1);
        assertTrue(scheduleAnother.get(hwk1.getDate()).contains(hwk1));
        assertFalse(scheduleAnother.get(hwk1.getDate()).contains(hwk3));
    }

    @Test
    public void testingScheduleWithDurationConflict(){

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
            displaySchedule();
        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingTooLongDurationFirstAndAlreadyExistsSecond(){
        try {
            course2.addHomeWork(hwk3);
            course2.addHomeWork(hwk4);
            student.addCourse(course2);
            student.scheduleMaker();
            fail();
            Course c1 = new Course(course1.getCourseName());
            student.addCourse(c1);
        } catch (TooLongDuration e) {

        } catch (AlreadyExists e) {

        } catch (NullHomeWorkException e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingAlreadyExistsFirstAndTooLongDurationSecond(){
        try {
            course2.addHomeWork(hwk3);
            course2.addHomeWork(hwk4);
            student.addCourse(course2);

            Course c1 = new Course(course2.getCourseName());
            student.addCourse(c1);
            fail();
            student.scheduleMaker();

        } catch (AlreadyExists e) {

        } catch (TooLongDuration e) {

        } catch (NullHomeWorkException e) {
            fail();
        } catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingCheckCourseListFailWith1Item(){
        try{
            student.addCourse(course1);
            assertFalse(student.checkCourseList(course1));

        } catch(AlreadyExists e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingCheckCourseListFailWith2Items(){
        try{
            student.addCourse(course1);
            student.addCourse(course2);
            assertFalse(student.checkCourseList(course2));

            //confirming if it still works
            Course temp = new Course("temp");
            assertTrue(student.checkCourseList(temp));

        } catch(AlreadyExists e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingCheckCourseListPassWith2Items(){
        try{
            Course course3 = new Course("trial");
            student.addCourse(course1);
            student.addCourse(course2);
            assertTrue(student.checkCourseList(course3));

        } catch(AlreadyExists e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingCheckCourseListPassWith1Item(){
        try{
            student.addCourse(course1);
            assertTrue(student.checkCourseList(course2));

        } catch(AlreadyExists e) {
            fail();
        }  catch (NullCourseException e) {
            fail();
        }
    }

    @Test
    public void testingCheckCourseListPassWithNoItems(){
        assertTrue(student.checkCourseList(course1));
    }

    @Test
    public void testingAddHomeWorkToScheduleAbove20Hrs() {
        try {

            student.addHomeWorkToSchedule(hwk3);
            student.addHomeWorkToSchedule(hwk4);
        } catch (TooLongDuration e) {

        }
    }

    @Test
    public void testingAddHomeWorkToScheduleTotalDurationAbove20Hrs() {
        try {
            String hwk4Date = "25/03/2003";
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
            LocalDate localDate4 = LocalDate.parse(hwk4Date, formatter1);
            HomeWork homeWork = new HomeWork("tooLong", localDate4, "english", 20, 50);
            student.addHomeWorkToSchedule(homeWork);
        } catch (TooLongDuration e) {

        }
    }
}
