package bridge;

// Abstraction — holds a bridge reference to Engine (implementor)
public abstract class Vehicle {

    protected final Engine engine;   // bridge

    public Vehicle(Engine engine) {
        this.engine = engine;
    }

    public abstract void drive();
    public abstract String getVehicleType();
}