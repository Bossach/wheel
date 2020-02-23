import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Face extends JFrame {

    private Wheel wheel;
    private JLabel velocity;

    private Thread updater;

    public static void main(String[] args) {
        Wheel wheel = new Wheel(3);
        Face face = new Face(wheel);
    }

    public Face(Wheel wheel) {
        this.wheel = wheel;

        setBounds(300, 300, 200, 120);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        velocity = new JLabel();
        add(velocity);
        //addForce button
        JButton addForce = new JButton("Add Force");
        addForce.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wheel.addForce(5, 500);
            }
        });
        add(addForce);
        //BrakeUntilStop button
        JButton brakeUntilStop = new JButton("Brake");
        brakeUntilStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread brake = wheel.brakeUntilStop(10);
                brakeUntilStop.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (brake.isAlive()) {
                            Thread.yield();
                        }
                        brakeUntilStop.setEnabled(true);
                    }
                }).start();
            }
        });

        add(brakeUntilStop);

        //updater
        runUpdater(100);

        //
        setVisible(true);

    }

    private void runUpdater(long msTimeout) {
        updater = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Thread.interrupted()) {
                    while (true) {
                        try {
                            Thread.sleep(msTimeout);
                        } catch (InterruptedException e) {
                            return;
                        }
                        update();
                    }
                }
            }
        });
        updater.setDaemon(true);
        updater.start();
    }

    private void update() {
        velocity.setText(String.format("%.3f", wheel.getCurrentVelocity()));
    }
}
