package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWorkTest {
    HomeWork hwk;
    LocalDate localDate1;

    @BeforeEach
    public void setup(){
        String hwk1Date = "24/03/2003";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        localDate1 = LocalDate.parse(hwk1Date, formatter1);
        hwk = new HomeWork("hwk", localDate1 , "cpsc", 3, 30.0);
    }

    @Test
    public void testGetters(){
        assertEquals(hwk.getName(), "hwk");
        assertEquals(hwk.getWeighing(), 30.0);
        assertEquals(hwk.getGrade(), 0);
        assertEquals(hwk.getDuration(), 3);
        assertEquals(hwk.getDeadline(), localDate1);
        assertEquals(hwk.getStatus(), "incomplete");
        assertEquals(hwk.getCourse(), "cpsc");
    }

    @Test
    public void testChangeStatusAndGrade(){
        assertEquals(hwk.getStatus(), "incomplete");
        hwk.changeStatus();
        assertEquals(hwk.getStatus(), "done");

        assertEquals(hwk.getGrade(), 0);
        hwk.setGrade(99);
        assertEquals(hwk.getGrade(), 99);
    }
}
