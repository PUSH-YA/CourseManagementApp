package ui;

import model.Student;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Quit {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 150;
    private JFrame frame;
    private JPanel panel;
    private JButton save;
    private JButton noSave;
    private Student student;

    public Quit(Student student) {
        this.student = student;
        frame = new JFrame("Quit");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 10));

        panel = new JPanel();
        panel.setLayout(new GridLayout(2,1,10,10));
        panel.setBackground(Color.darkGray);
        showButtons();
    }

    private void showButtons() {
        save = new JButton("save");
        save.setBackground(Color.getHSBColor(58, 64, 27));
        save.setIcon(new ImageIcon("./src/main/ui/images/save.png"));
        save.setHorizontalAlignment(JLabel.CENTER);
        noSave = new JButton("do not save");
        noSave.setBackground(Color.getHSBColor(58, 64, 27));
        noSave.setIcon(new ImageIcon("./src/main/ui/images/noSave.png"));
        noSave.setHorizontalAlignment(JLabel.CENTER);

        buttonActions();

        panel.add(save);
        panel.add(noSave);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void buttonActions() {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JsonWriter writer = new JsonWriter("./data/" + student.getName() + ".json");
                    writer.open();
                    writer.write(student);
                    writer.close();
                } catch (IOException l) {
                    System.out.println("could not file your file...contact the developer");
                } finally {
                    System.exit(0);
                }
            }
        });

        noSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
