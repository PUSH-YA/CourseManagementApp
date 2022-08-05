package ui;

import model.Course;
import model.Student;
import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CourseManagementUI {

    private static final int WIDTH = 720;
    private static final int HEIGHT = 580;
    private JFrame frame;
    private JPanel panel;
    private JPanel buttonPanel;
    private Student student;

    //EFFECTS: instantiates a student and calls create and show gui
    public CourseManagementUI(Student student) {
        this.student = student;
        createAndShowGUI();
    }

    //MODIFIES: panel and frame
    //EFFECTS: creates the jframe for this and shows the methods for each function
    //          showing labels
    //          adding new course
    //          adding new homework
    //          editing an existing homework
    //          display the schedule
    //          quit the application
    public void createAndShowGUI() {
        frame = new JFrame(student.getName() + "'s course management system");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 10));

        panel = new JPanel();
        panel.setLayout(new GridLayout(3,1,10,10));
        panel.setBackground(Color.darkGray);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3,1));
        buttonPanel.setBackground(Color.darkGray);
        buttonPanel.setSize(new Dimension(300,400));

        showLabels();
        askForCourse();
        hwkButtons();
        displayScheduleAndQuit();
        panel.add(buttonPanel);
        frame.add(panel);
        frame.setVisible(true);

    }


    public void showLabels() {
        JPanel labelPanel  = new JPanel();
        labelPanel.setLayout(new GridLayout(2,1));
        labelPanel.setBackground(Color.darkGray);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.setBackground(Color.darkGray);

        JLabel nameLabel = new JLabel(student.getName() + "'s",
                new ImageIcon("./src/main/ui/images/name.png"), JLabel.CENTER);
        nameLabel.setForeground(Color.white);
        nameLabel.setFont(new Font("Courier", Font. BOLD,60));

        JLabel title1 = new JLabel("Course");
        title1.setForeground(Color.white);
        title1.setFont(new Font("Courier", Font. BOLD,30));

        JLabel title2 = new JLabel("Management");
        title2.setForeground(Color.white);
        title2.setFont(new Font("Courier", Font. BOLD,30));

        JLabel title3 = new JLabel("System");
        title3.setForeground(Color.white);
        title3.setFont(new Font("Courier", Font. BOLD,30));

        addIconToPanel(namePanel, labelPanel, title1, title2, title3, nameLabel);

    }

    private void addIconToPanel(JPanel namePanel, JPanel labelPanel, JLabel title1,
                                JLabel title2, JLabel title3, JLabel nameLabel) {
        JLabel icon = new JLabel(new ImageIcon("./src/main/ui/images/title.png"));

        namePanel.add(title1);
        namePanel.add(title2);
        namePanel.add(title3);
        labelPanel.add(namePanel);
        labelPanel.add(icon);
        panel.add(nameLabel);
        panel.add(labelPanel);
    }

    //MODIFIES: panel and frame
    //EFFECTS: shows the add course buttons, text and label
    //          when clicked, sends the course string to addCourseForStudent
    public void askForCourse() {
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new FlowLayout());
        coursePanel.setBackground(Color.darkGray);

        JButton button = new JButton("Add");
        button.setBackground(Color.getHSBColor(58, 64, 27));
        JLabel text = new JLabel("New course name");
        text.setForeground(Color.white);
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 30));

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String course = field.getText();
                if (course.length() <= 0) {
                    JOptionPane.showMessageDialog(frame, "put a proper course name :(");
                    new CourseManagementUI(student);
                    frame.dispose();
                } else {
                    addCourseForStudent(course);
                }
            }
        });

        addCourseButtonsToPanels(coursePanel, text, field, button);
    }

    private void addCourseButtonsToPanels(JPanel coursePanel, JLabel text, JTextField field, JButton button) {
        coursePanel.add(text);
        coursePanel.add(field);
        coursePanel.add(button);
        buttonPanel.add(coursePanel);
    }

    //MODIFIES: student [listOfCourses]
    //EFFECTS: if !existsAlready adds the course with the name to the students
    //          else throws AlreadyExists error
    private void addCourseForStudent(String name) {
        name = name.toLowerCase();
        try {
            Course course = new Course(name);
            student.addCourse(course);
            JOptionPane.showMessageDialog(frame, course.getCourseName() + " Course added");
            CourseManagementUI ui = new CourseManagementUI(this.student);
            frame.dispose();
        }  catch (AlreadyExists e) {
            JOptionPane.showMessageDialog(frame, " this already exists");
        }  catch (NullCourseException e) {
            JOptionPane.showMessageDialog(frame,
                    " An error occurred while adding the course, I am sorry :( ...  try again?");

        }

    }


    //MODIFIES: panel and frame
    //EFFECTS: shows the add homework buttons, text and label
    //          when clicked, opens the addHomeWorkUI
    public void hwkButtons() {
        JPanel hwkPanel = new JPanel();
        hwkPanel.setLayout(new FlowLayout());
        hwkPanel.setBackground(Color.darkGray);
        hwkPanel.setLayout(new GridLayout(1,2, 10, 10));

        JButton addHwk = new JButton("Add HomeWork");
        addHwk.setBackground(Color.getHSBColor(58, 64, 27));
        addHwk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AddHomeWorkUI hu = new AddHomeWorkUI(student);

            }
        });

        JButton editHwk = new JButton("Edit HomeWork");
        editHwk.setBackground(Color.getHSBColor(58, 64, 27));
        editHwk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EditHomeWorkUI edit = new EditHomeWorkUI(student);

            }
        });

        hwkPanel.add(addHwk);
        hwkPanel.add(editHwk);
        buttonPanel.add(hwkPanel);
    }


    private void displayScheduleAndQuit() {

        JPanel lastPanel = new JPanel();
        lastPanel.setLayout(new FlowLayout());
        lastPanel.setBackground(Color.darkGray);
        lastPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton displaySchedule = new JButton("Display Schedule");
        displaySchedule.setBackground(Color.getHSBColor(58, 64, 27));
        displaySchedule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DisplaySchedule display = new DisplaySchedule(student);

            }
        });

        JButton quit = new JButton("Quit");
        quit.setBackground(Color.getHSBColor(58, 64, 27));
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Quit bye = new Quit(student);

            }
        });

        lastPanel.add(displaySchedule);
        lastPanel.add(quit);
        buttonPanel.add(lastPanel);
    }



}
