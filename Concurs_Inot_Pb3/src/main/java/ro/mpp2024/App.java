package ro.mpp2024;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Validators.ParticipantValidator;
import ro.mpp2024.Repository.DBRepositories.ParticipantsDBRepo;
import ro.mpp2024.Repository.DBRepositories.RecordsDBRepo;
import ro.mpp2024.Repository.DBRepositories.SwimmingEventsDBRepo;
import ro.mpp2024.Repository.DBRepositories.UsersDBRepo;
import ro.mpp2024.Service.AuthService;
import ro.mpp2024.Service.Interfaces.IAuthService;
import ro.mpp2024.Service.Interfaces.IMainService;
import ro.mpp2024.Service.Interfaces.IServices;
import ro.mpp2024.Service.MainService;
import ro.mpp2024.Service.Services;
import ro.mpp2024.UI.LoginController;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class App extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {

        logger.info("Starting application");
        Properties prop = new Properties();
        try{
            prop.load(new FileReader("db.config"));
        }
        catch(IOException e){
           logger.error("Cannot find bd.config " + e);
        }

        IAuthService authService = new AuthService(new UsersDBRepo(prop));
        IMainService mainService = new MainService(
                new ParticipantsDBRepo(prop, new ParticipantValidator()),
                new SwimmingEventsDBRepo(prop),
                new RecordsDBRepo(prop)
                );
        IServices service = new Services(authService, mainService);

        try {
            // Încărcarea fișierului menu.fxml
            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/view/view_login.fxml"));

            // Setarea scenei și configurarea ferestrei
            Scene scene = new Scene(loaderLogin.load());

            LoginController controllerLogin = loaderLogin.getController();
            controllerLogin.setAuthService(service);

            primaryStage.setTitle("Sign in");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
