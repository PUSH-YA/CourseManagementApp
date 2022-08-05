package ui;

import model.Course;
import model.HomeWork;
import model.Student;
import model.exceptions.NullCourseException;
import model.exceptions.NullHomeWorkException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditHomeWorkUI {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 300;
    private JFrame frame;
    private JPanel panel;
    private Student student;

    //JTextFields
    private JTextField courseField;
    private JTextField nameField;
    private JTextField gradeField;
    private JTextField doneField;

    //JLabels
    private JLabel courseLabel;
    private JLabel nameLabel;
    private JLabel gradeLabel;
    private JLabel doneLabel;

    //JButton
    private JButton button;

    //EFFECTS: creates a new JFrame,JPanel, JButton and JTextField for editing homework frame
    //          sets up the frame with the correct height and border
    //          sets up the panel with the grid layout and dark grey background
    //          instantiate JTextFields, JLabels and Jbutton
    //          calls to change colour and then add to frame
    public EditHomeWorkUI(Student student) {
        this.student = student;
        frame = new JFrame("Edit HomeWork");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 3));

        panel = new JPanel();
        panel.setLayout(new GridLayout(5,2,5,5));
        panel.setBackground(Color.darkGray);

        courseField = new JTextField();
        nameField = new JTextField();
        gradeField = new JTextField();
        doneField = new JTextField();

        courseLabel = new JLabel("Course of HomeWork name: ");
        nameLabel = new JLabel("HomeWork name: ");
        gradeLabel = new JLabel("grade achieved [integer] : ");
        doneLabel = new JLabel("is it done? [true or false]");

        button = new JButton("submit");

        changeColor();
        addToFrame();
    }

    //MODIFIES:JLabels, button
    //EFFECTS: changes the colour of the fields and the button
    private void changeColor() {
        button.setBackground(Color.getHSBColor(58, 64, 27));
        courseLabel.setForeground(Color.white);
        nameLabel.setForeground(Color.white);
        gradeLabel.setForeground(Color.white);
        doneLabel.setForeground(Color.white);
    }

    //MODIFIES: panel and frame
    //EFFECTS: adds all the buttons, fields and labels to the panels and adds that to the class
    private void addToFrame() {
        panel.add(courseLabel);
        panel.add(courseField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(gradeLabel);
        panel.add(gradeField);
        panel.add(doneLabel);
        panel.add(doneField);

        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);

        editHomeWorkButtonAction(button);
    }


    //MODIFIES: student [homework]
    //EFFECTS: creates the action even for the submit button and shows the corresponding
    //          message based on the error/ exception received
    //          shows an error message if done is not "true" or "false"
    //          dispose the frame after being successfully editing homework
    private void editHomeWorkButtonAction(JButton button) {

        button.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                String course = courseField.getText();
                String name = nameField.getText();
                int grade = Integer.parseInt(gradeField.getText());
                String done = doneField.getText().toLowerCase();
                if (!done.equals("true") && !done.equals("false")) {
                    JOptionPane.showMessageDialog(frame, "please just use true or false");
                }
                Boolean doneBool = Boolean.parseBoolean(done);
                try {
                    Course courseFound = getCourse(course);
                    editHomeWork(courseFound, name, grade, doneBool);
                    frame.dispose();
                } catch (NullCourseException l) {
                    JOptionPane.showMessageDialog(frame, "No such course exists");
                } catch (NullHomeWorkException n) {
                    JOptionPane.showMessageDialog(frame, "No such homework exists");
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
            courseName.toLowerCase();
            if (courseName.equals(name)) {
                courseChosen = course;
            }
        }
        if (courseChosen == null) {
            throw new NullCourseException();
        }
        return courseChosen;
    }

    //MODIFIES: student [homework]
    //EFFECTS: edits the homework based on the grade and its status
    //          shows the homework grade, course grade and the current status
    public void editHomeWork(Course course, String name, int grade, Boolean done)
                    throws NullHomeWorkException {
        try {
            HomeWork hwk = getHomeWork(course, name);
            hwk.setGrade(grade);
            hwk.changeStatus(done);
            String message = "homework grade is " + hwk.getGrade() + "\n"
                    + "Course grade is " + course.getGrade();
            if (done) {
                message = message + "\n homework is done";
            } else {
                message = message + "\n homework is incomplete";
            }
            JOptionPane.showMessageDialog(frame, message);
        } catch (NullHomeWorkException e) {
            throw new NullHomeWorkException();
        }

    }

    //EFFECTS: takes the the course, and then takes the input of the homework
    //        goes through the list of homeworks in the courses of the student and
    //        then returns the course with the same name
    //        if no homework is found, throws NullHomeWorkException
    public HomeWork getHomeWork(Course course, String name) throws NullHomeWorkException {
        HomeWork homeworkChosen = null;
        name = name.toLowerCase();
        java.util.List<HomeWork> hwkList = course.getHomeworks();
        for (HomeWork hwk : hwkList) {
            String hwkName = hwk.getName().toLowerCase();
            if (hwkName.equals(name)) {
                homeworkChosen = hwk;
            }
        }
        if (homeworkChosen == null) {
            throw new NullHomeWorkException();
        }
        return homeworkChosen;
    }



}
