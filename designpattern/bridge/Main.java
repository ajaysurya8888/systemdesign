package bridge;

public class Main {

    public static void main(String[] args) {

        // Engines (Implementors)
        Engine petrol   = new PetrolEngine();
        Engine diesel   = new DieselEngine();
        Engine electric = new ElectricEngine();

        System.out.println("========== Car with different engines ==========");
        new Car(petrol).drive();
        System.out.println();
        new Car(diesel).drive();
        System.out.println();
        new Car(electric).drive();

        System.out.println("\n========== Truck with different engines ==========");
        new Truck(diesel).drive();
        System.out.println();
        new Truck(electric).drive();

        System.out.println("\n========== Bike with different engines ==========");
        new Bike(petrol).drive();
        System.out.println();
        new Bike(electric).drive();

        System.out.println("\n========== Swap engine at runtime ==========");
        System.out.println("Upgrading Car from Petrol to Electric:");
        Vehicle upgradedCar = new Car(electric);
        upgradedCar.drive();
    }
}
