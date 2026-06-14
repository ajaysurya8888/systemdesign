package bridge;

public class ElectricEngine implements Engine {

    @Override
    public void start() {
        System.out.println("  [ElectricEngine] Charging motor... Silent hum.");
    }

    @Override
    public void stop() {
        System.out.println("  [ElectricEngine] Disconnecting motor. Silently off.");
    }

    @Override
    public String getType() { return "Electric"; }
}
