package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class GreetingBar extends UiPart<Region> {
    private static final String FXML = "GreetingBar.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private Label greetings;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public GreetingBar(ObservableList<Person> personList) {
        super(FXML);
        setGreetingCounter(personList);
    }

    void setGreetingCounter(ObservableList<Person> personList) {
        int size = personList.size();
        String greetingText = "Hello, you have " + size + " tasks undone";
        greetings.setText(greetingText);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}