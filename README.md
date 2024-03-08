# boids
An implementation of the Boids flocking-simulation algorithm

This program simulates the flocking behavior of birds in nature. Our birds, "boids" follow a simple set of rules:

* Separation: boids avoid other boids that are too close
* Alignment: boids try to match neighbors' velocity
* Cohesion: boids move towards their neighbors' center of mass