package ro.mpp2024.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.SwimmingEvent;
import ro.mpp2024.Domain.Validators.ValidatorException;
import ro.mpp2024.Repository.RepoException;
import ro.mpp2024.Service.Interfaces.IMainService;
import ro.mpp2024.Service.MainService;
import ro.mpp2024.Service.ServiceException;
import ro.mpp2024.UI.ContainersFX.SwimmingEventContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RegisterPageController {
    private IMainService mainService;
    private static final Logger logger = LogManager.getLogger(RegisterPageController.class);
    ObservableList<SwimmingEvent> eventsModel = FXCollections.observableArrayList();
    private List<SwimmingEvent> addedSwimmingEvents = new ArrayList<>();

    @FXML
    public TextField firstNameTextField;
    @FXML
    public TextField lastNameTextField;
    @FXML
    public TextField ageTextField;
    @FXML
    public Label errorLabel;

    @FXML
    public ListView<SwimmingEvent> eventsListView;

    @FXML
    public void initialize() {
        Consumer<SwimmingEventContainer> consumerAddEvent = this::handleRegisterToEvent;

        eventsListView.setCellFactory(new Callback<>() {

            @Override
            public ListCell<SwimmingEvent> call(ListView<SwimmingEvent> swimmingEventListView) {
                return new ListCell<SwimmingEvent>() {
                    private final SwimmingEventContainer container =
                            new SwimmingEventContainer(30.0,"",null,consumerAddEvent, "+");

                    @Override
                    protected void updateItem(SwimmingEvent swimmingEvent, boolean empty) {
                        super.updateItem(swimmingEvent, empty);
                        if (empty || swimmingEvent == null) {
                            setGraphic(null);
                        }
                        else {

                            container.setLabel(
                                    "Style: " + swimmingEvent.getStyle() +
                                            "\nDistance: " + swimmingEvent.getDistance()
                            );
                            container.setEntity(swimmingEvent);

                            if(addedSwimmingEvents.contains(swimmingEvent)) {
                                container.setButtonLabel("-");
                                container.getButton().setStyle("-fx-background-color: #d63700");
                            }
                            else {
                                container.setButtonLabel("+");
                                container.getButton().setStyle("-fx-background-color: #FFB433");
                            }

                            setGraphic(container);
                        }
                    }
                };
            }
        });

        eventsListView.setItems(eventsModel);
    }

    public void setMainService(IMainService mainService) {
        this.mainService = mainService;
        updateEventList();
    }

    public void updateEventList(){
        logger.info("Updating swimming events list");
        try {
            Iterable<SwimmingEvent> events = mainService.getSwimmingEvents();

            List<SwimmingEvent> eventsList = StreamSupport.stream(events.spliterator(), false)
                    .collect(Collectors.toList());
            eventsModel.setAll(eventsList);
        }
        catch (RepoException | ServiceException ex){
            logger.error(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        catch (Exception e) {
            logger.error(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void handleRegisterToEvent(SwimmingEventContainer swimmingEvent) {
        if(addedSwimmingEvents.contains(swimmingEvent.getEntity())) {
            addedSwimmingEvents.remove(swimmingEvent.getEntity());
            swimmingEvent.setButtonLabel("+");
            swimmingEvent.getButton().setStyle("-fx-background-color: #FFB433");
        }
        else {
            addedSwimmingEvents.add(swimmingEvent.getEntity());
            swimmingEvent.setButtonLabel("-");
            swimmingEvent.getButton().setStyle("-fx-background-color: #d63700");
        }

    }

    public void handleRegisterParticipant(ActionEvent actionEvent) {
        try {
            if(addedSwimmingEvents.isEmpty()) {
                errorLabel.setText("No swimming events added");
                return;
            }

            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String age = ageTextField.getText();
            int ageInt = Integer.parseInt(age);

            mainService.addParticipant(firstName,lastName,ageInt,addedSwimmingEvents);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registered");
            alert.setHeaderText("The participant was added");
            alert.setContentText("Click ok to continue");

            alert.showAndWait();
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.close();

        }
        catch(NumberFormatException ex){
            errorLabel.setText("Invalid age");
        }
        catch (ValidatorException ex){
            errorLabel.setText(ex.getMessage());
        }
        catch (Exception ex){
            logger.error(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
