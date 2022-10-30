package seedu.duke.command;

import seedu.duke.Parser;
import seedu.duke.Ui;
import seedu.duke.exception.IllegalValueException;
import seedu.duke.records.RecordList;
import seedu.duke.records.biometrics.Biometrics;
import seedu.duke.records.biometrics.WeightAndFat;
import seedu.duke.records.exercise.CardioExercise;
import seedu.duke.records.exercise.Exercise;
import seedu.duke.records.exercise.ExerciseList;
import seedu.duke.records.exercise.StrengthExercise;
import seedu.duke.records.food.Food;
import seedu.duke.records.food.FoodList;
import seedu.duke.storage.Storage;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCommand extends Command {
    private final boolean isMarkDone;
    private Ui ui;
    private String arguments;
    private boolean toDisplay;
    private Food food;
    public static final String INVALID_FOOD_INPUT = "Invalid food input";
    final String[] invalidFoodNames = {"", " ", "[]\\[;]"};

    private ExerciseList exerciseList;

    private FoodList foodList;

    private Biometrics biometrics;
    private RecordList recordList;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public AddCommand(String arguments, boolean toDisplay, boolean isMarkDone) {
        this.arguments = arguments;
        this.toDisplay = toDisplay;
        this.isMarkDone = isMarkDone;
    }

    @Override
    public void execute() throws IllegalValueException {
        String[] argumentList = Parser.getArgumentList(arguments);
        String addType = Parser.getClassType(argumentList);
        switch (addType) {
        case ("food"):
            addFood(argumentList);
            break;
        case ("strength"):
            addStrengthExercise(argumentList);
            break;
        case ("cardio"):
            addCardioExercise(argumentList);
            break;
        case ("weight"):
            addWeightAndFat(argumentList);
            break;
        default:
            handleInvalidAddType();
        }
    }

    private void addStrengthExercise(String[] argumentList) throws IllegalValueException {
        if (!toDisplay && argumentList.length != 8) {
            LOGGER.warning("Invalid arguments for loading strength exercise");
            throw new IllegalValueException("Unable to load strength exercise");
        } else if (toDisplay && (argumentList.length < 5 || argumentList.length > 6)) {
            LOGGER.warning("Invalid arguments length for add strength exercise");
            throw new IllegalValueException("Invalid add strength exercise command");
        }
        String description = getDescriptionWithValidation(argumentList[1]);
        try {
            int weight = getWeightWithValidation(argumentList);
            int set = getSetWithValidation(argumentList);
            int repetition = getRepetitionWithValidation(argumentList);
            LocalDate date;
            if (argumentList.length == 5) {
                date = LocalDate.now();
            } else {
                date = getDateWithValidation(argumentList[5], toDisplay);
            }
            Exercise exercise = new StrengthExercise(description, weight, set, repetition, date);
            exerciseList.addExercise(exercise);
            assert (exerciseList.getCurrentExercise(exerciseList.getCurrentExerciseListSize() - 1)
                    .equals(exercise)) : "Exercise not added properly";
            if (isMarkDone) {
                double time = Double.parseDouble(argumentList[6]);
                int calories = Integer.parseInt(argumentList[7]);
                exerciseList.markDone(exerciseList.getCurrentExerciseListSize() - 1, time, calories);
            }
            if (toDisplay) {
                ui.output(exercise.toString());
                ui.output(" This strength exercise is added to the exercise list successfully");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error converting string to integer", e);
            throw new IllegalValueException("Set and repetition must be integers");
        }
    }

    private static LocalDate getDateWithValidation(String date, boolean toDisplay) throws IllegalValueException {
        LocalDate today = LocalDate.now();
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate oldestinputDate =  LocalDate.parse("31-12-2023", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (toDisplay && today.isAfter(localDate)) {
                throw new IllegalValueException("Date cannot be before today");
            } else if (localDate.compareTo(oldestinputDate) > 0) {
                throw new IllegalValueException("Date input cannot be after 2023");
            }
        } catch (DateTimeException e) {
            throw new IllegalValueException("Date is in the wrong format. Please follow the dd-MM-yyyy format");
        }
        return localDate;
    }

    private static int getSetWithValidation(String[] argumentList) throws IllegalValueException {
        int set = Integer.parseInt(argumentList[3]);
        if (set <= 0 || set > 500) {
            throw new IllegalValueException("Invalid value for sets");
        }
        return set;
    }

    private static int getWeightWithValidation(String[] argumentList) throws IllegalValueException {
        int weight = Integer.parseInt(argumentList[2]);
        if (weight < 0 || weight > 1000) {
            throw new IllegalValueException("Invalid number for weight");
        }
        return weight;
    }

    private static String getDescriptionWithValidation(String description) throws IllegalValueException {
        if (description.length() > 50) {
            throw new IllegalValueException("Description of the exercise must be within 50 characters");
        }
        return description;
    }

    private void addCardioExercise(String[] argumentList) throws IllegalValueException {
        if (!toDisplay && argumentList.length != 7) {
            LOGGER.warning("Invalid arguments for loading cardio exercise");
            throw new IllegalValueException("Unable to load cardio exercise");
        } else if (toDisplay && (argumentList.length < 4 || argumentList.length > 5)) {
            LOGGER.warning("Invalid arguments length for add cardio exercise");
            throw new IllegalValueException("Invalid add cardio exercise command");
        }
        String description = getDescriptionWithValidation(argumentList[1]);
        try {
            double distance = getDistanceWithValidation(Double.parseDouble(argumentList[2]));
            int repetition = getRepsWithValidation(argumentList);
            LocalDate date;
            if (argumentList.length == 4) {
                date = LocalDate.now();
            } else {
                date = getDateWithValidation(argumentList[4], toDisplay);
            }
            Exercise exercise = new CardioExercise(description, distance, repetition, date);
            exerciseList.addExercise(exercise);
            assert (exerciseList.getCurrentExercise(exerciseList.getCurrentExerciseListSize() - 1)
                    .equals(exercise)) : "Exercise not added properly";
            if (isMarkDone) {
                double time = Double.parseDouble(argumentList[5]);
                int calories = Integer.parseInt(argumentList[6]);
                exerciseList.markDone(exerciseList.getCurrentExerciseListSize() - 1, time, calories);
            }
            if (toDisplay) {
                ui.output(exercise.toString());
                ui.output(" This cardio exercise is added to the exercise list successfully");
            }
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Distance and repetition must be numbers");
        }
    }

    private int getRepsWithValidation(String[] argumentList) throws IllegalValueException {
        int repetition = Integer.parseInt(argumentList[3]);
        if (repetition <= 0 || repetition > 50) {
            throw new IllegalValueException("Invalid value for repetitions");
        }
        return repetition;
    }

    private static double getDistanceWithValidation(double distance) throws IllegalValueException {
        if (distance > 100) {
            throw new IllegalValueException("Distance should not be more than 100km!");
        } else if (distance < 0) {
            throw new IllegalValueException("Distance cannot be negative!");
        }
        return distance;
    }

    private static int getRepetitionWithValidation(String[] argumentList) throws IllegalValueException {
        int repetition = Integer.parseInt(argumentList[4]);
        if (repetition <= 0 || repetition > 50) {
            throw new IllegalValueException("Invalid value for repetitions");
        }
        return repetition;
    }

    private void handleInvalidAddType() throws IllegalValueException {
        throw new IllegalValueException("Invalid add command");
    }


    private void addFood(String[] argumentList) throws IllegalValueException {
        try {
            if (argumentList.length < 3) {
                throw new IllegalValueException(INVALID_FOOD_INPUT);
            }
            LocalDate date;
            if (argumentList.length == 4) {
                date = LocalDate.parse(argumentList[3], DateTimeFormatter.ofPattern("d-M-yyyy"));
            } else {
                date = LocalDate.now();
            }
            String description = extractFoodName(argumentList[1]);
            int calories = extractCalories(argumentList[2]);
            food = new Food(description, calories, date);
            foodList.addFood(food);
            assert foodList.getFood(foodList.getFoodListSize() - 1).equals(food) : "Food added properly";
            if (toDisplay) {
                ui.output(food.toString());
                ui.output(" This food is added to the food list successfully");
            }
        } catch (NumberFormatException e) {
            throw new IllegalValueException(INVALID_FOOD_INPUT);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Date should be in the format dd-mm-yyyy");
        }
    }


    private void addWeightAndFat(String[] argumentList) throws IllegalValueException {
        if (argumentList.length < 3) {
            throw new IllegalValueException("INVALID_WEIGHT_INPUT");
        }
        try {
            int weight = Integer.parseInt(argumentList[1]);
            int fat = Integer.parseInt(argumentList[2]);
            LocalDate date;
            if (argumentList.length == 4) {
                date = LocalDate.parse(argumentList[3], DateTimeFormatter.ofPattern("d-M-yyyy"));
            } else {
                date = LocalDate.now();
            }
            WeightAndFat weightAndFat = new WeightAndFat(weight, fat, date);
            biometrics.weightAndFatList.addWeightAndFat(weightAndFat);
            biometrics.setWeight(weight);
            biometrics.setFat(fat);
            if (toDisplay) {
                ui.output(weightAndFat.toString());
                ui.output(" Weight and fat percentage are recorded successfully");
            }
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Weight and fat percentage should be numerical");
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Date should be in the format dd-mm-yyyy");
        }
    }

    private String extractFoodName(String input) throws IllegalValueException {
        if (Arrays.asList(invalidFoodNames).contains(input)) {
            throw new IllegalValueException("Please provide valid food description inputs!");
        }
        return input;
    }

    private int extractCalories(String input) throws IllegalValueException {
        int calories = Integer.parseInt(input);
        if (calories <= 0) {
            throw new IllegalValueException("Calories inputs need to be positive integer values!");
        }
        return calories;
    }

    @Override
    public void setData(Ui ui, Storage storage, Biometrics biometrics, ExerciseList exerciseList, FoodList foodList,
                        RecordList recordList) {
        this.ui = ui;
        this.exerciseList = exerciseList;
        this.foodList = foodList;
        this.biometrics = biometrics;
        this.recordList = recordList;
    }
}