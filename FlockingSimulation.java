import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class FlockingSimulation extends JPanel {
    private ArrayList<Boid> boids;
    private int numBoids = 100;

    private FlockingSimulation() {
        setPreferredSize(new Dimension(800, 600));
        boids = new ArrayList<>();
        for (int i = 0; i < numBoids; i++) {
            boids.add(new Boid(new Random().nextInt(800), new Random().nextInt(600)));
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
