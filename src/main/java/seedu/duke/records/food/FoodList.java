package seedu.duke.records.food;

import seedu.duke.exception.IllegalValueException;

import java.util.ArrayList;

import static seedu.duke.logic.command.DateCommand.sortDateForFood;

public class FoodList {
    ArrayList<Food> foodList;

    public FoodList() {
        this.foodList = new ArrayList<>();
    }

    public void addFood(Food food) {
        foodList.add(food);
    }

    public Food getFood(int index) throws IllegalValueException {
        if (index < 0 || index >= getFoodListSize()) {
            throw new IllegalValueException("Please provide a valid index for food");
        }
        return foodList.get(index);
    }

    public void removeFood(int index) throws IllegalValueException {
        if (index < 0 || index >= getFoodListSize()) {
            throw new IllegalValueException("Please provide a valid index for food");
        }
        foodList.remove(index);
    }


    public ArrayList<Food> getFoodList() {
        sortDateForFood(foodList);
        return foodList;
    }

    public int getFoodListSize() {
        return foodList.size();
    }

}