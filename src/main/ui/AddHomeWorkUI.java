package ui;

import model.Course;
import model.HomeWork;
import model.Student;
import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;
import model.exceptions.NullHomeWorkException;
import model.exceptions.TooLongDuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddHomeWorkUI extends FramesUI {
    private Student student;

    //JTextFields
    private JComboBox  coursebox;
    private JTextField nameField;
    private JTextField dateField;
    private JTextField durationField;
    private JTextField weighingField;

    //JLabels
    private JLabel courseLabel;
    private JLabel nameLabel;
    private JLabel dateLabel;
    private JLabel durationLabel;
    private JLabel weighingLabel;

    //JButton
    private JButton button;

    //EFFECTS: instantiates the student from the constructor
    //         creates a new JFrame,JPanel, JButton and JTextField for adding homework ui
    //          change colour
    public AddHomeWorkUI(Student student) {
        super("New homework adding", 480, 300, new GridLayout(6,2,5,5));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.student = student;

        coursebox = new JComboBox(getName(student.getListOfCourses()));
        nameField = new JTextField();
        dateField = new JTextField();
        durationField = new JTextField();
        weighingField = new JTextField();

        courseLabel = new JLabel("Course of HomeWork name: ");
        nameLabel = new JLabel("HomeWork name: ");
        dateLabel = new JLabel("date in dd/mm/yyyy format: ");
        durationLabel = new JLabel("duration in hours [integer]");
        weighingLabel = new JLabel("weighing in % [integer]");

        button = new JButton("submit");

        changeColour();
    }


    //EFFECTS: takes a list of courses and returns a string of their name
    private String[] getName(List<Course> courses) {
        String[] names = new String[courses.size()];
        int i = 0;
        for (Course c : courses) {
            names[i] = c.getCourseName();
            i++;
        }
        return names;
    }

    //MODIFIES: button and JLabels
    //EFFECTS: sets the colour of the button to cape honey colour
    //          set the label colour to white
    //          calls to add to frame
    //          then adds panel to frame and sets visibility true
    public void changeColour() {
        button.setBackground(Color.getHSBColor(58, 64, 27));
        courseLabel.setForeground(Color.white);
        nameLabel.setForeground(Color.white);
        dateLabel.setForeground(Color.white);
        durationLabel.setForeground(Color.white);
        weighingLabel.setForeground(Color.white);
        addToFrame();
        frame.add(panel);
        frame.setVisible(true);
    }

    //MODIFIES: panel and frame
    //EFFECTS: adds all the buttons, fields and labels to the panels and adds that to the class
    //          calls add homework action if button or pressed enter
    private void addToFrame() {
        panel.add(courseLabel);
        panel.add(coursebox);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(durationLabel);
        panel.add(durationField);
        panel.add(weighingLabel);
        panel.add(weighingField);
        panel.add(button);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addHomeWorkButtonAction();
            }
        });

        button.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addHomeWorkButtonAction();
                }
            }
        });
    }

    //MODIFIES: student
    //EFFECTS: creates the action event for the submit button and shows the corresponding
    //          dialogue message based on the error/exception received
    //          send the text fields received to add homework in schedule
    //          also show the message if the name is ""
    private void addHomeWorkButtonAction() {
        try {
            String course = coursebox.getSelectedItem().toString();
            String name = nameField.getText();
            if (name.length() <= 0) {
                JOptionPane.showMessageDialog(frame,"Put a proper homework name, stop trying to find bugs :(");
            }
            String date = dateField.getText();
            String duration = durationField.getText();
            String weighing = weighingField.getText();
            addHomeWorkInSchedule(getCourse(course), name, date, weighing, duration);
        } catch (NullCourseException l) {
            JOptionPane.showMessageDialog(frame, "No such course exists");
        } catch (TooLongDuration d) {
            JOptionPane.showMessageDialog(frame, "Split your work :), have < 20 hrs work each day");
        } catch (AlreadyExists a) {
            JOptionPane.showMessageDialog(frame, "This homework already exists");
        } catch (NullHomeWorkException n) {
            JOptionPane.showMessageDialog(frame, "Something went wrong, please try again :)");
        }
    }


    //EFFECTS: takes the input of the course,
    //        goes through the list of courses of the student and returns the course with the same name
    //        if no course found, throws NullCourseException
    private Course getCourse(String name) throws NullCourseException {
        Course courseChosen = null;
        name = name.toLowerCase();
        List<Course> courseList = student.getListOfCourses();
        for (Course course : courseList) {
            String courseName = course.getCourseName();
            courseName = courseName.toLowerCase();
            if (courseName.equals(name)) {
                courseChosen = course;
            }
        }
        if (courseChosen == null) {
            throw new NullCourseException();
        }
        return courseChosen;
    }


    //MODIFIES: student
    //EFFECTS: takes the course chosen and name and then takes the input of
    //          date, duration and weighing of the homework and then creates the homework
    //          if (!catches any exception), adds the homework to the given course
    //          if catches AlreadyExists or TooLongDuration, throws it
    private void addHomeWorkInSchedule(
            Course courseChosen, String name, String startDate, String weighingString, String durationString)
            throws TooLongDuration, AlreadyExists, NullHomeWorkException {

        int duration = Integer.parseInt(durationString);
        double weighing = Double.parseDouble(weighingString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(startDate, formatter);
        if (duration > 20) {
            throw new TooLongDuration();
        }
        HomeWork homeWork  = new HomeWork(name, localDate, courseChosen.getCourseName(), duration, weighing);
        try {
            student.addHomeWorkToSchedule(homeWork);
            courseChosen.addHomeWork(homeWork);
            System.out.println(student.getListOfCourses().size());
            System.out.println(student.getListOfCourses().get(0).getHomeworks().size());
            JOptionPane.showMessageDialog(frame, name + " added to " + courseChosen.getCourseName());
            frame.dispose();
        } catch (TooLongDuration e) {
            throw new TooLongDuration();
        }
    }


}
