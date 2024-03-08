import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Boid {
    private float x, y;
    private float vx, vy;
    private float maxSpeed = 4;
    private float maxForce = 0.1f;

    Boid(float x, float y) {
        this.x = x;
        this.y = y;
        this.vx = new Random().nextFloat() * 2 - 1;
        this.vy = new Random().nextFloat() * 2 - 1;
    }

    void update(ArrayList<Boid> boids) {
        flock(boids);
        x += vx;
        y += vy;
        edges();
    }

    void flock(ArrayList<Boid> boids) {
        Point sep = separate(boids);
        Point ali = align(boids);
        Point coh = cohesion(boids);

        sep.setLocation(sep.getX() * 1.5, sep.getY() * 1.5);
        ali.setLocation(ali.getX() * 1.0, ali.getY() * 1.0);
        coh.setLocation(coh.getX() * 1.0, coh.getY() * 1.0);

        applyForce(sep);
        applyForce(ali);
        applyForce(coh);
    }

    private void applyForce(Point force) {
        vx += force.x;
        vy += force.y;
        limitSpeed();
    }

    private void limitSpeed() {
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
    }

    private Point separate(ArrayList<Boid> boids) {
        float desiredSeparation = 25;
        Point steer = new Point(0, 0);
        int count = 0;

        for (Boid other : boids) {
            float d = (float) Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
            if (d > 0 && d < desiredSeparation) {
                float diffX = x - other.x;
                float diffY = y - other.y;
                diffX /= d;
                diffY /= d;
                steer.translate((int) diffX, (int) diffY);
                count++;
            }
        }

        if (count > 0) {
            steer.setLocation(steer.getX() / count, steer.getY() / count);
        }

        return steer;
    }

    private Point align(ArrayList<Boid> boids) {
        float neighborDist = 50;
        Point sum = new Point(0, 0);
        int count = 0;

        for (Boid other : boids) {
            float d = (float) Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
            if (d > 0 && d < neighborDist) {
                sum.translate((int) other.vx, (int) other.vy);
                count++;
            }
        }

        if (count > 0) {
            sum.setLocation(sum.getX() / count, sum.getY() / count);
            sum.setLocation(sum.getX() * maxSpeed, sum.getY() * maxSpeed);
            Point steer = new Point((int) (sum.getX() - vx), (int) (sum.getY() - vy));
            return limitForce(steer);
        } else {
            return new Point(0, 0);
        }
    }

    private Point cohesion(ArrayList<Boid> boids) {
        float neighborDist = 50;
        Point sum = new Point(0, 0);
        int count = 0;

        for (Boid other : boids) {
            float d = (float) Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
            if (d > 0 && d < neighborDist) {
                sum.translate((int) other.x, (int) other.y);
                count++;
            }
        }

        if (count > 0) {
            sum.setLocation(sum.getX() / count, sum.getY() / count);
            return seek(sum);
        } else {
            return new Point(0, 0);
        }
    }

    private Point seek(Point target) {
        Point desired = new Point((int) (target.getX() - x), (int) (target.getY() - y));
        float d = (float) Math.sqrt(desired.getX() * desired.getX() + desired.getY() * desired.getY());
        desired.setLocation(desired.getX() * maxSpeed / d, desired.getY() * maxSpeed / d);
        Point steer = new Point((int) (desired.getX() - vx), (int) (desired.getY() - vy));
        return limitForce(steer);
    }

    private Point limitForce(Point force) {
        float mag = (float) Math.sqrt(force.getX() * force.getX() + force.getY() * force.getY());
        if (mag > maxForce) {
            force.setLocation(force.getX() * maxForce / mag, force.getY() * maxForce / mag);
        }
        return force;
    }

    private void edges() {
        if (x > 800) x = 0;
        else if (x < 0) x = 800;
        if (y > 600) y = 0;
        else if (y < 0) y = 600;
    }

    void display(Graphics2D g2d) {
        float theta = (float) (Math.atan2(vy, vx) + Math.PI / 2);
        g2d.setColor(Color.BLACK); // Set color to black
        g2d.setStroke(new BasicStroke(1));
        g2d.translate(x, y);
        g2d.rotate(theta);
        int[] xPoints = {0, -5, 5};
        int[] yPoints = {-8, 8, 8};
        g2d.fillPolygon(xPoints, yPoints, 3); // Fill the polygon with black color
        g2d.rotate(-theta);
        g2d.translate(-x, -y);
    }
    
}
