package bridge;

public class Truck extends Vehicle {

    public Truck(Engine engine) {
        super(engine);
    }

    @Override
    public void drive() {
        System.out.println("Truck (" + engine.getType() + ") — hauling cargo on highway:");
        engine.start();
        System.out.println("  [Truck]         Engaging turbo... rolling at 60 km/h with full load.");
        engine.stop();
    }

    @Override
    public String getVehicleType() { return "Truck"; }
}
