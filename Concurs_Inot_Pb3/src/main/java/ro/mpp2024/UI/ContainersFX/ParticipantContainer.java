package ro.mpp2024.UI.ContainersFX;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ro.mpp2024.Domain.Participant;

import java.util.function.Consumer;

public class ParticipantContainer extends HBox {
    private final Label label;
    private Participant entity;

    public ParticipantContainer(double spacing, String textLabel, Participant entity) {
        super(spacing);

        label = new Label(textLabel);
        this.entity = entity;

        this.getChildren().add(label);

        this.getStyleClass().add("containerListItem");
    }

    public void setLabel(String textLabel) {
        label.setText(textLabel);
    }

    public Participant getEntity() {
        return entity;
    }

    public void setEntity(Participant entity) {
        this.entity = entity;
    }
}
