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
import java.util.Arrays;
import java.util.List;

public class ClientJsonWorker implements Runnable, IObserver {
    private IServices services;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ClientJsonWorker.class);

    public ClientJsonWorker(IServices services, Socket connection) {
        this.services = services;
        this.connection = connection;
        gsonFormatter = new Gson();
        try {
            output=new PrintWriter(connection.getOutputStream());
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected=true;
        }
        catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request){
        Response response=null;
        //completeaza
        if (request.getType()== RequestType.LOGIN){
            logger.debug("Login request ...{}",request.getUser().getUsername());
            User user= request.getUser();

            try {
                if(services.login(user.getUsername(), user.getPassword(), this))
                return okResponse;
                else throw new ServiceException("Login failed");
            } catch (ServiceException e) {
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.LOGOUT){
            logger.debug("Logout request {}",request.getUser().getUsername());
            User user= request.getUser();
            try {
                services.logout(user, this);
                connected=false;
                return okResponse;

            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_SWIMMING_EVENTS)
        {
            logger.debug("Get Swimming events request");
            try {
                Iterable<SwimmingEvent> it = services.getSwimmingEvents();
                List<SwimmingEvent> eventList = new ArrayList<>();

                for (SwimmingEvent e : it) {
                    eventList.add(e);
                }

                SwimmingEvent[] events = eventList.toArray(new SwimmingEvent[0]);

                return JsonProtocolUtils.createGetSwimmingEventsResponse(events);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_PARTICIPANTS_BY_EVENT)
        {
            logger.debug("Get Participants request");
            DTOSwimmingEvent dto = request.getSwimmingEvent();
            SwimmingEvent event = DTOUtils.getFromDTO(dto);

            String filter = request.getNameFilter();
            try {
                Iterable<Participant> it = null;
                if(filter!=null && !filter.isEmpty()){
                    it = services.getParticipantsByEvent(event, filter);
                }
                else
                {
                    it = services.getParticipantsByEvent(event);
                }
                List<Participant> eventList = new ArrayList<>();

                for (Participant e : it) {
                    eventList.add(e);
                }

                Participant[] objs = eventList.toArray(new Participant[0]);

                return JsonProtocolUtils.createGetParticipantsByEventResponse(objs);
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.ADD_PARTICIPANT)
        {
            logger.debug("Add participant request");
            DTOParticipant dto = request.getParticipant();
            Participant participant = DTOUtils.getFromDTO(dto);

            DTOSwimmingEvent[] dtoSwimmingEvents = request.getSwimmingEvents();
            SwimmingEvent[] swimmingEvents = DTOUtils.getFromDTO(dtoSwimmingEvents);
            try {
                List<SwimmingEvent> list = Arrays.stream(swimmingEvents).toList();
                services.addParticipant(participant.getFirstName(),participant.getLastName(),participant.getAge(),list);

                return JsonProtocolUtils.createAddParticipantResponse();
            } catch (ServiceException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        String responseLine=gsonFormatter.toJson(response);
        logger.debug("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    @Override
    public void Update() {
        logger.info("Update response for users");
        Response response = new Response();
        response.setType(ResponseType.UPDATE);
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            logger.error(e);
        }
    }
}
