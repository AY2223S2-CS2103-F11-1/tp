package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import java.util.HashSet;

/**
 * Sorts students accordingly.
 */
public class SortStudentCommand extends Command {
    public static final String COMMAND_WORD = "sort-student";

    public static final String MESSAGE_SUCCESS = "Listed all persons in sorted order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all address book students.\n "
            + "Parameters: The group of students you wish to sort (either all, lab, tutorial or consultation), "
            + "the metric to be sorted (either address, email, name, performance or remark), "
            + "and the desired order (either reverse or nonreverse)\n"
            + "For example: 'sort-student all name reverse' command will order all students in reverse-alphabetical "
            + "ordering of their names";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Sort command not implemented yet";
    private static HashSet<String> validMetrics;
    private static String group;
    private static String metric;
    private static boolean isIncreasing;

    /**
     * Constructor for SortStudentCommand
     * @param group The group of students to be sorted
     * @param metric The metric to be used for the sorting
     * @param isIncreasing The desired order to sort the students
     */
    public SortStudentCommand(String group, String metric, boolean isIncreasing) {
        this.group = group;
        this.metric = metric;
        this.isIncreasing = isIncreasing;
        this.validMetrics = new HashSet<>();
        this.validMetrics.add("address");
        this.validMetrics.add("email");
        this.validMetrics.add("name");
        this.validMetrics.add("remark");
        this.validMetrics.add("performance");
    }

    /**
     * Getter method for the group
     * @return the group input by the user
     */
    private String getGroup() {
        return this.group;
    }

    /**
     * Getter method for the metric
     * @return the metric input by the user
     */
    private String getMetric() {
        return this.metric;
    }

    /**
     * Getter method for whether the order is increasing
     * @return true if the order is increasing else false
     */
    private boolean getIsIncreasing() {
        return this.isIncreasing;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        /*
        switch (this.group) {
        case "all":
            model.updateSortAllPersonList(this.metric, this.increasingOrder);
            break;
        case "lab":
            model.updateSortLabPersonList(this.metric, this.increasingOrder);
            break;
        case "tutorial":
            model.updateSortTutorialPersonList(this.metric, this.increasingOrder);
            break;
        case "consultation":
            model.updateSortConsultationPersonList(this.metric, this.increasingOrder);
            break;
        }

         */
        if (!validMetrics.contains(metric)) {
            throw new CommandException(Messages.MESSAGE_RESTRICTED_VALUE);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateSortAllPersonList(this.metric, this.isIncreasing);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
