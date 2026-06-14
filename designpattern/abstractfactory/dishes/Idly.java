package abstractfactory.dishes;

import abstractfactory.MainDish;

public class Idly implements MainDish {

    @Override
    public String getName() { return "Idly"; }

    @Override
    public void prepare() {
        System.out.println("  Steaming fluffy Idlies from fermented rice-urad batter...");
    }
}