package ui;

import model.Course;
import model.Student;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

public class AskNameUI {
    private static final int WIDTH = 350;
    private static final int HEIGHT = 100;
    private JFrame frame;
    private JPanel panel;
    private Student student;
    private JButton button;
    private JTextField field;

    //EFFECTS: creates a new JFrame,JPanel, JButton and JTextField for asking name frame
    public AskNameUI() {
        frame = new JFrame("Student name");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 3));

        panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.setLayout(new FlowLayout());

        field = new JTextField();
        button = new JButton("enter name");
        askForName();
    }

    //EFFECTS: prompts the student to write the name in the field and shows the buttons, fields on the frame
    public void askForName() {
        button.setBackground(Color.getHSBColor(58, 64, 27));
        JLabel text = new JLabel("Your name: ", new ImageIcon("./src/main/ui/images/askName.png"), JLabel.RIGHT);
        text.setForeground(Color.white);

        field.setPreferredSize(new Dimension(100, 30));

        panel.add(text);
        panel.add(field);
        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);
        sendName();

    }

    //EFFECTS: takes the name and then finds the correct name from the json file
    //         sends the student from json file to the CourseManagementUI if it exists
    //          creates a new Student if it no json file exists and sends that
    //          also shows Welcome, student name to the user
    private void sendName() {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = field.getText();
                name = name.toLowerCase();
                if (name.length() <= 0) {
                    JOptionPane.showMessageDialog(frame, "Put a name in, don't try to find bugs :( ");
                    new AskNameUI();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Welcome, " + name);

                    try {
                        JsonReader reader = new JsonReader("./data/" + name + ".json");
                        student = reader.read();

                    } catch (IOException l) {
                        student = new Student(name);

                    } finally {
                        CourseManagementUI cu = new CourseManagementUI(student);
                        frame.dispose();

                    }
                }
            }
        });
    }
}
