package bridge;

public class PetrolEngine implements Engine {

    @Override
    public void start() {
        System.out.println("  [PetrolEngine]  Igniting fuel injection... Vroom!");
    }

    @Override
    public void stop() {
        System.out.println("  [PetrolEngine]  Cutting fuel supply. Engine off.");
    }

    @Override
    public String getType() { return "Petrol"; }
}
