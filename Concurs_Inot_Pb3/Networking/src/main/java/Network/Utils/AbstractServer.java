package Network.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket serverSocket = null;
    public static Logger logger = LogManager.getLogger(AbstractServer.class);

    public AbstractServer(int port) {this.port = port;}

    public void start() throws ServerException {
        try {
            logger.info("Starting server on port " + port);
            serverSocket = new ServerSocket(port);
            logger.info("Started server on port " + port);
            while(true){
                logger.info("Waiting for clients ...");
                Socket client=serverSocket.accept();
                logger.info("Client connected ...");
                processRequest(client);
            }
        }
        catch (IOException e) {
            logger.error(e);
            throw new ServerException("Starting server errror ",e);
        }
        finally {
            stop();
        }
    }

    protected abstract void processRequest(Socket client);

    public void stop() throws ServerException {
        try {
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        }
        catch (IOException e) {
            throw new ServerException("Closing server error ", e);
        }
    }
}
