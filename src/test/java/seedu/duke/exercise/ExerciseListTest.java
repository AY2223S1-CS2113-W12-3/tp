package seedu.duke.exercise;

import org.junit.jupiter.api.Test;
import seedu.duke.Parser;
import seedu.duke.Ui;
import seedu.duke.biometrics.Biometrics;
import seedu.duke.command.Command;
import seedu.duke.exception.IllegalValueException;
import seedu.duke.food.FoodList;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ExerciseListTest {
    private final Ui ui = new Ui();
    private final Biometrics biometrics = new Biometrics();

    private FoodList foodList = new FoodList();

    @Test
    void getCompletedExercise_outOfBoundIndex_exceptionThrown() {
        ExerciseList exerciseList = new ExerciseList();
        try {
            addExercise(exerciseList);
        } catch (IllegalValueException e) {
            fail();
        }
        int completedExerciseListSize = exerciseList.getCompletedExerciseListSize();
        try {
            exerciseList.getCompletedExercise(completedExerciseListSize);
            fail();
        } catch (IllegalValueException e) {
            assertEquals("Exercise not found", e.getMessage());
        }
        try {
            exerciseList.getCompletedExercise(-1);
            fail();
        } catch (IllegalValueException e) {
            assertEquals("Exercise not found", e.getMessage());
        }
    }

    @Test
    void getCurrentExercise_outOfBoundIndex_exceptionThrown() {
        ExerciseList exerciseList = new ExerciseList();
        try {
            addExercise(exerciseList);
        } catch (IllegalValueException e) {
            fail();
        }
        int currentExerciseListSize = exerciseList.getCurrentExerciseListSize();
        try {
            exerciseList.getCurrentExercise(currentExerciseListSize);
            fail();
        } catch (IllegalValueException e) {
            assertEquals("Exercise not found", e.getMessage());
        }
        try {
            exerciseList.getCurrentExercise(-1);
            fail();
        } catch (IllegalValueException e) {
            assertEquals("Exercise not found", e.getMessage());
        }
    }

    private void addExercise(ExerciseList exerciseList) throws IllegalValueException {
        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("add exercise bench 10 180");
        commandList.add("add exercise press 8 58");
        commandList.add("add exercise deadlift 6 120");

        for (String input : commandList) {
            Command c = Parser.parse(input);
            c.setData(ui, biometrics, exerciseList, foodList);
            c.execute();
        }
    }

}