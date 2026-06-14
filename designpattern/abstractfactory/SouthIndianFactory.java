package abstractfactory;

import abstractfactory.dishes.Chutney;
import abstractfactory.dishes.Idly;
import abstractfactory.dishes.Sambhar;

import java.util.Arrays;
import java.util.List;

public class SouthIndianFactory implements MealFactory {

    @Override
    public MainDish createMainDish() {
        return new Idly();
    }

    @Override
    public List<SideDish> createSideDishes() {
        return Arrays.asList(new Chutney(), new Sambhar());
    }

    @Override
    public String getMealName() { return "South Indian Meal"; }
}