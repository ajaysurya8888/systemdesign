package decorator;

public class Main {

    public static void main(String[] args) {
        // Plain pizza
        Pizza pizza = new PlainPizza();
        printOrder(pizza);

        // Plain + Cheese
        pizza = new CheeseTopping(new PlainPizza());
        printOrder(pizza);

        // Plain + Cheese + Pepperoni
        pizza = new PepperoniTopping(new CheeseTopping(new PlainPizza()));
        printOrder(pizza);

        // Plain + Cheese + Mushroom + Pepperoni (stack all three)
        pizza = new PepperoniTopping(new MushroomTopping(new CheeseTopping(new PlainPizza())));
        printOrder(pizza);
    }

    private static void printOrder(Pizza pizza) {
        System.out.printf("%-45s $%.2f%n", pizza.getDescription(), pizza.getCost());
    }
}
