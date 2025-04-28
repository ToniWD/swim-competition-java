package Controllers;

import Interfaces.IObserver;
import Interfaces.IServices;
import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Utils.ServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ContainersFX.ParticipantContainer;
import ContainersFX.SwimmingEventContainer;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPageController implements IObserver {
    private IServices mainService;
    private static final Logger logger = LogManager.getLogger(MainPageController.class);
    private SwimmingEvent currentEvent = null;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    ObservableList<SwimmingEvent> eventsModel = FXCollections.observableArrayList();
    ObservableList<Participant> participantsModel = FXCollections.observableArrayList();
    @FXML
    public ListView<SwimmingEvent> eventsListView;
    @FXML
    public ListView<Participant> participantsListView;
    @FXML
    public VBox participantsVBox;
    @FXML
    public TextField searchTextField;

    private Stage register = null;


    @FXML
    public void initialize() {

        searchTextField.setOnAction(event -> {
            updateParticipantsList(currentEvent);
        });

        Consumer<SwimmingEventContainer> consumerViewParticipants = this::handleViewParticipants;

        eventsListView.setCellFactory(new Callback<>() {

            @Override
            public ListCell<SwimmingEvent> call(ListView<SwimmingEvent> swimmingEventListView) {
                return new ListCell<SwimmingEvent>() {
                    private final SwimmingEventContainer container =
                            new SwimmingEventContainer(30.0,"",null,consumerViewParticipants, "View participants");

                    @Override
                    protected void updateItem(SwimmingEvent swimmingEvent, boolean empty) {
                        super.updateItem(swimmingEvent, empty);
                        if (empty || swimmingEvent == null) {
                            setGraphic(null);
                        }
                        else {

                            container.setLabel(
                                    "Style: " + swimmingEvent.getStyle() +
                                    "\nDistance: " + swimmingEvent.getDistance() +
                                    "\nParticipants: " + swimmingEvent.getNrParticipants()
                            );
                            container.setEntity(swimmingEvent);
                            setGraphic(container);
                        }
                    }
                };
            }
        });

        participantsListView.setCellFactory(new Callback<>() {

            @Override
            public ListCell<Participant> call(ListView<Participant> swimmingEventListView) {
                return new ListCell<Participant>() {
                    private final ParticipantContainer container =
                            new ParticipantContainer(30.0,"",null);

                    @Override
                    protected void updateItem(Participant entity, boolean empty) {
                        super.updateItem(entity, empty);
                        if (empty || entity == null) {
                            setGraphic(null);
                        }
                        else {

                            container.setLabel(
                                    entity.getFirstName() + " " + entity.getLastName() +
                                            "\nAge: " + entity.getAge() +
                                            "\nNr. events: " + entity.getNrEvents()
                            );
                            container.setEntity(entity);
                            setGraphic(container);
                        }
                    }
                };
            }
        });

        eventsListView.setItems(eventsModel);
        participantsListView.setItems(participantsModel);
        participantsVBox.setVisible(false);
        participantsVBox.setPrefWidth(0);
    }


    public void setMainService(IServices mainService) {
        this.mainService = mainService;
        mainService.setClient(this);
        updateEventList();
    }

    public void updateEventList(){
        logger.info("Updating swimming events list");
        Platform.runLater(() ->
        {
            try {
                Iterable<SwimmingEvent> events = mainService.getSwimmingEvents();

                List<SwimmingEvent> eventsList = StreamSupport.stream(events.spliterator(), false)
                        .collect(Collectors.toList());
                eventsModel.setAll(eventsList);
            }
            catch (ServiceException ex){
                logger.error(ex);
            }
            catch (Exception e) {
                logger.error(e);
            }
        });


    }

    public void handleViewParticipants(SwimmingEventContainer event) {

        logger.info("Updating participants list");
        try {
            currentEvent = event.getEntity();
            updateParticipantsList(currentEvent);
        }
        catch (ServiceException ex){
            logger.error(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        catch (Exception e) {
            logger.error(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK);
            alert.showAndWait();
        }

        participantsVBox.setVisible(true);
        participantsVBox.setPrefWidth(300);
    }

    private void updateParticipantsList(SwimmingEvent entity) {
        if(entity == null) {return;}

        Platform.runLater(() -> {
            Iterable<Participant> events = null;
            if(searchTextField.getText().isEmpty())
                events = mainService.getParticipantsByEvent(entity);
            else
                events = mainService.getParticipantsByEvent(entity, searchTextField.getText());

            List<Participant> participantList = StreamSupport.stream(events.spliterator(), false)
                    .collect(Collectors.toList());
            participantsModel.setAll(participantList);
        });

    }

    public void handleDisconnect(ActionEvent event) {
        try {
            // Încărcarea fișierului menu.fxml
            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/view/view_login.fxml"));
            Stage stage = (Stage) eventsListView.getScene().getWindow();
            // Setarea scenei și configurarea ferestrei
            Scene scene = new Scene(loaderLogin.load());

            LoginController controllerLogin = loaderLogin.getController();
            controllerLogin.setAuthService(mainService);

            new Thread(() -> {
                mainService.logout(user, this);
            }).start();

            register.close();

            stage.setOnCloseRequest(e -> {
                try{
                    logger.info("Exit application");
                }
                finally {
                    Platform.exit();
                }
            });

            stage.setTitle("Sign in");
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            logger.error(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK);
            alert.showAndWait();
        }
    }



    public void handleNewParticipant(ActionEvent event) {
        if(register != null && register.isShowing()) {
            register.toFront();
            return;
        }

        try {
            // Încărcarea fișierului menu.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view_register_participant.fxml"));
            Stage stage = new Stage();
            // Setarea scenei și configurarea ferestrei
            Scene scene = new Scene(loader.load());

            RegisterPageController controller = loader.getController();
            controller.setMainService(this.mainService);

            stage.setTitle("Register");
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setResizable(false);
            register = stage;
            stage.show();
        } catch (Exception e) {
            logger.error(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    public void Update() {
        logger.debug("Refresh la lista");
        Platform.runLater(() -> {
            if(currentEvent!=null)updateParticipantsList(this.currentEvent);
            updateEventList();
        });

    }
}
