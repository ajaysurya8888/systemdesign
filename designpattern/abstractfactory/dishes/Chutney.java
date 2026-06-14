package abstractfactory.dishes;

import abstractfactory.SideDish;

public class Chutney implements SideDish {

    @Override
    public String getName() { return "Coconut Chutney"; }

    @Override
    public void prepare() {
        System.out.println("  Grinding fresh coconut with green chillies and mustard for Chutney...");
    }
}