package ui;

import model.Student;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AskNameUI {
    private static final int WIDTH = 350;
    private static final int HEIGHT = 100;
    private JFrame frame;
    private JPanel panel;
    private Student student;
    private JTextField field;

    //EFFECTS: creates a new JFrame,JPanel, JButton and JTextField for asking name frame
    //         set the jframe colour, border and size jpanel layout and add the
    //          calls add name method
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
        askForName();
    }

    //EFFECTS: prompts the student to write the name in the field
    //          set the button to cape honey colour and label to white
    //          adds the buttons, fields on the frame
    //          adds key listener to the field
    //          calls to send method name if pressed enter
    public void askForName() {
        JLabel text = new JLabel("Your name: ", new ImageIcon("./src/main/ui/images/askName.png"), JLabel.RIGHT);
        text.setForeground(Color.white);

        field.setPreferredSize(new Dimension(100, 30));
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendName();
                }
            }
        });

        panel.add(text);
        panel.add(field);
        frame.add(panel);
        frame.setVisible(true);
    }

    //EFFECTS: takes the name and then finds the correct name from the json file
    //         sends the student from json file to the CourseManagementUI if it exists
    //          creates a new Student if it no json file exists and sends that
    //          also shows Welcome, student name to the user
    //          if no name is an empty string then new this and dispose old this
    //          calls to Course management ui class
    private void sendName() {
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

}
