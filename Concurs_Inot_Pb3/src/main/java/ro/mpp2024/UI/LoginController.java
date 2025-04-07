package ro.mpp2024.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Validators.ParticipantValidator;
import ro.mpp2024.Repository.DBRepositories.ParticipantsDBRepo;
import ro.mpp2024.Repository.DBRepositories.RecordsDBRepo;
import ro.mpp2024.Repository.DBRepositories.SwimmingEventsDBRepo;
import ro.mpp2024.Service.AuthService;
import ro.mpp2024.Service.Interfaces.IAuthService;
import ro.mpp2024.Service.Interfaces.IMainService;
import ro.mpp2024.Service.MainService;
import ro.mpp2024.Service.ServiceException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class LoginController  {
    private IAuthService authService;
    private static final Logger logger = LogManager.getLogger(AuthService.class);
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Label errorLabel;

    public void setAuthService(IAuthService authService) {
        this.authService = authService;
    }

    public void login(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        errorLabel.setText("");
        try {
            if(authService.authentificate(username, password)){
                //logat cu succes

                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/view_main_page.fxml"));
                Stage stage = (Stage) usernameTextField.getScene().getWindow();

                Scene scene = new Scene(mainLoader.load());

                MainPageController controller = mainLoader.getController();
                controller.setMainService((IMainService) authService);

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
