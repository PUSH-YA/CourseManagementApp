package ui;

import javax.swing.*;
import java.awt.*;

public class FramesUI {

    protected JFrame frame;
    protected JPanel panel;

    //EFFECTS:      creates the jframe for this and sets up frame with correct height and border
    //              creates the jpanel with grid layout and dark grey
    public FramesUI(String name, int width, int height, LayoutManager layout) {
        frame = new JFrame(name);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.darkGray, 10));

        panel = new JPanel();
        panel.setLayout(layout);
        panel.setBackground(Color.darkGray);

    }
}
