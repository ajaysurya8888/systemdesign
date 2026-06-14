package abstractfactory;

import abstractfactory.dishes.Biriyani;
import abstractfactory.dishes.Raitha;

import java.util.Collections;
import java.util.List;

public class BiryaniFactory implements MealFactory {

    @Override
    public MainDish createMainDish() {
        return new Biriyani();
    }

    @Override
    public List<SideDish> createSideDishes() {
        return Collections.singletonList(new Raitha());
    }

    @Override
    public String getMealName() { return "Biryani Meal"; }
}