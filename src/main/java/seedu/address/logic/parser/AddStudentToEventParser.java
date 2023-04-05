package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONSULTATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddStudentToEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments while checking for validity of the user input,
 * and creates a new AddStudentToEventCommand object.
 */
public class AddStudentToEventParser implements Parser<AddStudentToEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddStudentToEventCommand
     * and returns an AddStudentToEventCommand object for execution.
     *
     * @param args the given string arguments to be parsed.
     * @return an AddStudentToEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddStudentToEventCommand parse(String args) throws ParseException {
        Index studentIndex;
        Index eventIndex;
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TUTORIAL, PREFIX_LAB, PREFIX_CONSULTATION);

        Optional<String> tutorialName = argMultimap.getValue(PREFIX_TUTORIAL);
        Optional<String> labName = argMultimap.getValue(PREFIX_LAB);
        Optional<String> consultationName = argMultimap.getValue(PREFIX_CONSULTATION);

        if (tutorialName.isEmpty() && labName.isEmpty() && consultationName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddStudentToEventCommand.MESSAGE_USAGE));
        }

        try {
            studentIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
            eventIndex = ParserUtil.parseIndex(
                    tutorialName.orElse(labName.orElse(consultationName.orElse(""))));
        } catch (ParseException pe) {
            // index not non-zero integer
            throw pe;
        }

        String eventType = PREFIX_TUTORIAL.getPrefix();
        if (!labName.isEmpty()) {
            eventType = PREFIX_LAB.getPrefix();
        }
        if (!consultationName.isEmpty()) {
            eventType = PREFIX_CONSULTATION.getPrefix();
        }

        return new AddStudentToEventCommand(studentIndex, eventIndex, eventType);
    }
}
