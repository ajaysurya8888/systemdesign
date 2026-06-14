package bridge;

public class Bike extends Vehicle {

    public Bike(Engine engine) {
        super(engine);
    }

    @Override
    public void drive() {
        System.out.println("Bike (" + engine.getType() + ") — zipping through traffic:");
        engine.start();
        System.out.println("  [Bike]          Throttling up... weaving at 60 km/h.");
        engine.stop();
    }

    @Override
    public String getVehicleType() { return "Bike"; }
}
