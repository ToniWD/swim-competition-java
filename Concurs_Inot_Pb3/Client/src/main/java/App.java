import Controllers.LoginController;
import Interfaces.IServices;
import Network.JsonProtocol.ServicesJsonProxy;
import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class App extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {

        logger.info("Starting application");
        Properties clientProps = new Properties();
        try{
            clientProps.load(App.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        }
        catch(IOException e){
           logger.error("Cannot find client.properties " + e);
           return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException var12) {
            NumberFormatException ex = var12;
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }


        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IServices server = new ServicesJsonProxy(serverIP, serverPort);

        try {
            // Încărcarea fișierului menu.fxml
            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/view/view_login.fxml"));

            // Setarea scenei și configurarea ferestrei
            Scene scene = new Scene(loaderLogin.load());

            LoginController controllerLogin = loaderLogin.getController();
            controllerLogin.setAuthService(server);

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
