package Network.JsonProtocol;


import Interfaces.IObserver;
import Interfaces.IServices;
import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Network.DTO.DTOParticipant;
import Network.DTO.DTOSwimmingEvent;
import Network.DTO.DTOUtils;
import Utils.ServiceException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesJsonProxy implements IServices {
    private String host;
    private int port;

    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    private User user;

    private static Logger logger = LogManager.getLogger(ServicesJsonProxy.class);

    public ServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }
    
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

    }

    private void sendRequest(Request request)throws ServiceException {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServiceException {
       Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }
    private void initializeConnection() throws ServiceException {
        try {
            gsonFormatter=new Gson();
            connection=new Socket(host,port);
            output=new PrintWriter(connection.getOutputStream());
            output.flush();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished=false;
            startReader();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response){
        logger.info("Update MenuControler");
        client.Update();
    }

    private boolean isUpdate(Response response){
        return response.getType() == ResponseType.UPDATE;
    }

    @Override
    public boolean login(String username, String password, IObserver client) throws ServiceException {
        initializeConnection();
        User user = new User(username,password);
        Request req = JsonProtocolUtils.createLoginRequest(user);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            this.client = client;
            return true;
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();

            closeConnection();
            throw new ServiceException(err);
        }
        return false;
    }

    @Override
    public Iterable<SwimmingEvent> getSwimmingEvents() throws ServiceException {
        Request req=JsonProtocolUtils.createGetSwimmingEventsRequest(user);

        sendRequest(req);

        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new ServiceException(err);
        }
        DTOSwimmingEvent[] objsDTO=response.getSwimmingEvents();
        SwimmingEvent[] objs= DTOUtils.getFromDTO(objsDTO);
        return List.of(objs);
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event) throws ServiceException {
        Request req=JsonProtocolUtils.createGetParticipantsByEventRequest(event);
        sendRequest(req);

        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        DTOParticipant[] objsDTO=response.getParticipants();
        Participant[] objs= DTOUtils.getFromDTO(objsDTO);
        return List.of(objs);
    }

    @Override
    public Iterable<Participant> getParticipantsByEvent(SwimmingEvent event, String fullName) throws ServiceException {
        Request req=JsonProtocolUtils.createGetParticipantsByEventRequest(event, fullName);
        sendRequest(req);

        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        DTOParticipant[] objsDTO=response.getParticipants();
        Participant[] objs= DTOUtils.getFromDTO(objsDTO);
        return List.of(objs);
    }

    @Override
    public void addParticipant(String firstName, String lastName, int age, Iterable<SwimmingEvent> swimmingEvents) throws ServiceException {
        logger.info("Add participant");
        Participant participant = new Participant(firstName,lastName,age);
        List<SwimmingEvent> eventList = new ArrayList<>();

        for (SwimmingEvent e : swimmingEvents) {
            eventList.add(e);
        }

        SwimmingEvent[] events = eventList.toArray(new SwimmingEvent[0]);
        Request req = JsonProtocolUtils.createAddParticipantRequest(participant, events);
        sendRequest(req);

        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new ServiceException(err);
        }
        logger.info("Added participant");
    }

    public void logout(User user, IObserver client) throws ServiceException {

        Request req=JsonProtocolUtils.createLogoutRequest(user);
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new ServiceException(err);
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    String responseLine=input.readLine();
                    logger.debug("response received {}",responseLine);
                    Response response=gsonFormatter.fromJson(responseLine, Response.class);
                    if(response == null) continue;
                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (IOException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }

    public IObserver getClient() {
        return client;
    }

    public void setClient(IObserver client) {
        this.client = client;
    }
}
