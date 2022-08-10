package ui;

import model.Event;
import model.EventLog;
import model.Student;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Quit extends FramesUI {
    private JButton save;
    private JButton noSave;
    private Student student;

    //EFFECTS:  instantiates student
    //          calls show buttons
    public Quit(Student student) {
        super("Quit", 300, 150, new GridLayout(2,1,10,10));
        this.student = student;
        showButtons();
    }

    //EFFECTS: show buttons with the correct images and cape honey colour
    //       add buttons to panel and panel to frame
    //       call to button actions method
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

    //MODIFIES: studentName.json
    //EFFECTS: if save button, saves to json file with the student name and exit system
    //          else, exists the system without saving
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
                    printLog(EventLog.getInstance());
                    System.exit(0);
                }
            }
        });

        noSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printLog(EventLog.getInstance());
                System.exit(0);
            }
        });

    }

    private void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString());
        }
    }
}
