package abstractfactory.dishes;

import abstractfactory.SideDish;

public class Raitha implements SideDish {

    @Override
    public String getName() { return "Onion Raitha"; }

    @Override
    public void prepare() {
        System.out.println("  Mixing chilled yogurt with sliced onions and coriander for Raitha...");
    }
}