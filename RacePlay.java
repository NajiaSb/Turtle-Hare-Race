import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RacePlay extends JPanel implements ActionListener {
    private JFrame frame;
    private JLabel label;
    private JButton button;
    private Timer time1, time2;
    private ArrayList<Point> point;
    private JPanel backJPanel, southJPanel;
    private Image backImage, hareImage, turtleImage;
    private int move1 = 0, move2 = 0, speed1 = 0, speed2 = 0, speed3 = 0, count = 0, elapsedTime = 0, i = 0;

    RacePlay() {
        // JLabel for player instructions and feedback
        label = new JLabel(" Press 'Start' to start game.");
        label.setForeground(Color.WHITE);

        // JButton to initialize game
        button = new JButton("Start");
        button.setBounds(50, 30, 50, 50);

        // JPanel that contains label and button
        southJPanel = new JPanel(new BorderLayout());
        southJPanel.setPreferredSize(new Dimension(431, 30));
        southJPanel.setBackground(new Color(164, 127, 96));
        southJPanel.add(label, BorderLayout.WEST);
        southJPanel.add(button, BorderLayout.EAST);

        // Initialize two timer events for hare and turtle
        time1 = new Timer(500, this);
        time2 = new Timer(600, this);

        // Set dimensions of playing panel
        this.setPreferredSize(new Dimension(431, 320));

        // Get pictures as ImageIcons
        backImage = new ImageIcon("background.png").getImage();
        hareImage = new ImageIcon("hare.png").getImage();
        turtleImage = new ImageIcon("turtle.png").getImage();

        // Add two panels to another JPanel
        backJPanel = new JPanel(new BorderLayout());
        backJPanel.add(southJPanel, BorderLayout.SOUTH);
        backJPanel.add(this, BorderLayout.CENTER);

        // Initlaize sleeping points ArrayList
        point = new ArrayList<Point>();

        // Initialize JFrame and add elements
        frame = new JFrame("Turtle Hare Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(431, 350);
        frame.add(backJPanel);
        frame.setResizable(false);
        frame.setVisible(true);

        // Add button listener to actionListener
        button.addActionListener(this);
    }

    public void Start() {

        point.clear(); // Clear Arraylist
        label.setText(" Press 'Start' to start game.");
        count = elapsedTime = move1 = move2 = speed1 = speed2 = speed3 = i = 0;

        // Get user inputs
        speed1 = Integer.parseInt(JOptionPane.showInputDialog("Set speed of Hare."));
        speed2 = Integer.parseInt(JOptionPane.showInputDialog("Set speed of Turtle."));
        count = Integer.parseInt(JOptionPane.showInputDialog("Number of sleeps (1 or 2)?"));
        speed3 = Integer.parseInt(JOptionPane.showInputDialog("Length of sleep in milliseconds?"));

        label.setText(" Click " + count + " points and double click and part to start game.");
        time1.setDelay(speed1);
        time2.setDelay(speed2);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ((count == 1 || count == 2) && i < count) { // Click to get 0, 1, or 2 points
                    point.add(e.getPoint());
                    i++;
                }
                if (e.getClickCount() == 2) { // Double click to start timers and game
                    time1.start();
                    time2.start();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backImage, 0, 0, null); // Draw background
        g2d.drawImage(hareImage, move1, 110, null); // Draw hare
        g2d.drawImage(turtleImage, move2, 140, null); // Draw turtle
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button) {
            Start();
        }
        if (e.getSource() == time1) { // for hare timer event, move on x axis once
            move1++;
            elapsedTime = 0; // Used to sleep time later
            repaint(); // Call repaint
        }
        if (e.getSource() == time2) { // for turtle timer event, move on x axis once
            move2++;
            repaint(); // Call repaint
        }

        // Winning status if hare/turtle reach other end of x axis
        if (move1 >= 375 && move2 >= 375) {
            label.setText(" Tie!!");
            time1.stop();
            time2.stop();
        } else if (move1 >= 375) {
            time1.stop();
            time2.stop();
            label.setText(" Hare Won!!");
        } else if (move2 >= 375) {
            time1.stop();
            time2.stop();
            label.setText(" Turtle Won!!");
        }

        // Do sleep time
        for (int i = 0; i < point.size(); i++) {
            if (move1 == point.get(i).x) {
                Timer timer = new Timer(speed3, this); // initlize other timer
                timer.start();
                if (elapsedTime == speed3) { // If delay == speed set by user; will only work once
                    timer.stop();
                    time1.start();
                    elapsedTime = 0;
                } else if (elapsedTime < speed3) {
                    time1.stop();
                }
                elapsedTime += timer.getDelay(); // Add delay
            }
        }
        repaint(); // Call repaint
    }

    public static void main(String[] args) {
        new RacePlay();
    }
}
