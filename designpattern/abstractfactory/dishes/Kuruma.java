package abstractfactory.dishes;

import abstractfactory.SideDish;

public class Kuruma implements SideDish {

    @Override
    public String getName() { return "Vegetable Kuruma"; }

    @Override
    public void prepare() {
        System.out.println("  Simmering mixed vegetables in coconut-cashew gravy for Kuruma...");
    }
}