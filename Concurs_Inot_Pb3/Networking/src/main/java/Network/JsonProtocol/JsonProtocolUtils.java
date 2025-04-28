package Network.JsonProtocol;

import Models.Participant;
import Models.SwimmingEvent;
import Models.User;
import Network.DTO.DTOUtils;

public class JsonProtocolUtils {

    public static Response createOkResponse(){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Request createLoginRequest(User user){
        Request req=new Request();
        req.setType(RequestType.LOGIN);
        req.setUser(user);
        return req;
    }

    public static Response createErrorResponse(String errorMessage){
        Response resp=new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Request createLogoutRequest(User user) {
        Request req=new Request();
        req.setType(RequestType.LOGOUT);
        req.setUser(user);
        System.out.println(user.getUsername());
        return req;
    }

    public static Request createGetSwimmingEventsRequest(User user) {
        Request req=new Request();
        req.setUser(user);
        req.setType(RequestType.GET_SWIMMING_EVENTS);
        return req;
    }

    public static Response createGetSwimmingEventsResponse(SwimmingEvent[] events){
        Response resp=new Response();
        resp.setSwimmingEvents(DTOUtils.getDTO(events));
        resp.setType(ResponseType.UPDATE_SWIMMING_EVENTS);
        return resp;
    }

    public static Request createGetParticipantsByEventRequest(SwimmingEvent swimmingEvent) {
        Request req=new Request();
        req.setType(RequestType.GET_PARTICIPANTS_BY_EVENT);
        req.setSwimmingEvent(DTOUtils.getDTO(swimmingEvent));
        return req;
    }

    public static Request createGetParticipantsByEventRequest(SwimmingEvent swimmingEvent, String nameFilter) {
        Request req=new Request();
        req.setType(RequestType.GET_PARTICIPANTS_BY_EVENT);
        req.setSwimmingEvent(DTOUtils.getDTO(swimmingEvent));
        req.setNameFilter(nameFilter);
        return req;
    }

    public static Response createGetParticipantsByEventResponse(Participant[] participants){
        Response resp=new Response();
        resp.setParticipants(DTOUtils.getDTO(participants));
        resp.setType(ResponseType.UPDATE_PARTICIPANTS);
        return resp;
    }

    public static Request createAddParticipantRequest(Participant participant, SwimmingEvent[] swimmingEvents) {
        Request req=new Request();
        req.setType(RequestType.ADD_PARTICIPANT);
        req.setSwimmingEvents(DTOUtils.getDTO(swimmingEvents));
        req.setParticipant(DTOUtils.getDTO(participant));
        return req;
    }

    public static Response createAddParticipantResponse(){
        Response resp=new Response();
        resp.setType(ResponseType.UPDATE_PARTICIPANTS);
        return resp;
    }
}
