package abstractfactory.dishes;

import abstractfactory.MainDish;

public class Poori implements MainDish {

    @Override
    public String getName() { return "Poori"; }

    @Override
    public void prepare() {
        System.out.println("  Deep frying golden puffed Pooris from wheat dough...");
    }
}