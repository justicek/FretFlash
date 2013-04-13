import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Creates a GUI.
 */
@SuppressWarnings("serial")
public class UIPanel extends JPanel {

    // Note Generator
    private NoteGenerator ng;

    // Swing components
    private JLabel label;
    private JPanel contentPane;
    private JPanel bottomToolbar;
    private JButton startButton;
    private JButton stopButton;
    private JSlider timeSlider;
    private JPanel buttonArea;
    private JPanel auxToolbar;
    private JLabel speedLabel;
    private JCheckBox muteBox;
    private JCheckBox sharpsBox;
    private JPanel checkArea;

    // Animation variables
    private boolean stopped;
    private Thread startThread;
    private StartAction sa;

    // User variables
    private int timeInt;
    private boolean sound;
    private boolean sharps;

    /**
     * Initializes the program.
     */
    public UIPanel() {
        super(new BorderLayout());
        ng = new NoteGenerator();
        makeFrame();
        timeInt = 3;
        sound = true;
        sharps = true;
    }

    /**
     * Constructs the frame.
     */
    private void makeFrame() {

        // Label - will hold the random note
        label = new JLabel("ready?");
        label.setFont(new Font("Arial", Font.BOLD, 100));

        // Content pane - area holding the label (note)
        contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(350, 250));
        add(contentPane, BorderLayout.NORTH);
        contentPane.add(label);

        // Start button
        startButton = new JButton("Start");
        StartAction sa = new StartAction();
        startButton.addActionListener(sa);

        // Stop button
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent e) {
                stopped = true;
                startThread.stop();
                label.setText("pause.");
            }
        });

        // Speed Slider
        timeSlider = new JSlider(1, 8);
        timeSlider.setMajorTickSpacing(1);
        timeSlider.setValue(3);
        timeSlider.setSnapToTicks(true);
        //timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);
        timeSlider.setFont(new Font("Arial", Font.PLAIN, 9));
        timeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                timeInt = timeSlider.getValue();
                speedLabel.setText("Speed: " + timeSlider.getValue() + "s");
            }
        });
        speedLabel = new JLabel("Speed: " + timeSlider.getValue() + "s");
        speedLabel.setFont(new Font("Arial", Font.BOLD, 11));


        // Checkbox options
        muteBox = new JCheckBox();
        muteBox.setToolTipText("Mute");
        muteBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sound = !sound;
            }
        });
        sharpsBox = new JCheckBox();
        sharpsBox.setToolTipText("Select for flats instead of sharps");
        sharpsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sharps = !sharps;
                ng.accidentals(sharps);
            }
        });

        // Button area - holds start and stop buttons
        buttonArea = new JPanel(new FlowLayout());
        buttonArea.setBackground(new Color(184,213,241));
        buttonArea.add(startButton);
        buttonArea.add(stopButton);

        // Bottom toolbar - holds the start/stop buttons
        bottomToolbar = new JPanel(new FlowLayout());
        bottomToolbar.setBackground(new Color(184,213,241));
        bottomToolbar.setPreferredSize(new Dimension(350, 45));
        add(bottomToolbar, BorderLayout.SOUTH);
        bottomToolbar.add(buttonArea);
        bottomToolbar.add(timeSlider);

        // Checkbox area - holds checkboxes
        checkArea = new JPanel(new FlowLayout());
        checkArea.add(sharpsBox);
        checkArea.add(muteBox);

        // Auxillary toolbar - holds speed, and other options
        auxToolbar = new JPanel(new BorderLayout());
        add(auxToolbar, BorderLayout.CENTER);
        auxToolbar.add(speedLabel, BorderLayout.WEST);
        auxToolbar.add(checkArea, BorderLayout.EAST);
    }

    /**
     * Gets the next note
     */
    public void getNote() {
        label.setText(ng.randomizeNoRpt());
        revalidate();
        repaint();
    }

    /**
     * Action listener for the start button.
     */
    private class StartAction implements ActionListener, Runnable {
        @Override
        public void run() {
            stopped = false;
            boolean virgin = true;
            getNote();
                try {
                    do {
                        if (virgin) {
                            label.setText("get rdy");
                            repaint();
                            virgin = false;
                            Thread.sleep(1000);
                            label.setText("3...");
                            repaint();
                            Thread.sleep(1000);
                            label.setText("2...");
                            repaint();
                            Thread.sleep(1000);
                            label.setText("1...");
                            repaint();
                            Thread.sleep(1000);
                        }
                        else {
                            if (sound)
                                Toolkit.getDefaultToolkit().beep();
                            getNote();
                            Thread.sleep(timeInt * 1000);
                        }
                    }
                    while(!stopped);
                }
                catch (InterruptedException e) {}
            }

        @Override
        public void actionPerformed(ActionEvent e) {
            stopped = false;
            sa = new StartAction();
            startThread = new Thread(sa);
            startThread.start();
        }
    }

}
