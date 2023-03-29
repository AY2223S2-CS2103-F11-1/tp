package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONSULTATIONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_LABS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TUTORIALS;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;
import seedu.address.model.event.Event;
import seedu.address.model.event.Lab;
import seedu.address.model.event.Note;
import seedu.address.model.event.Tutorial;
import seedu.address.model.person.Person;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "editEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the event identified "
            + "by the index number used in the displayed event list. ";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Event: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the address book.";

    private final Index index;
    private EditEventDescriptor editEventDescriptor;
    private final boolean isTutorial;
    private final boolean isLab;
    private final boolean isConsultation;

    /**
     * Has parameters to pass data on whether the event is a tutorial, a lab or consultation for type casting
     * @param index
     * @param editEventDescriptor
     * @param isTutorial
     * @param isLab
     * @param isConsultation
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor, boolean isTutorial,
                            boolean isLab, boolean isConsultation) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.isTutorial = isTutorial;
        this.isLab = isLab;
        this.isConsultation = isConsultation;
        this.editEventDescriptor = editEventDescriptor;
    }

    /**
     * Executes model by typccasing depending on whether it is a tutorial, lab or consultation
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult
     * @throws CommandException
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (isTutorial) {
            return executeTutorial(model);
        } else if (isLab) {
            return executeLab(model);
        } else {
            return executeConsultation(model);
        }
    }

    /**
     * Executes the set tutorial with model
     * @param model
     * @return CommandResult
     * @throws CommandException
     */
    public CommandResult executeTutorial(Model model) throws CommandException {
        List<Tutorial> lastShownList = model.getFilteredTutorialList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Tutorial tutorialToEdit = lastShownList.get(index.getZeroBased());
        Tutorial editedTutorial = createEditedTutorial(tutorialToEdit, editEventDescriptor);

        if (!tutorialToEdit.isSameTutorial(editedTutorial) && model.hasTutorial(editedTutorial)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setTutorial(tutorialToEdit, editedTutorial);
        model.updateFilteredTutorialList(PREDICATE_SHOW_ALL_TUTORIALS);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedTutorial),
                false, false, false, true);
    }

    /**
     * Executes the set lab with model
     * @param model
     * @return CommandResult
     * @throws CommandException
     */
    public CommandResult executeLab(Model model) throws CommandException {
        List<Lab> lastShownList = model.getFilteredLabList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Lab labToEdit = lastShownList.get(index.getZeroBased());
        Lab editedLab = createEditedLab(labToEdit, editEventDescriptor);

        if (!labToEdit.isSameLab(editedLab) && model.hasLab(editedLab)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setLab(labToEdit, editedLab);
        model.updateFilteredLabList(PREDICATE_SHOW_ALL_LABS);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedLab), false, false, false, true);
    }

    /**
     * Executes the set consultation with model
     * @param model
     * @return CommandResult
     * @throws CommandException
     */
    public CommandResult executeConsultation(Model model) throws CommandException {
        List<Consultation> lastShownList = model.getFilteredConsultationList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Consultation consultationToEdit = lastShownList.get(index.getZeroBased());
        Consultation editedConsultation = createEditedConsultation(consultationToEdit, editEventDescriptor);

        if (!consultationToEdit.isSameConsultation(editedConsultation)
                && model.hasConsultation(editedConsultation)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setConsultation(consultationToEdit, editedConsultation);
        model.updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedConsultation),
                false, false, false, true);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Tutorial createEditedTutorial(Event eventToEdit,
                                            EditEventCommand.EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        String updatedEventName = editEventDescriptor.getEventName().orElse(eventToEdit.getName());
        LocalDateTime updatedDate = editEventDescriptor.getDate().orElse(eventToEdit.getDate());
        List<Person> sameStudents = eventToEdit.getStudents();
        List<File> updatedAttachments = editEventDescriptor.getAttachment().orElse(eventToEdit.getAttachments());
        List<Note> sameNotes = eventToEdit.getNotes();
        return new Tutorial(updatedEventName, updatedDate, sameStudents,
                updatedAttachments, sameNotes);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Lab createEditedLab(Event eventToEdit,
                                           EditEventCommand.EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        String updatedEventName = editEventDescriptor.getEventName().orElse(eventToEdit.getName());
        LocalDateTime updatedDate = editEventDescriptor.getDate().orElse(eventToEdit.getDate());
        List<Person> sameStudents = eventToEdit.getStudents();
        List<File> updatedAttachments = editEventDescriptor.getAttachment().orElse(eventToEdit.getAttachments());
        List<Note> sameNotes = eventToEdit.getNotes();
        return new Lab(updatedEventName, updatedDate, sameStudents,
                updatedAttachments, sameNotes);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Consultation createEditedConsultation(Event eventToEdit,
                                           EditEventCommand.EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        String updatedEventName = editEventDescriptor.getEventName().orElse(eventToEdit.getName());
        LocalDateTime updatedDate = editEventDescriptor.getDate().orElse(eventToEdit.getDate());
        List<Person> sameStudents = eventToEdit.getStudents();
        List<Note> sameNotes = eventToEdit.getNotes();
        return new Consultation(updatedEventName, sameStudents, sameNotes, updatedDate);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        // state check
        EditEventCommand e = (EditEventCommand) other;
        return index.equals(e.index)
                && editEventDescriptor.equals(e.editEventDescriptor);
    }


    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditEventDescriptor {
        private String name;
        private LocalDateTime eventDate;
        private List<Person> students;
        private List<Note> notes;
        private List<File> attachments = new ArrayList<>();

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditEventDescriptor(EditEventCommand.EditEventDescriptor toCopy) {
            setEventName(toCopy.name);
            setDate(toCopy.eventDate);
            setStudents(toCopy.students);
            setAttachments(toCopy.attachments.get(0));
            setNotes(toCopy.notes);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, eventDate, students, attachments, notes);
        }

        public void setEventName(String name) {
            this.name = name;
        }

        public Optional<String> getEventName() {
            return Optional.ofNullable(name);
        }

        public void setDate(LocalDateTime date) {
            eventDate = date;
        }

        public Optional<LocalDateTime> getDate() {
            return Optional.ofNullable(eventDate);
        }

        public void setAttachments(File file) {
            this.attachments.clear();
            this.attachments.add(file);
        }

        public Optional<List<File>> getAttachment() {
            return Optional.ofNullable(attachments);
        }

        /**
         * Ensures students in an event cannot be modified when event is editted.
         * The only way to add and remove students is through the command addStudent etc.
         */
        public void setStudents(List<Person> students) {
            this.students = students;
        }

        public Optional<List<Person>> getStudents() {
            return Optional.ofNullable(students);
        }

        /**
         * Ensures notes in an event cannot be modified when event is editted.
         * The only way to add and remove notes is through the command add-note etc.
         */
        public void setNotes(List<Note> notes) {
            this.notes = notes;
        }

        public Optional<List<Note>> getNotes() {
            return Optional.ofNullable(notes);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventCommand.EditEventDescriptor)) {
                return false;
            }

            // state check
            EditEventCommand.EditEventDescriptor e = (EditEventCommand.EditEventDescriptor) other;

            return getEventName().equals(e.getEventName())
                    && getDate().equals(e.getDate())
                    && getStudents().equals(e.getStudents())
                    && getNotes().equals(e.getNotes());
        }
    }


}
