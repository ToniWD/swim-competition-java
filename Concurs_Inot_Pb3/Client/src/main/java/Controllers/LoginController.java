package Controllers;

import Interfaces.IServices;
import Models.User;
import Utils.ServiceException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class LoginController  {
    private IServices authService;
    private static final Logger logger = LogManager.getLogger(IServices.class);
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Label errorLabel;

    private User user;

    public void setAuthService(IServices authService) {
        this.authService = authService;
    }

    public void login(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        errorLabel.setText("");
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/view_main_page.fxml"));
            Stage stage = (Stage) usernameTextField.getScene().getWindow();

            Scene scene = new Scene(mainLoader.load());

            MainPageController controller = mainLoader.getController();



            if(authService.login(username, password, controller)){
                //logat cu succes
                controller.setMainService(authService);

                User user = new User(username, password);
                this.user = user;

                controller.setUser(user);

                stage.setOnCloseRequest(event -> {

                    new Thread(() -> {
                        try {
                            authService.logout(this.user); // logout în fundal
                        } catch (Exception e) {
                            logger.error("Logout failed", e);
                        } finally {
                            logger.debug("Closing application");
                            Platform.exit(); // mai sigur decât System.exit(0)
                        }
                    }).start();
                });



                stage.setScene(scene);
                stage.setTitle("Menu");
                stage.setResizable(true);
                stage.setMinWidth(800);
                stage.setMinHeight(600);
                stage.show();
            }
            else{
                errorLabel.setText("Username or Password incorrect");
            }
        }
        catch (ServiceException e) {
            logger.error(e);
            errorLabel.setText("Username or Password incorrect");
        } catch (IOException e) {
            logger.error(e);
        }
//        catch(Exception e){
//            logger.error(e);
//            errorLabel.setText("There was an error while logging in");
//        }
    }

}
