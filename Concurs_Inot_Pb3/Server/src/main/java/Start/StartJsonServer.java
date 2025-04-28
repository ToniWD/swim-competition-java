package Start;

import DBRepositories.ParticipantsDBRepo;
import DBRepositories.RecordsDBRepo;
import DBRepositories.SwimmingEventsDBRepo;
import DBRepositories.UsersDBRepo;
import Interfaces.*;
import Network.Utils.AbstractServer;
import Network.Utils.JsonConcurrentServer;
import Network.Utils.ServerException;
import Validators.ParticipantValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Services.AuthService;
import Services.MainService;
import Services.Services;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort = 55555;
    private static Logger logger = LogManager.getLogger(StartJsonServer.class);

    public static void main(String[] args) {
        Properties serverProps = new Properties();

        try {
            serverProps.load(StartJsonServer.class.getResourceAsStream("/server.properties"));
            logger.info("Server properties set. {} ", serverProps);
        }
        catch (IOException e) {
            logger.error("Cannot find chatserver.properties "+e);
            logger.debug("Looking for file in "+(new File(".")).getAbsolutePath());
            return;
        }

        UsersRepository usersRepository = new UsersDBRepo(serverProps);
        ParticipantsRepository participantsRepository = new ParticipantsDBRepo(serverProps, new ParticipantValidator());
        SwimmingEventsRepository eventsRepository = new SwimmingEventsDBRepo(serverProps);
        RecordsRepository recordsRepository = new RecordsDBRepo(serverProps);


        IAuthService authService = new AuthService(usersRepository);
        IMainService mainService = new MainService(participantsRepository, eventsRepository, recordsRepository);
        IServices services = new Services(authService, mainService);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            logger.error("Wrong  Port Number"+nef.getMessage());
            logger.debug("Using default port "+defaultPort);
        }
        logger.debug("Starting server on port: "+serverPort);
        AbstractServer server = new JsonConcurrentServer(serverPort, services);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }


    }
}
