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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddHomeWorkUI {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 300;
    private JFrame frame;
    private JPanel panel;
    private Student student;

    //JTextFields
    private JTextField courseField;
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

    //EFFECTS: creates a new JFrame,JPanel, JButton and JTextField for adding homework frame
    public AddHomeWorkUI(Student student) {
        this.student = student;
        frame = new JFrame("New HomeWork adding");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 3));

        panel = new JPanel();
        panel.setLayout(new GridLayout(6,2,5,5));
        panel.setBackground(Color.darkGray);

        courseField = new JTextField();
        nameField = new JTextField();
        dateField = new JTextField();
        durationField = new JTextField();
        weighingField = new JTextField();

        courseLabel = new JLabel("Course of HomeWork name: ");
        nameLabel = new JLabel("HomeWork name: ");
        dateLabel = new JLabel("date in dd/mm/yyyy format: ");
        durationLabel = new JLabel("duration as a number [hours]");
        weighingLabel = new JLabel("weighing of homework as a number [%]");

        button = new JButton("submit");

        changeColour();
    }

    //EFFECTS: creates all the button, fields and labels with the color for the adding homework class
    public void changeColour() {
        button.setBackground(Color.getHSBColor(58, 64, 27));
        courseLabel.setForeground(Color.white);
        nameLabel.setForeground(Color.white);
        dateLabel.setForeground(Color.white);
        durationLabel.setForeground(Color.white);
        weighingLabel.setForeground(Color.white);
        addToFrame();
    }

    //MODIFIES: panel and frame
    //EFFECTS: adds all the buttons, fields and labels to the panels and adds that to the class
    private void addToFrame() {


        panel.add(courseLabel);
        panel.add(courseField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(durationLabel);
        panel.add(durationField);
        panel.add(weighingLabel);
        panel.add(weighingField);
        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);

        addHomeWorkButtonAction(button);
    }

    //MODIFIES: student
    //EFFECTS: creates the action even for the submit button and shows the corresponding
    //          message based on the error recived
    private void addHomeWorkButtonAction(JButton button) {

        button.addActionListener(new ActionListener()  {

            public void actionPerformed(ActionEvent e) {
                try {
                    String course = courseField.getText();
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

        });
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
            courseChosen.addHomeWork(homeWork);
            student.addHomeWorkToSchedule(homeWork);
            JOptionPane.showMessageDialog(frame, name + " added to " + courseChosen.getCourseName());
            frame.dispose();
        } catch (TooLongDuration e) {
            throw new TooLongDuration();
        } catch (AlreadyExists e) {
            throw new AlreadyExists();
        } catch (NullHomeWorkException e) {
            throw new NullHomeWorkException();
        }
    }


}
