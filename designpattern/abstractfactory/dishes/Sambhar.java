package abstractfactory.dishes;

import abstractfactory.SideDish;

public class Sambhar implements SideDish {

    @Override
    public String getName() { return "Sambhar"; }

    @Override
    public void prepare() {
        System.out.println("  Boiling toor dal with tamarind, tomatoes and drumstick for Sambhar...");
    }
}