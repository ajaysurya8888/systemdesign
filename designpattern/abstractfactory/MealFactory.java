package abstractfactory;

import java.util.List;

public interface MealFactory {
    MainDish createMainDish();
    List<SideDish> createSideDishes();
    String getMealName();
}