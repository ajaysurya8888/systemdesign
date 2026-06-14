package abstractfactory;

import abstractfactory.dishes.Kuruma;
import abstractfactory.dishes.Poori;

import java.util.Collections;
import java.util.List;

public class NorthIndianFactory implements MealFactory {

    @Override
    public MainDish createMainDish() {
        return new Poori();
    }

    @Override
    public List<SideDish> createSideDishes() {
        return Collections.singletonList(new Kuruma());
    }

    @Override
    public String getMealName() { return "North Indian Meal"; }
}