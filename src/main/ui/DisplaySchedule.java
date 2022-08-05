package ui;

import model.HomeWork;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class DisplaySchedule {
    private static final int WIDTH = 720;
    private static final int HEIGHT = 580;
    private Student student;
    private JFrame frame;
    private JPanel panel;


    //EFFECTS: instantiates the student from the constructor
    //          creates a jframe with proper colour and border
    //          creates a panel with the correct colour and box layout [y-axis]
    //          adds panel to frame and then calls display method
    public DisplaySchedule(Student student) {
        this.student = student;
        try {
            student.scheduleMaker();
        } catch (Exception e) {
            System.out.println("should not have failed at making schedules");
        }
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 3));

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.darkGray);
        frame.add(panel);
        frame.setVisible(true);
        display();
    }


    // EFFECTS: takes the schedule and parses through the key Set of local date
    //          for each date creates a panel with flow layout, dark colour, cape honey colour and large font
    //          for each homework list sends the panel and the list to addhomework to panel
    //          adds frame to panel
    private void display() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
        Set<LocalDate> localDates = schedule.keySet();
        for (LocalDate l : localDates) {
            JPanel temp = new JPanel();
            temp.setBackground(Color.darkGray);
            temp.setLayout(new FlowLayout(FlowLayout.LEADING));
            JLabel date = new JLabel(l.format(formatter) + ": ");
            date.setForeground(Color.getHSBColor(58, 64, 27));
            date.setFont(new Font("Courier", Font.BOLD, 20));
            temp.add(date);
            addHomeWorksToPanel(schedule.get(l), temp);
            panel.add(temp);
        }
        frame.add(panel);
        frame.setVisible(true);
    }

    //EFFECTS: adds each homework to the panel in flow layout with the course name
    //          indicating the course name and status with icon and text
    private void addHomeWorksToPanel(List<HomeWork> hwks, JPanel temp) {
        for (HomeWork h : hwks) {
            String label = h.getName() + " (" + h.getCourse() + ") "
                    + "(" + doneORNot(h) + ")" + ", ";
            JLabel hwkLabel = new JLabel(label, makeRect(h), JLabel.LEFT);
            hwkLabel.setForeground(Color.WHITE);
            hwkLabel.setFont(new Font("Courier", Font.BOLD, 15));
            temp.add(hwkLabel);
        }
    }

    //EFFECTS: returns "done" if the homework is done
    //          returns "incomplete" if the homework is not done
    private String doneORNot(HomeWork hwk) {
        if (hwk.getStatus()) {
            return "done";
        } else {
            return "incomplete";
        }
    }

    //EFFECTS: returns the icon for homework,
    //         red.png for incomplete homework and green.png for complete homework
    private Icon makeRect(HomeWork hwk) {
        ImageIcon image = null;
        if (hwk.getStatus()) {
            image = new ImageIcon("./src/main/ui/images/green.png");
        } else {
            image = new ImageIcon("./src/main/ui/images/red.png");
        }

        return image;
    }
}

