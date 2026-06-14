package adapter;

// Adapter — wraps WeightInPound and exposes WeightInKG interface
public class WeightAdapter implements WeightInKG {

    private static final double POUND_TO_KG = 0.453592;

    private final WeightInPound weightInPound;

    public WeightAdapter(WeightInPound weightInPound) {
        this.weightInPound = weightInPound;
    }

    @Override
    public double getWeightInKG() {
        double pounds = weightInPound.getWeightInPound();
        double kg     = pounds * POUND_TO_KG;
        System.out.printf("  [Adapter] Converting %.2f lbs → %.4f kg%n", pounds, kg);
        return kg;
    }
}