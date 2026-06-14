package adapter;

// Adaptee — existing system that only speaks pounds (incompatible interface)
public class WeightInPound {

    private final double weightInPound;

    public WeightInPound(double weightInPound) {
        this.weightInPound = weightInPound;
    }

    public double getWeightInPound() {
        return weightInPound;
    }
}