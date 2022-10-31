package seedu.duke;

import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.GreetCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.InvalidCommand;
import seedu.duke.command.MarkCommand;
import seedu.duke.command.RemoveCommand;
import seedu.duke.command.SetCommand;
import seedu.duke.command.ViewCommand;
import seedu.duke.exception.IllegalValueException;
import seedu.duke.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

;

public class Parser {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Command parse(String input) {
        String userCommand = getUserCommand(input);
        LOGGER.info("command: " + userCommand);
        String arguments = getArgumentSubString(input, userCommand);
        LOGGER.info("arguments:" + arguments);
        switch (userCommand) {
        case "greet":
            return new GreetCommand();
        case "exit":
            return new ExitCommand();
        case "set":
            return new SetCommand(arguments, true);
        case "add":
            return new AddCommand(arguments, true, false);
        case "view":
            return new ViewCommand(arguments);
        case "mark":
            return new MarkCommand(arguments);
        case "remove":
            return new RemoveCommand(arguments);
        case "find":
            return new FindCommand(arguments);
        case "help":
            return new HelpCommand();
        default:
            return new InvalidCommand();
        }
    }

    private static String getUserCommand(String input) {
        String userCommand = input.split(" ")[0];
        return userCommand;
    }

    private static String getArgumentSubString(String input, String userCommand) {
        return input.substring(userCommand.length()).trim();
    }

    public static String[] getArgumentList(String arguments) {
        assert (arguments != null);
        String[] argumentList = arguments.toLowerCase().split("\\s*/\\s*");
        return argumentList;
    }

    public static String getClassType(String[] argumentList) {
        return argumentList[0];
    }

    public static String getDateNoDateTracker(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(input, formatter);
        return formatter.format(localDate);
    }

    public static LocalDate parseDate(String input, int monthsToAdd) throws IllegalValueException {
        try {
            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("d-M-yyyy"));
            if (date.isAfter(LocalDate.now().plusMonths(monthsToAdd))) {
                throw new IllegalValueException("Date is too far in the future");
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Date is in the wrong format. Please follow the dd-MM-yyyy format");
        }
    }
}
