package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateTutorialException;
import seedu.address.model.event.exceptions.TutorialNotFoundException;
import seedu.address.model.person.Person;

/**
 * A list of tutorial that enforces uniqueness between its elements and does not allow nulls.
 * A person is considered unique by comparing using {@code Person#isSamePerson(Person)}.
 * As such, adding and updating of tutorial uses Person#isSamePerson(Person) for equality to
 * ensure that the person being added or updated is unique in terms of identity in the UniquePersonList.
 * However, the removal of a person uses Person#equals(Object) to ensure that the person with exactly the
 * same fields will be removed.
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 */
public class UniqueTutorialList implements Iterable<Tutorial> {

    private final ObservableList<Tutorial> internalList = FXCollections.observableArrayList();
    private final ObservableList<Tutorial> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent tutorial as the given argument.
     */
    public boolean contains(Tutorial toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTutorial);
    }

    /**
     * Checks whether an event is contained in the list universally
     * @param nameOfEvent Name of event
     * @return A boolean indicator
     */
    public boolean containsEventName(String nameOfEvent) {
        requireNonNull(nameOfEvent);
        return internalList.stream().anyMatch(x -> x.hasMatchByName(nameOfEvent));
    }

    /**
     * Checks whether a note is contained universally
     * @param note Note to check upon
     * @return A boolean indicator
     */
    public boolean containsNote(Note note) {
        requireNonNull(note);
        Optional<NoteList> mergedList = internalList.stream().map(Event::getNoteList).reduce(NoteList::merge);
        return mergedList.map(noteList -> noteList.getNotes().stream().anyMatch(note::equals)).orElse(false);
    }

    // todo: probably try remove get and size methods to preserve abstraction barrier
    public Tutorial get(int index) {
        return this.internalList.get(index);
    }

    public int size() {
        return this.internalList.size();
    }

    /**
     * Adds a tutorial to the list.
     * The tutorial must not already exist in the list.
     */
    public void add(Tutorial toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTutorialException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the tutorial {@code target} in the list with {@code editedTutorial}.
     * {@code target} must exist in the list.
     * The tutorial identity of {@code editedTutorial} must not be the same as another existing tutorial in the list.
     */
    public void setTutorial(Tutorial target, Tutorial editedTutorial) {
        requireAllNonNull(target, editedTutorial);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TutorialNotFoundException();
        }

        if (!target.isSameTutorial(editedTutorial) && contains(editedTutorial)) {
            throw new DuplicateTutorialException();
        }

        internalList.set(index, editedTutorial);
    }

    /**
     * Removes the equivalent tutorial from the list.
     * The tutorial must exist in the list.
     */
    public void remove(Tutorial toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TutorialNotFoundException();
        }
    }

    public void setTutorials(UniqueTutorialList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code tutorials}.
     * {@code tutorials} must not contain duplicate tutorials.
     */
    public void setTutorials(List<Tutorial> tutorials) {
        requireAllNonNull(tutorials);
        if (!tutorialsAreUnique(tutorials)) {
            throw new DuplicateTutorialException();
        }

        internalList.setAll(tutorials);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Tutorial> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Tutorial> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTutorialList // instanceof handles nulls
                && internalList.equals(((UniqueTutorialList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code tutorials} contains only unique tutorials.
     */
    private boolean tutorialsAreUnique(List<Tutorial> tutorials) {
        for (int i = 0; i < tutorials.size() - 1; i++) {
            for (int j = i + 1; j < tutorials.size(); j++) {
                if (tutorials.get(i).isSameTutorial(tutorials.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
