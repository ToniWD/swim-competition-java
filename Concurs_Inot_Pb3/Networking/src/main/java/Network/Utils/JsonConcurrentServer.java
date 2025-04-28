package Network.Utils;

import Interfaces.IServices;
import Network.JsonProtocol.ClientJsonWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class JsonConcurrentServer extends AbsConcurrentServer {
    private IServices services;
    private static Logger logger = LogManager.getLogger(JsonConcurrentServer.class);

    public JsonConcurrentServer(int port, IServices services) {
        super(port);
        this.services = services;
        logger.info("JsonConcurrentServer started");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientJsonWorker worker = new ClientJsonWorker(services, client);

        Thread tw = new Thread(worker);
        return tw;
    }
}
