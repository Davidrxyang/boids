import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;


public class FlockingSimulation extends JPanel {
    private ArrayList<Boid> boids;
    private int numBoids = 100;
    private float maxSpeed = 4;
    private float maxForce = 0.1f;

    int WIDTH = 800;
    int HEIGHT = 500;

    private FlockingSimulation() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT + 200));


        boids = new ArrayList<>();
        for (int i = 0; i < numBoids; i++) {
            boids.add(new Boid(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT)));
        }

        // Add controls
        JSlider numBoidsSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        numBoidsSlider.setMajorTickSpacing(50);
        numBoidsSlider.setPaintTicks(true);
        numBoidsSlider.setPaintLabels(true);
        numBoidsSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int num = (int) source.getValue();
                    setNumBoids(num);
                }
            }
        });

        JSlider maxSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 4);
        maxSpeedSlider.setMajorTickSpacing(1);
        maxSpeedSlider.setPaintTicks(true);
        maxSpeedSlider.setPaintLabels(true);
        maxSpeedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    float speed = (float) source.getValue();
                    setMaxSpeed(speed);
                }
            }
        });

        JSlider maxForceSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        maxForceSlider.setMajorTickSpacing(1);
        maxForceSlider.setPaintTicks(true);
        maxForceSlider.setPaintLabels(true);
        maxForceSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    float force = (float) (source.getValue() * 0.1);
                    setMaxForce(force);
                }
            }
        });

        JPanel controlPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        controlPanel.setBackground(Color.LIGHT_GRAY);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.add(new JLabel("Number of Boids:"));
        controlPanel.add(numBoidsSlider);
        controlPanel.add(new JLabel("Max Speed:"));
        controlPanel.add(maxSpeedSlider);
        controlPanel.add(new JLabel("Max Force:"));
        controlPanel.add(maxForceSlider);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void setNumBoids(int num) {
        numBoids = num;
        resetBoids();
    }

    private void setMaxSpeed(float speed) {
        maxSpeed = speed;
        updateBoidsSpeed();
    }

    private void setMaxForce(float force) {
        maxForce = force;
        updateBoidsForce();
    }

    private void resetBoids() {
        boids.clear();
        for (int i = 0; i < numBoids; i++) {
            boids.add(new Boid(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT)));
        }
    }

    private void updateBoidsSpeed() {
        for (Boid b : boids) {
            b.maxSpeed = maxSpeed;
        }
    }

    private void updateBoidsForce() {
        for (Boid b : boids) {
            b.maxForce = maxForce;
        }
    }

    private void update() {
        for (Boid b : boids) {
            b.update(boids);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Boid b : boids) {
            b.display(g2d);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flocking Simulation");
        FlockingSimulation flockingSimulation = new FlockingSimulation();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(flockingSimulation);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {
            flockingSimulation.update();
            flockingSimulation.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
