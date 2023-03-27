package seedu.address.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    private static int start = 0;
    private static int end = 0;
    public Messages(int start, int end) {
        this.start = start;
        this.end = end;
        MESSAGE_INVALID_VALUE = String.format("Integer given must be between %d and %d", start, end);
    }
    public static String MESSAGE_INVALID_VALUE = String.format("Integer given must be between %d and %d",
            start, end);
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_EVENT_DISPLAYED_INDEX = "The event index provided is invalid";
    public static final String MESSAGE_RESTRICTED_VALUE = "Invalid command! There are only certain inputs you can give";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";

}
