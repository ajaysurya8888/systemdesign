package bridge;

public class DieselEngine implements Engine {

    @Override
    public void start() {
        System.out.println("  [DieselEngine]  Compressing air... Rumble rumble!");
    }

    @Override
    public void stop() {
        System.out.println("  [DieselEngine]  Stopping compression. Engine off.");
    }

    @Override
    public String getType() { return "Diesel"; }
}
