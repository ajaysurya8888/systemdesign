package abstractfactory.dishes;

import abstractfactory.MainDish;

public class Biriyani implements MainDish {

    @Override
    public String getName() { return "Biriyani"; }

    @Override
    public void prepare() {
        System.out.println("  Slow-cooking aromatic Biriyani with basmati rice and spices (Dum style)...");
    }
}