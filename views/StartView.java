package views;

import javax.swing.*;
import java.awt.*;

public class StartView extends JFrame {
    public StartView() {
        super("Choose your role");

        // set layout
        //this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        //this.getContentPane().setMargin(new Insets(10, 10, 10, 10));

        Container pane = new Container();
        this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        this.getContentPane().add(pane);
        this.getContentPane().setBackground(Color.black);
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // btnHost
        JButton btnHost = new JButton("Host a Game");
        btnHost.setMargin(new Insets(40, 30, 40, 30));
        btnHost.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHost.setBackground(Color.darkGray);
        btnHost.setOpaque(true);
        pane.add(btnHost);

        // btnJoin
        JButton btnJoin = new JButton("Join a Game");
        btnJoin.setMargin(new Insets(40, 30, 40, 30));
        btnJoin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJoin.setBackground(Color.darkGray);
        pane.add(btnJoin);

        // make visible, resize, and prepare for exit
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}