package ro.mpp2024.UI.ContainersFX;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ro.mpp2024.Domain.SwimmingEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SwimmingEventContainer extends HBox {

    private final Label label;
    private final Button button;
    private SwimmingEvent entity;

    public SwimmingEventContainer(double spacing, String textLabel, SwimmingEvent entity ,
                                  Consumer<SwimmingEventContainer> actionButton, String buttonText) {
        super(spacing);

        label = new Label(textLabel);
        button = new Button(buttonText);
        this.entity = entity;

        button.setOnAction(actionEvent -> {
            actionButton.accept(this);
        });

        this.getChildren().addAll(label, button);

        this.getStyleClass().add("containerListItem");
    }

    public void setButtonLabel(String textLabel) {
        button.setText(textLabel);
    }

    public Button getButton() {
        return button;
    }

    public void setLabel(String name) {
        this.label.setText(name);
    }

    public void setEntity(SwimmingEvent entity) {
        this.entity = entity;
    }

    public SwimmingEvent getEntity() {
        return entity;
    }
}
