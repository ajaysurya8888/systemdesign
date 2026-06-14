package abstractfactory;

import java.util.Arrays;
import java.util.List;

public class Main {

    static void serveMeal(MealFactory factory) {
        System.out.println("=== " + factory.getMealName() + " ===");

        MainDish main = factory.createMainDish();
        List<SideDish> sides = factory.createSideDishes();

        System.out.println("Main dish  : " + main.getName());
        main.prepare();

        for (SideDish side : sides) {
            System.out.println("Side dish  : " + side.getName());
            side.prepare();
        }
        System.out.println();
    }

    public static void main(String[] args) {

        List<MealFactory> meals = Arrays.asList(
                new SouthIndianFactory(),
                new NorthIndianFactory(),
                new BiryaniFactory()
        );

        for (MealFactory factory : meals) {
            serveMeal(factory);
        }
    }
}