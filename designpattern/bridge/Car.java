package bridge;

public class Car extends Vehicle {

    public Car(Engine engine) {
        super(engine);
    }

    @Override
    public void drive() {
        System.out.println("Car (" + engine.getType() + ") — driving on city roads:");
        engine.start();
        System.out.println("  [Car]           Shifting gears... cruising at 80 km/h.");
        engine.stop();
    }

    @Override
    public String getVehicleType() { return "Car"; }
}
